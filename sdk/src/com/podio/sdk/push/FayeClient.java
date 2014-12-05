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

import android.util.Log;

import com.podio.sdk.QueueClient;
import com.podio.sdk.Request.ErrorListener;
import com.podio.sdk.Request.ResultListener;
import com.podio.sdk.internal.CallbackManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author László Urszuly
 */
public class FayeClient extends QueueClient implements Push {


    /**
     * A pure data structure class that bundles the caller provided listeners, a template class
     * definition (this is what the transport layer provided json will be parsed into) and the
     * original request object together for easy management.
     *
     * @author László Urszuly
     */
    private static class Subscription {
        private final ArrayList<ResultListener<?>> listeners;
        private final Class<?> template;

        private Subscription(ResultListener<?> listener, Class<?> template) {
            this.listeners = new ArrayList<ResultListener<?>>();
            this.listeners.add(listener);
            this.template = template;
        }
    }

    /**
     * The internal error listener channel between the push client and the transport layer. It's up
     * to this implementation to decide what is facing the caller and what is not.
     */
    private final ErrorListener internalErrorListener = new ErrorListener() {

        @Override
        public boolean onErrorOccured(final Throwable cause) {
            cause.printStackTrace();
            return true;
        }

    };

    /**
     * The internal event listener channel between the push client and the transport layer. This
     * implementation is responsible for parsing the provided json and call appropriate push event
     * listeners.
     */
    private final ResultListener<String> internalEventListener = new ResultListener<String>() {

        @Override
        public boolean onRequestPerformed(final String json) {
            // This one should be running on the main thread.
            Log.d("MYTAG", "Poll result: " + json);

            // Reconnect if needed.
            if (PushRequest.getState() != PushRequest.State.closed) {
                ConnectRequest reconnectRequest = new ConnectRequest(transport);
                execute(reconnectRequest);
            }

            return true;
        }

    };

    /**
     * The list of active subscriptions, grouped by channel.
     */
    private final HashMap<String, Subscription> subscriptions;

    private final CallbackManager<Void> callbackManager;

    /**
     * The transport layer to be used by this push implementation.
     */
    private final Transport transport;

    /**
     * Initializes the push client to its default state.
     *
     * @param transport
     *         The transport implementation over which the push events and configurations will be
     *         sent and received.
     */
    public FayeClient(Transport transport) {
        super(1, 1, 0L);

        this.callbackManager = new CallbackManager<Void>();
        this.subscriptions = new HashMap<String, Subscription>();
        this.transport = transport;
        this.transport.setErrorListener(internalErrorListener);
        this.transport.setEventListener(internalEventListener);
    }

    @Override
    public void publish(String channel, String signature, String timestamp, Object data) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    /**
     * Subscribes to the given push channel in the Podio infrastructure. If there already is an
     * existing subscription, the given listener will simply be added to the list of event listeners
     * and the original request will be returned. Note that this request may be null by now as this
     * implementation only keeps a weak reference to it. The caller must therefore check for null
     * pointers.
     */
    @Override
    public void subscribe(String channel, String signature, String timestamp, ResultListener<?> listener, Class<?> template) {
        if (subscriptions.containsKey(channel)) {
            Subscription subscription = subscriptions.get(channel);
            subscription.listeners.add(listener);
        } else {
            Subscription subscription = new Subscription(listener, template);
            subscriptions.put(channel, subscription);
            execute(new SubscribeRequest(channel, signature, timestamp, transport));
        }
    }

    /**
     * Removes the given listener from the stated push channel. If {@code null} is provided as
     * listener, then all listeners will be removed. If there are no more listeners listening for
     * events at the stated channel, a termination request will be sent to the Podio API.
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
                Subscription subscription = subscriptions.get(channel);
                subscription.listeners.remove(listener);

                // If no more listener, then also unsubscribe at API level.
                if (subscription.listeners.size() == 0) {
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
    public Push addErrorListener(ErrorListener errorListener) {
        callbackManager.addErrorListener(errorListener, false, null);
        return this;
    }

    @Override
    public Push removeErrorListener(ErrorListener errorListener) {
        callbackManager.removeErrorListener(errorListener);
        return this;
    }

}
