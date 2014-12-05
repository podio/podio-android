/*
 *  Copyright (C) 2014 Copyright Citrix Systems, Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of
 *  this software and associated documentation files (the "Software"), to deal in
 *  the Software without restriction, including without limitation the rights to
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to
 *  do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package com.podio.sdk.push;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.podio.sdk.QueueClient;
import com.podio.sdk.Request.ErrorListener;
import com.podio.sdk.Request.ResultListener;
import com.podio.sdk.domain.push.Event;
import com.podio.sdk.internal.CallbackManager;
import com.podio.sdk.internal.Utils;

/**
 * @author László Urszuly
 */
public class FayePushClient extends QueueClient implements PushClient {

    /**
     * The list of active subscriptions, grouped by channel.
     */
    private final HashMap<String, ArrayList<ResultListener<Event[]>>> subscriptions;

    /**
     * Manages the external error listeners.
     * <p/>
     * TODO: Investigate the effort needed to add "grouping" support to
     * CallbackManager, much like the "subscriptions" map in this class.
     */
    private final CallbackManager<Event[]> callbackManager;

    /**
     * The transport layer to be used by this push implementation.
     */
    private final Transport transport;

    /**
     * Initializes the push client to its default state.
     *
     * @param transport
     *        The transport implementation over which the push events and
     *        configurations will be sent and received.
     */
    public FayePushClient(Transport transport) {
        super(1, 1, 0L);

        this.callbackManager = new CallbackManager<Event[]>();
        this.subscriptions = new HashMap<String, ArrayList<ResultListener<Event[]>>>();
        this.transport = transport;

        // The internal error listener channel between the push client and the
        // transport layer. It's up to this implementation to decide what is
        // facing the caller and what is not.
        this.transport.setErrorListener(new ErrorListener() {

            @Override
            public boolean onErrorOccured(Throwable cause) {
                // Shut down everything and clear any subscriptions.
                Transport transport = FayePushClient.this.transport;
                execute(new DisconnectRequest(transport));
                subscriptions.clear();

                // Tell the world.
                callbackManager.deliverErrorOnMainThread(cause);
                return true;
            }

        });

        // The internal event listener channel between the push client and the
        // transport layer. This implementation is responsible for parsing the
        // provided json and call appropriate push event listeners.
        this.transport.setEventListener(new ResultListener<String>() {

            @Override
            public boolean onRequestPerformed(String json) {
                // This one should be running on the main thread.

                // Reconnect if needed.
                if (PushRequest.getState() != PushRequest.State.closed) {
                    Transport transport = FayePushClient.this.transport;
                    ConnectRequest reconnectRequest = new ConnectRequest(transport);
                    execute(reconnectRequest);
                }

                // Parse the delivered json events.
                HashMap<String, ArrayList<Event>> events = parseEventData(json);
                deliverEvents(events);

                return true;
            }

        });
    }

    @Override
    public void publish(String channel, String signature, String timestamp, Object data) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    /**
     * Subscribes to the given push channel in the Podio infrastructure. If
     * there already is an existing subscription, the given listener will simply
     * be added to the list of event listeners and the original request will be
     * returned. Note that this request may be null by now as this
     * implementation only keeps a weak reference to it. The caller must
     * therefore check for null pointers.
     */
    @Override
    public void subscribe(String channel, String signature, String timestamp, ResultListener<Event[]> listener) {
        if (subscriptions.containsKey(channel)) {
            ArrayList<ResultListener<Event[]>> listeners = subscriptions.get(channel);
            listeners.add(listener);
        } else {
            ArrayList<ResultListener<Event[]>> listeners = new ArrayList<ResultListener<Event[]>>();
            listeners.add(listener);
            subscriptions.put(channel, listeners);
            execute(new SubscribeRequest(channel, signature, timestamp, transport));
        }
    }

