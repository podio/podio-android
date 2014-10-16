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
package com.podio.sdk;

import java.util.concurrent.Future;

public interface Request<T> extends Future<T> {

    public static enum Method {
        DELETE, GET, POST, PUT
    }

    /**
     * A special-case error listener, targeting authorization errors. This
     * interface is intended to be used internally by the SDK only and enables
     * the auth managers to detect and act on auth errors.
     * 
     * @author László Urszuly
     */
    interface AuthErrorListener<E> {

        /**
         * Delivers the request that caught an authorization error to the
         * implementing receiver. This method should NOT - never ever - be
         * called by the client as it may heavily interfere with the internal
         * execution flow.
         * 
         * @param request
         *        The request that caught the authorization error.
         */
        public boolean onAuthErrorOccured(Request<E> request);

    }

    /**
     * Callback interface for error events.
     * 
     * @author László Urszuly
     */
    public interface ErrorListener {

        /**
         * Delivers the cause of a request failure. The implementation must
         * return boolean true if the event is to be consumed (no subsequent
         * listeners in the chain will be called) or boolean false to allow
         * bubbling of the event.
         * 
         * @param cause
         *        The cause of the error.
         * @return Boolean flag whether the event is to be consumed or not by
         *         this implementation.
         */
        public boolean onErrorOccured(Throwable cause);

    }

    /**
     * Callback interface for successfully executed request events.
     * 
     * @author László Urszuly
     */
    public interface ResultListener<E> {

        /**
         * Delivers the result of a successfully performed request. The
         * implementation must return boolean true if the event is to be
         * consumed (no subsequent listeners in the chain will be called) or
         * boolean false to allow bubbling of the event.
         * 
         * @param content
         *        The content that was requested.
         * @return Boolean flag whether the event is to be consumed or not by
         *         this implementation.
         */
        public boolean onRequestPerformed(E content);

    }

    /**
     * Callback interface for session change events.
     * 
     * @author László Urszuly
     */
    public interface SessionListener {

        /**
         * Delivers the new session details on session change. The
         * implementation must return boolean true if the event is to be
         * consumed (no subsequent listeners in the chain will be called) or
         * boolean false to allow bubbling of the event.
         * 
         * @param session
         *        The new session object.
         * @return Boolean flag whether the event is to be consumed or not by
         *         this implementation.
         */
        public boolean onSessionChanged(String authToken, String refreshToken, long expires);

    }

    public Request<T> withResultListener(ResultListener<T> contentListener);

    public Request<T> withErrorListener(ErrorListener errorListener);

    public Request<T> withSessionListener(SessionListener sessionListener);

}
