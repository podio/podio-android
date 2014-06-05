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
	public SQLiteRestClient(Context context, String authority,
			RestClientDelegate databaseDelegate, int queueCapacity) {
		super("content", authority, queueCapacity);

		if (databaseDelegate == null) {
			throw new IllegalArgumentException(
					"The JsonClientDelegate mustn't be null");
		}

		this.databaseDelegate = databaseDelegate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected RestResult handleRequest(RestRequest restRequest) {
		Uri uri = restRequest.getFilter().buildUri(scheme, authority);

		return restRequest.getOperation().invoke(databaseDelegate, uri,
				restRequest.getContent(), restRequest.getParser());
	}

}
