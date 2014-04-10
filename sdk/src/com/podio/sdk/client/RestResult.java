package com.podio.sdk.client;


/**
 * Wraps the result of a performed {@link RestRequest}.
 * 
 * @author László Urszuly
 */
public class RestResult {
    private final boolean isSuccess;
    private final String message;
    private final Object item;

    /**
     * Constructor. The one and only way to set the state of this object.
     * 
     * @param isSuccess
     *            Boolean true if this object represents a successfully
     *            performed {@link RestRequest}. Boolean false otherwise.
     * @param message
     *            A optional message provided to the caller by the creator of
     *            this object.
     * @param item
     *            Optional content of this object.
     */
    public RestResult(boolean isSuccess, String message, Object item) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.item = item;
    }

    /**
     * Returns the read-only success state.
     * 
     * @return Boolean true if the corresponding request was successfully
     *         performed. Boolean false otherwise.
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * Returns any items associated with the corresponding request. May be null.
     * 
     * @return A list of items or null.
     */
    public Object item() {
        return item;
    }

    /**
     * Returns any message provided by the underlying infrastructure. May be
     * null or empty.
     * 
     * @return A message string.
     */
    public String message() {
        return message;
    }

}
