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

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.podio.sdk.QueueClient;
import com.podio.sdk.Request;
import com.podio.sdk.Request.ErrorListener;
import com.podio.sdk.Request.ResultListener;
import com.podio.sdk.internal.Utils;

public class FayeClient extends QueueClient implements Push, ResultListener<String>, ErrorListener {

    private static class Subscription {
        private final ResultListener<?> listener;
        private final Class<?> template;

        private Subscription(ResultListener<?> listener, Class<?> template) {
            this.listener = listener;
            this.template = template;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            ResultListener<?> other = (o instanceof Subscription) ?
                    ((Subscription) o).listener : (o instanceof ResultListener<?>) ?
                            (ResultListener<?>) o : null;

            if (listener == other) {
                return true;
            }

            if (listener != null && listener.equals(other)) {
                return true;
            }

            return false;
        }

        @Override
        public int hashCode() {
            return listener != null ? listener.hashCode() : 0;
        }
    }

    private final HashMap<String, ArrayList<Subscription>> eventListeners;
    private final ArrayList<ErrorListener> errorListeners;
    private final Transport transport;

    public FayeClient(Transport transport) {
        super(1, 1, 0L);
        this.eventListeners = new HashMap<String, ArrayList<Subscription>>();
        this.errorListeners = new ArrayList<ErrorListener>();
        // this.url = "https://push.podio.com/faye";
        this.transport = transport;
        this.transport.setErrorListener(this);
        this.transport.setEventListener(this);
    }

    @Override
    public Request<Void> publish(String channel, String signature, String timestamp, Object data) {
        PublishRequest request = new PublishRequest(channel, signature, timestamp, data, transport);
        execute(request);
        return request;
    }

    @Override
    public Request<Void> subscribe(String channel, String signature, String timestamp) {
        SubscribeRequest request = new SubscribeRequest(channel, signature, timestamp, transport);
        execute(request);
        return request;
    }

    @Override
    public Request<Void> unsubscribe(String channel) {
        UnsubscribeRequest request = new UnsubscribeRequest(channel, transport);
        execute(request);
        return request;
    }

    @Override
    public Push addErrorListener(ErrorListener listener) {
        if (listener != null) {
            errorListeners.add(listener);
        }

        return this;
    }

    @Override
    public Push addEventListener(String channel, ResultListener<?> listener, Class<?> template) {
        if (Utils.notEmpty(channel) && template != null && listener != null) {
            ArrayList<Subscription> listeners = eventListeners.get(template);

            if (listeners == null) {
                listeners = new ArrayList<Subscription>();
                eventListeners.put(channel, listeners);
            }

            if (!listeners.contains(listener)) {
                listeners.add(new Subscription(listener, template));
            }
        }

        return this;
    }

    @Override
    public Push removeErrorListener(ErrorListener listener) {
        errorListeners.remove(listener);
        return this;
    }

    @Override
    public Push removeEventListener(ResultListener<?> listener) {
        Set<String> keySet = eventListeners.keySet();

        for (String key : keySet) {
            ArrayList<Subscription> subscriptions = eventListeners.get(key);

            if (Utils.notEmpty(subscriptions)) {
                subscriptions.remove(listener);
            }
        }

        return this;
    }

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

}
