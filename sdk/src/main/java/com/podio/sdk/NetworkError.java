
package com.podio.sdk;

/**
 * This class represents a network error when trying to perform the request.
 *
 */
public class NetworkError extends PodioError {

    public NetworkError(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkError(String message) {
        super(message);
    }

    public NetworkError(Throwable cause) {
        super(cause);
    }

}
