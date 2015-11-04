
package com.podio.sdk;

/**
 * This class represents a client side error caused by an invalid connection state (typically
 * DISCONNECTED, CONNECTING or DISCONNECTING).
 *
 */
public class ConnectionError extends PodioError {

    public ConnectionError(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionError(String message) {
        super(message);
    }

    public ConnectionError(Throwable cause) {
        super(cause);
    }

}
