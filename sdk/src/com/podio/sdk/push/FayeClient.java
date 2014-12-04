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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.podio.sdk.QueueClient;
import com.podio.sdk.Request;
import com.podio.sdk.Request.ErrorListener;
import com.podio.sdk.Request.ResultListener;

/**
 * @author László Urszuly
 */
public class FayeClient extends QueueClient implements Push {

    /**
     * A pure data structure class that bundles the caller provided listeners, a
     * template class definition (this is what the transport layer provided json
     * will be parsed into) and the original request object together for easy
     * management.
     * 
     * @author László Urszuly
     */
    private static class Subscription {
        private final ArrayList<ResultListener<?>> listeners;
        private final WeakReference<SubscribeRequest> request;
        private final Class<?> template;

        private Subscription(SubscribeRequest request, ResultListener<?> listener, Class<?> template) {
            this.listeners = new ArrayList<ResultListener<?>>();
            this.listeners.add(listener);
            this.request = new WeakReference<SubscribeRequest>(request);
            this.template = template;
        }
    }

    /**
     * The internal error listener channel between the push client and the
     * transport layer. It's up to this implementation to decide what is facing
     * the caller and what is not.
     */
    private final ErrorListener internalErrorListener = new ErrorListener() {

        @Override
        public boolean onErrorOccured(final Throwable cause) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    for (ErrorListener listener : errorListeners) {
                        listener.onErrorOccured(cause);
                    }
                }

            });

            return true;
        }

    };

    /**
     * The internal event listener channel between the push client and the
     * transport layer. This implementation is responsible for parsing the
     * provided json and call appropriate push event listeners.
     */
    private final ResultListener<String> internalEventListener = new ResultListener<String>() {

        @Override
        public boolean onRequestPerformed(final String json) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    Log.d("MYTAG", "Poll result: " + json);
                }

            });

            return true;
        }

    };

    /**
     * The list of active subscriptions, grouped by channel.
     */
    // TODO: Somehow maintain this map in case of irrecoverable errors
    private final HashMap<String, Subscription> subscriptions;

    /**
     * Error listener notifying the caller on any errors pushed from the Podio
     * API. Note that this is not where any direct request related errors will
     * be delivered. Those kind of errors (say an invalid token error during a
     * subscribe request) will be delivered through the returned request object
     * itself.
     */
    private final ArrayList<ErrorListener> errorListeners;

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
    public FayeClient(Transport transport) {
        super(1, 1, 0L);
        this.subscriptions = new HashMap<String, Subscription>();
        this.errorListeners = new ArrayList<ErrorListener>();
        this.transport = transport;
        this.transport.setErrorListener(internalErrorListener);
        this.transport.setEventListener(internalEventListener);
    }

    @Override
    public Request<Void> publish(String channel, String signature, String timestamp, Object data) {
        PublishRequest request = new PublishRequest(channel, signature, timestamp, data, transport);
        execute(request);
        return request;
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
    public Request<Void> subscribe(String channel, String signature, String timestamp, ResultListener<?> listener, Class<?> template) {
        if (subscriptions.containsKey(channel)) {
            Subscription subscription = subscriptions.get(channel);
            subscription.listeners.add(listener);
            return subscription.request.get();
        } else {
            SubscribeRequest request = new SubscribeRequest(channel, signature, timestamp, transport);
            Subscription subscription = new Subscription(request, listener, template);
            subscriptions.put(channel, subscription);
            execute(request);
            return request;
        }
    }

    /**
     * Removes the given listener from the stated push channel. If {@code null}
     * is provided as listener, then all listeners will be removed. If there are
     * no more listeners listening for events at the stated channel, a
     * termination request will be sent to the Podio API.
     */
    @Override
    public Request<Void> unsubscribe(String channel, ResultListener<?> listener) {
        if (listener != null) {
            if (subscriptions.containsKey(channel)) {
                subscriptions.get(channel).listeners.remove(listener);
            }

            return null;
        } else {
            if (subscriptions.containsKey(channel)) {
                subscriptions.remove(channel);
            }

            UnsubscribeRequest request = new UnsubscribeRequest(channel, transport);
            execute(request);
            return request;
        }
    }

    /**
     * Adds the given error listener to the list of push error observers.
     */
    @Override
    public Push addErrorListener(ErrorListener listener) {
        if (listener != null) {
            errorListeners.add(listener);
        }

        return this;
    }

    /**
     * Removes the given error listener from the list of push error observers.
     */
    @Override
    public Push removeErrorListener(ErrorListener listener) {
        errorListeners.remove(listener);
        return this;
    }

}
