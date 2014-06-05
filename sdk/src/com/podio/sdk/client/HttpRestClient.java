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

import com.podio.sdk.PodioFilter;
import com.podio.sdk.PodioParser;
import com.podio.sdk.RestClient;
import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.client.delegate.HttpClientDelegate;
import com.podio.sdk.domain.Session;
import com.podio.sdk.internal.request.RestOperation;

/**
 * This class manages the communication between the client application and the
 * Podio servers.
 * 
 * @author László Urszuly
 */
public class HttpRestClient extends QueuedRestClient {

    private static final String SCHEME = "https";

    protected final RestClientDelegate networkDelegate;

    /**
     * @param context
     *            The context to execute the database operations in.
     * @param authority
     *            The authority to use in URIs by this client.
     * @param authToken
     *            The initial auth token to use when communicating with the
     *            Podio servers.
     * @param networkDelegate
     *            The {@link RestClientDelegate} implementation that will
     *            perform the actual HTTP request.
     * @param queueCapacity
     *            The custom request queue capacity.
     * 
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
    protected RestResult handleRequest(RestRequest restRequest) {
    	if (restRequest == null) {
    		throw new NullPointerException("restRequest cannot be null");
    	}
    	restRequest.validate();
		
    	PodioFilter filter = restRequest.getFilter();

		Uri uri = filter.buildUri(scheme, authority);

		RestOperation operation = restRequest.getOperation();
		Object item = restRequest.getContent();
		PodioParser<?> parser = restRequest.getParser();

		return queryNetwork(operation, uri, item, parser);
    }

    public void restoreSession(String refreshPath, Session session) {
        if (networkDelegate instanceof HttpClientDelegate) {
            Uri sessionRefreshUri = new Uri.Builder() //
                    .scheme(SCHEME) //
                    .authority(authority) //
                    .appendEncodedPath(refreshPath) //
                    .build();
            String url = sessionRefreshUri.toString();
            ((HttpClientDelegate) networkDelegate).restoreSession(url, session);
        }
    }

    private RestResult queryNetwork(RestOperation operation, Uri uri, Object item,
            PodioParser<?> parser) {

        switch (operation) {
        case AUTHORIZE:
            return networkDelegate.authorize(uri, parser);
        case DELETE:
            return networkDelegate.delete(uri, parser);
        case GET:
            return networkDelegate.get(uri, parser);
        case POST:
            return networkDelegate.post(uri, item, parser);
        case PUT:
            return networkDelegate.put(uri, item, parser);
        default:
            String message = "Unknown operation: " + operation.name();
        	throw new IllegalArgumentException(message);
        }
    }
}
