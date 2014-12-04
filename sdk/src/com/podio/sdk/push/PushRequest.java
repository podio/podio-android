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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.google.gson.Gson;
import com.podio.sdk.Request;
import com.podio.sdk.internal.CallbackManager;

class PushRequest<T> extends FutureTask<T> implements Request<T> {

    private static enum Machine {
        connected, handshaken, created, success, error
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
            this.supportedConnectionTypes = new String[] { "long-polling" };
        }
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

    protected static final class ExtData {
        @SuppressWarnings("unused")
        private final String private_pub_signature;

        @SuppressWarnings("unused")
        private final String private_pub_timestamp;

        ExtData(String signature, String timestamp) {
            this.private_pub_signature = signature;
            this.private_pub_timestamp = timestamp;
        }
    }

    private static Machine state = Machine.created;

    /**
     * @param transport
     * @throws IllegalStateException
     */
    protected static Status open(Transport transport) throws IllegalStateException {
        String json = transport.open(new HandshakeData());
        Status status = buildStatusObject(json);

        if (status.isSuccess() && status.hasSupportForConnectionType("long-polling")) {
            clientId = status.clientId();
            timeout = status.advicedReconnectTimeout();
        }

        return status;
    }

    protected static Status connect(Transport transport) throws IllegalStateException {
        String json = transport.connect(new ConnectData(clientId), timeout);
        return buildStatusObject(json);
    }

    protected static Status disconnect(Transport transport) throws IllegalStateException {
        String json = transport.disconnect(new DisconnectData(clientId));
        return buildStatusObject(json);
    }

    protected static Status send(Transport transport, Object data) {
        if (state == Machine.created || state == Machine.error) {
            state = open(transport).isSuccess() ?
                    Machine.handshaken :
                    Machine.error;
        }

        if (state == Machine.handshaken) {
            state = connect(transport).isSuccess() ?
                    Machine.connected :
                    Machine.error;
        }

        String json = transport.send(data);
        Status status = buildStatusObject(json);

        if (!status.isSuccess()) {
            state = Machine.error;

            switch (status.advicedReconnectApproach()) {
            case unknown:
                break;
            case handshake:
                open(transport).validate();
                state = Machine.handshaken;
                // Intentional fall-through
            case retry:
                connect(transport).validate();
                state = Machine.connected;
                // Intentional fall-through
            default:
                json = transport.send(data);
                status = buildStatusObject(json);
            }
        }

        status.validate();
        state = Machine.success;
        return status;
    }

    private static Status buildStatusObject(String json) {
        Gson gson = new Gson();
        Status status = gson.fromJson(json, Status.class);
        return status;
    }

    protected static String clientId;
    private static int timeout;

    /**
     * The delegate callback handler that will manage our callback interfaces
     * for us.
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
     * Makes sure the result listeners are called properly when a result is
     * delivered.
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
