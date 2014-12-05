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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.podio.sdk.Request;
import com.podio.sdk.internal.CallbackManager;
import com.podio.sdk.internal.Utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

abstract class PushRequest<T> extends FutureTask<T> implements Request<T> {

    protected static enum State {
        unknown, initialized, connected, closed
    }

    private static final class ConnectData {
        @SuppressWarnings("unused")
        private final String channel;

        @SuppressWarnings("unused")
        private final String clientId;

        @SuppressWarnings("unused")
        private final String connectionType;

        private ConnectData(String clientId) {
            this.channel = "/meta/connect";
            this.clientId = clientId;
            this.connectionType = "long-polling";
        }
    }

    private static final class DisconnectData {
        @SuppressWarnings("unused")
        private final String channel;

        @SuppressWarnings("unused")
        private final String clientId;

        private DisconnectData(String clientId) {
            this.channel = "/meta/disconnect";
            this.clientId = clientId;
        }
    }

    private static final class ExtData {
        @SuppressWarnings("unused")
        private final String private_pub_signature;

        @SuppressWarnings("unused")
        private final String private_pub_timestamp;

        ExtData(String signature, String timestamp) {
            this.private_pub_signature = signature;
            this.private_pub_timestamp = timestamp;
        }
    }

    private static final class HandshakeData {
        @SuppressWarnings("unused")
        private final String channel;

        @SuppressWarnings("unused")
        private final String version;

        @SuppressWarnings("unused")
        private final String[] supportedConnectionTypes;

        private HandshakeData() {
            this.channel = "/meta/handshake";
            this.version = "1.0";
            this.supportedConnectionTypes = new String[]{"long-polling"};
        }
    }

    private static final class SubscribeData {
        @SuppressWarnings("unused")
        private final String channel;

        @SuppressWarnings("unused")
        private final String clientId;

        @SuppressWarnings("unused")
        private final String subscription;

        @SuppressWarnings("unused")
        private final ExtData ext;

        private SubscribeData(String clientId, String subscription, String signature, String timestamp) {
            this.channel = "/meta/subscribe";
            this.clientId = clientId;
            this.subscription = subscription;
            this.ext = new ExtData(signature, timestamp);
        }
    }

    private static final class UnsubscribeData {
        @SuppressWarnings("unused")
        private final String channel;

        @SuppressWarnings("unused")
        private final String clientId;

        @SuppressWarnings("unused")
        private final String subscription;

        private UnsubscribeData(String clientId, String subscription) {
            this.channel = "/meta/unsubscribe";
            this.clientId = clientId;
            this.subscription = subscription;
        }
    }

    private static State state = State.unknown;

    private static Status status;

    private static Status.Advice advice;

    protected static void connect(Transport transport) {
        // Get the current status.
        String clientId = status.clientId();
        int timeout = advice.reconnectTimeout();

        if (advice.reconnectApproach() == Status.Approach.handshake) {
            shakeHands(transport);
            clientId = status.clientId();
            timeout = advice.reconnectTimeout();
        }

        // Try to (re)connect. The connect request should return null,
        // while holding a long HTTP request open in the background.
        // In other words: there isn't a status reported by this call.
        transport.connect(new ConnectData(clientId), timeout);
        state = State.connected;
    }

    protected static void disconnect(Transport transport) {
        String clientId = status.clientId();
        String json = transport.disconnect(new DisconnectData(clientId));
        transport.close();

        parseStatus(json);
        state = State.closed;
    }

    static State getState() {
        return state;
    }

    protected static void shakeHands(Transport transport) {
        // Shake hands if not already done so.
        String json = transport.initialize(new HandshakeData());
        state = parseStatus(json).isSuccess() ? State.initialized : State.closed;

        // Hostile reception of handshake attempt. Be offended and abort.
        if (state != State.initialized) {
            throw new IllegalStateException("Expected: " + State.initialized + ", found: " + state);
        }
    }

    protected static void subscribe(Transport transport, String channel, String signature, String timestamp) {
        String clientId = status.clientId();
        String json = transport.configure(new SubscribeData(clientId, channel, signature, timestamp));
        parseStatus(json);
    }

    protected static void unsubscribe(Transport transport, String channel) {
        if (state != State.connected) {
            return;
        }

        String clientId = status.clientId();
        String json = transport.disconnect(new UnsubscribeData(clientId, channel));
        parseStatus(json);
    }

    private static Status parseStatus(String json) {
        if (Utils.isEmpty(json)) {
            status = new Status(); // Defaults to "unknown error" status.
            return status;
        }

        // Parse the json string into an object tree.
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);
        JsonArray jsonArray;

        // Make a general data structure (a JsonArray) to search for the
        // first available status object (sometimes the API delivers the
        // Status object on its own, and sometimes in an array).
        if (jsonElement.isJsonArray()) {
            jsonArray = jsonElement.getAsJsonArray();
        } else {
            jsonArray = new JsonArray();
            jsonArray.add(jsonElement);
        }

        // Now try to find the first available Status JsonObject which
        // meets our minimum criteria.
        int size = jsonArray.size();

        for (int i = 0; i < size; i++) {
            jsonElement = jsonArray.get(i);

            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                if (jsonObject.has("clientId") && jsonObject.has("channel") && jsonObject.has("successful")) {
                    status = (new Gson()).fromJson(jsonObject, Status.class);

                    if (status.hasAdvice()) {
                        advice = status.advice();
                    }

                    break;
                }
            }
        }

        return status;
    }

    /**
     * The delegate callback handler that will manage our callback interfaces for us.
     */
    private final CallbackManager<T> callbackManager;

    /**
     * The delivered result.
     */
    private T result;

    /**
     * The delivered error.
     */
    private Throwable error;

    PushRequest(Callable<T> callable) {
        super(callable);
        this.callbackManager = new CallbackManager<T>();
    }

    /**
     * Makes sure the result listeners are called properly when a result is delivered.
     *
     * @see java.util.concurrent.FutureTask#done()
     */
    @Override
    protected void done() {
        super.done();

        try {
            result = get();
            error = null;
            callbackManager.deliverResultOnMainThread(result);
        } catch (ExecutionException e) {
            result = null;
            error = e.getCause();
            callbackManager.deliverErrorOnMainThread(error);
        } catch (InterruptedException e) {
            result = null;
            error = e;
            callbackManager.deliverErrorOnMainThread(error);
        }
    }

    @Override
    public Request<T> withResultListener(Request.ResultListener<T> listener) {
        callbackManager.addResultListener(listener, isDone(), result);
        return null;
    }

    @Override
    public Request<T> withErrorListener(Request.ErrorListener listener) {
        callbackManager.addErrorListener(listener, isDone() && error != null, error);
        return null;
    }

    @Override
    public Request<T> withSessionListener(Request.SessionListener sessionListener) {
        throw new UnsupportedOperationException("This implementation doesn't handle sessions.");
    }

}
