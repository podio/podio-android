
package com.podio.sdk;

public interface Request<T> {

    public static enum Method {
        DELETE, GET, POST, PUT
    }

    /**
     * Callback interface for error events.
     *
     * @author László Urszuly
     */
    public interface ErrorListener {

        /**
         * Delivers the cause of a request failure. The implementation must return boolean true if
         * the event is to be consumed (no subsequent listeners in the chain will be called) or
         * boolean false to allow bubbling of the event.
         *
         * @param cause
         *         The cause of the error.
         *
         * @return Boolean flag whether the event is to be consumed or not by this implementation.
         */
        public boolean onErrorOccurred(Throwable cause);

    }

    /**
     * Callback interface for successfully executed request events.
     *
     * @author László Urszuly
     */
    public interface ResultListener<E> {

        /**
         * Delivers the result of a successfully performed request. The implementation must return
         * boolean true if the event is to be consumed (no subsequent listeners in the chain will be
         * called) or boolean false to allow bubbling of the event.
         *
         * @param content
         *         The content that was requested.
         *
         * @return Boolean flag whether the event is to be consumed or not by this implementation.
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
         * Delivers the new session details on session change. The implementation must return
         * boolean true if the event is to be consumed (no subsequent listeners in the chain will be
         * called) or boolean false to allow bubbling of the event.
         *
         * @param authToken
         *         The new access token.
         * @param refreshToken
         *         The new refresh token.
         * @param transferToken
         *         The new transfer token.
         * @param expires
         *         The Unix epoch when the access token expires.
         *
         * @return Boolean flag whether the event is to be consumed or not by this implementation.
         */
        public boolean onSessionChanged(String authToken, String refreshToken, String transferToken, long expires);

    }

    /**
     * Make sure to catch any thrown PodioError
     * @param maxSeconds
     * @return
     * @throws PodioError
     */
    public T waitForResult(long maxSeconds) throws PodioError;

    public Request<T> withResultListener(ResultListener<T> contentListener);

    public Request<T> withErrorListener(ErrorListener errorListener);

    public Request<T> withSessionListener(SessionListener sessionListener);

}
