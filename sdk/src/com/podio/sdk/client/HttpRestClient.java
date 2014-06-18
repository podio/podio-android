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

package com.podio.sdk.client;

import android.content.Context;
import android.net.Uri;

import com.podio.sdk.RestClient;
import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.client.delegate.HttpClientDelegate;
import com.podio.sdk.domain.Session;

/**
 * This class manages the communication between the client application and the
 * Podio servers.
 * 
 * @author László Urszuly
 */
public class HttpRestClient extends QueuedRestClient {

    private static final String SCHEME = "https";

    private final RestClientDelegate networkDelegate;

    /**
     * @param context
     *        The context to execute the database operations in.
     * @param authority
     *        The authority to use in URIs by this client.
     * @param authToken
     *        The initial auth token to use when communicating with the Podio
     *        servers.
     * @param networkDelegate
     *        The {@link RestClientDelegate} implementation that will perform
     *        the actual HTTP request.
     * @param queueCapacity
     *        The custom request queue capacity.
     * @see QueuedRestClient
     * @see RestClient
     */
    public HttpRestClient(Context context, String authority, RestClientDelegate networkDelegate,
            int queueCapacity) {
        super(SCHEME, authority, queueCapacity);

        if (networkDelegate == null) {
            throw new NullPointerException("The networkDelegate must not be null");
        }

        this.networkDelegate = networkDelegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected <T> RestResult<T> handleRequest(RestRequest<T> restRequest) throws PodioException {
        Uri uri = restRequest
                .getFilter()
                .buildUri(getScheme(), getAuthority());

        return restRequest
                .getOperation()
                .invoke(networkDelegate, uri, restRequest.getContent(), restRequest.getParser());
    }

    public void restoreSession(String refreshPath, Session session) {
        if (networkDelegate instanceof HttpClientDelegate) {
            Uri sessionRefreshUri = new Uri.Builder()
                    .scheme(SCHEME)
                    .authority(getAuthority())
                    .appendEncodedPath(refreshPath)
                    .build();
            String url = sessionRefreshUri.toString();
            ((HttpClientDelegate) networkDelegate).restoreSession(url, session);
        }
    }
}
