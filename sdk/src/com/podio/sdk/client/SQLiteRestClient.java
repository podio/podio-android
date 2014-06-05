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
import com.podio.sdk.internal.request.RestOperation;

/**
 * This class manages the communication between the client application and the
 * local Podio database cache.
 * 
 * @author László Urszuly
 */
public final class SQLiteRestClient extends QueuedRestClient {

    protected final RestClientDelegate databaseDelegate;

    /**
     * @param context
     *            The context to execute the database operations in.
     * @param authority
     *            The authority to use in URIs by this client.
     * @param databaseDelegate
     *            The {@link RestClientDelegate} implementation that will
     *            perform the SQLite queries.
     * @param queueCapacity
     *            The custom request queue capacity.
     * 
     * @see QueuedRestClient
     * @see RestClient
     */
    public SQLiteRestClient(Context context, String authority, RestClientDelegate databaseDelegate,
            int queueCapacity) {

        super("content", authority, queueCapacity);

        if (databaseDelegate == null) {
            throw new IllegalArgumentException("The JsonClientDelegate mustn't be null");
        } else {
            this.databaseDelegate = databaseDelegate;
        }
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
		PodioParser<?> parser = restRequest.getParser();

		RestOperation operation = restRequest.getOperation();
		Object item = restRequest.getContent();
		Uri uri = filter.buildUri(scheme, authority);

		return queryDatabase(operation, uri, item, parser);
    }

    /**
     * Delegates the requested operation to the {@link DatabaseClientDelegate}
     * to execute.
     * 
     * @param operation
     *            The type of rest operation to perform.
     * @param uri
     *            The URI that defines the details of the operation.
     * @param content
     *            Any additional data that the operation refers to.
     * @return An object representation of the result of the operation.
     */
    private RestResult queryDatabase(RestOperation operation, Uri uri, Object content,
            PodioParser<?> parser) {

        switch (operation) {
        case AUTHORIZE:
            return databaseDelegate.authorize(uri, parser);
        case DELETE:
            return databaseDelegate.delete(uri, parser);
        case GET:
            return databaseDelegate.get(uri, parser);
        case POST:
            return databaseDelegate.post(uri, content, parser);
        case PUT:
            return databaseDelegate.put(uri, content, parser);
        default:
            String message = "Unknown operation: " + operation.name();
            throw new IllegalArgumentException(message);
        }
    }

}
