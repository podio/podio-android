package com.podio.sdk;

import com.podio.sdk.client.RestRequest;

/**
 * Manages the communication between the client application and the content
 * infrastructure.
 * 
 * The RestClient is responsible for validating all the possible result states
 * once the given request is performed and return either a generic success or
 * failure state. The client application should not be given any further
 * possibilities for analyzing state; it's either success or failure.
 * 
 * The RestClient is also responsible for maintaining proper means of spawning
 * worker threads (if necessary) on which the actual request is performed.
 * 
 * @author László Urszuly
 */
public interface RestClient {

    /**
     * Returns the authority string for this rest client. The authority is the
     * 'www.google.com' part in 'http://www.google.com/query?a=b' or the
     * 'com.podio' part in 'content://com.podio/provider/item/12'.
     * 
     * @return The authority of this client.
     */
    public String getAuthority();

    /**
     * Returns the scheme for this rest client. The scheme is the 'http' part in
     * 'http://www.google.com' or the 'content' part in 'content://com.podio'.
     * 
     * @return The scheme of this client.
     */
    public String getScheme();

    /**
     * Performs an initial validation of the request and returns a boolean flag
     * depending on whether the request is accepted for future processing or
     * not.
     * 
     * @param request
     *            The request that this client should perform at some time in
     *            the near future.
     * @return Boolean true if the client accepted the request, boolean false
     *         otherwise.
     */
    public boolean perform(RestRequest request);

}
