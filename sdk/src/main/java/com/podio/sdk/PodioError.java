
package com.podio.sdk;

import com.android.volley.VolleyError;

/**
 * This class represents a generic client side error with an undefined or unknown cause.
 *
 * @author László Urszuly
 */
public class PodioError extends RuntimeException {

    private int responseCode;

    public PodioError(String message, Throwable cause) {
        super(message, cause);
        responseCode = 0;
    }

    public PodioError(String message) {
        super(message);
        responseCode = 0;
    }

    public PodioError(Throwable cause) {
        super(cause);
        responseCode = 0;
    }

    public PodioError(VolleyError volleyError, int responseCode) {
        super(volleyError);
        this.responseCode = responseCode;
    }

    /**
     * In the scenario that our API does not return a json error we will fallback to using
     * PodioError rather than ApiError and in such scenario this method will return the response
     * code but PodioError is used in more scenarios than actual API responses and in such scenarios
     * the response code returned will be 0 and thus not valid.
     *
     * @return
     */
    public int getResponseCode() {
        return responseCode;
    }
}
