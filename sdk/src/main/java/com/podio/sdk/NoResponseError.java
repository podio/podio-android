
package com.podio.sdk;

/**
 * This class represents a client side error caused by a request not delivering a response in a
 * timely manner.
 *
 * @author László Urszuly
 */
public class NoResponseError extends PodioError {

    public NoResponseError(String message, Throwable cause) {
        super(message, cause);
    }

    public NoResponseError(String message) {
        super(message);
    }

    public NoResponseError(Throwable cause) {
        super(cause);
    }

}