    /**
     * Removes the given listener from the stated push channel. If {@code null}
     * is provided as listener, then all listeners will be removed. If there are
     * no more listeners listening for events at the stated channel, a
     * termination request will be sent to the Podio API.
     */
    @Override
    public void unsubscribe(String channel, ResultListener<?> listener) {
        if (listener == null) {
            // Remove all subscriptions for the given channel.
            if (subscriptions.containsKey(channel)) {
                subscriptions.remove(channel);
                execute(new UnsubscribeRequest(channel, transport));
            }
        } else {
            // Remove the given listener for the given channel.
            if (subscriptions.containsKey(channel)) {
                ArrayList<ResultListener<Event[]>> listeners = subscriptions.get(channel);
                listeners.remove(listener);

                // If no more listener, then also unsubscribe at API level.
                if (listeners.size() == 0) {
                    subscriptions.remove(channel);
                    execute(new UnsubscribeRequest(channel, transport));
                }
            }
        }

        if (subscriptions.isEmpty()) {
            execute(new DisconnectRequest(transport));
        }
    }

    @Override
    public PushClient addErrorListener(ErrorListener errorListener) {
        callbackManager.addErrorListener(errorListener, false, null);
        return this;
    }

    @Override
    public PushClient removeErrorListener(ErrorListener errorListener) {
        callbackManager.removeErrorListener(errorListener);
        return this;
    }

    private HashMap<String, ArrayList<Event>> parseEventData(String json) {
        HashMap<String, ArrayList<Event>> result = new HashMap<String, ArrayList<Event>>();

        if (Utils.isEmpty(json)) {
            return result;
        }

        // Parse the root json element.
        JsonParser jsonParser = new JsonParser();
        JsonElement root = jsonParser.parse(json);
        JsonArray jsonElements;

        // Make sure we're always operating on an array of objects.
        if (root.isJsonArray()) {
            jsonElements = root.getAsJsonArray();
        } else if (root.isJsonObject()) {
            jsonElements = new JsonArray();
            jsonElements.add(root.getAsJsonObject());
        } else {
            return result;
        }

        JsonObject jsonObject;
        Gson gson = new Gson();

        // Search for all data bearing event objects.
        for (JsonElement jsonElement : jsonElements) {
            if (jsonElement.isJsonObject()) {
                jsonObject = jsonElement.getAsJsonObject();

                JsonObject fayeData = getJsonObject(jsonObject, "data");
                JsonObject podioData = getJsonObject(fayeData, "data");

                String key = getString(jsonObject, "channel");
                String type = getString(podioData, "event");

                try {
                    Event.Type eventType = Event.Type.valueOf(type);
                    Event event = gson.fromJson(fayeData, eventType.getClassObject());
                    addEventToMap(key, event, result);
                } catch (NullPointerException e) {
                } catch (IllegalArgumentException e) {
                }
            }
        }

        return null;
    }

    private JsonObject getJsonObject(JsonObject parent, String member) {
        return parent != null && parent.has(member) ? parent.getAsJsonObject(member) : new JsonObject();
    }

    private String getString(JsonObject parent, String member) {
        return parent != null && parent.has(member) ? parent.get(member).getAsString() : "";
    }

    private void addEventToMap(String key, Event event, HashMap<String, ArrayList<Event>> map) {
        if (event != null && Utils.notEmpty(key) && Utils.notEmpty(map)) {
            if (map.containsKey(key)) {
                map.get(key).add(event);
            } else {
                ArrayList<Event> events = new ArrayList<Event>();
                events.add(event);
                map.put(key, events);
            }
        }
    }

    private void deliverEvents(HashMap<String, ArrayList<Event>> events) {
        if (events == null) {
            return;
        }

        Set<String> keys = events.keySet();

        // ...and deliver to their corresponding listeners.
        for (String key : keys) {
            ArrayList<Event> eventsList = events.get(key);
            Event[] eventsArray = new Event[eventsList.size()];
            eventsList.toArray(eventsArray);

            ArrayList<ResultListener<Event[]>> listeners = subscriptions.get(key);
            for (ResultListener<Event[]> listener : listeners) {
                listener.onRequestPerformed(eventsArray);
            }
        }
    }
}
