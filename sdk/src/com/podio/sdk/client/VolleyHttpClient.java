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

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.podio.sdk.Parser;
import com.podio.sdk.PodioException;
import com.podio.sdk.RestClient;
import com.podio.sdk.SessionManager;
import com.podio.sdk.domain.Session;
import com.podio.sdk.parser.JsonParser;

/**
 * This class manages the communication between the client application and the
 * Podio servers.
 * 
 * @author László Urszuly
 */
public class VolleyHttpClient extends QueuedRestClient {

    private static final String SCHEME = "https";

    private final SessionManager sessionManager;
    private final RequestQueue requestQueue;

    /**
     * @param context
     *        The context to execute the database operations in.
     * @param authority
     *        The authority to use in URIs by this client.
     * @see QueuedRestClient
     * @see RestClient
     */
    public VolleyHttpClient(Context context, String authority, SessionManager sessionManager) {
        this(context, authority, Integer.MAX_VALUE, sessionManager);
    }

    /**
     * @param context
     *        The context to execute the database operations in.
     * @param authority
     *        The authority to use in URIs by this client.
     * @param capacity
     *        The desired request queue capacity.
     * @see QueuedRestClient
     * @see RestClient
     */
    public VolleyHttpClient(Context context, String authority, int capacity, SessionManager sessionManager) {
        super(SCHEME, authority, capacity);
        this.sessionManager = sessionManager;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    protected <T> RestResult<T> handleRequest(RestRequest<T> restRequest) {
        RestClient.Operation operation = restRequest.getOperation();
        Uri uri = restRequest.getFilter().buildUri(getScheme(), getAuthority());

        switch (operation) {
        case DELETE:
            return request(Method.DELETE, uri, restRequest.getContent(), restRequest.getParser(), true);
        case GET:
            return request(Method.GET, uri, restRequest.getContent(), restRequest.getParser(), true);
        case POST:
            return request(Method.POST, uri, restRequest.getContent(), restRequest.getParser(), true);
        case PUT:
            return request(Method.PUT, uri, restRequest.getContent(), restRequest.getParser(), true);
        default:
            throw new UnsupportedOperationException("Unknown operation: " + operation.name());
        }
    }

    private <T> RestResult<T> request(int method, Uri uri, Object item, Parser<? extends T, ?> parser, boolean tryRefresh) {
        if (!(parser instanceof JsonParser)) {
            throw new IllegalArgumentException("Invalid parser type: " + parser.getClass().getName() +
                    " Expected: " + JsonParser.class.getName());
        }

        @SuppressWarnings("unchecked")
        JsonParser<? extends T> jsonParser = (JsonParser<? extends T>) parser;

        String body = jsonParser.write(item);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + sessionManager.getAccessToken());

        VolleyRequest request = VolleyRequest.newRequest(method, uri, body, headers);
        requestQueue.add(request);

        try {
            Session refreshedSession = sessionManager.checkSession();
            String output = request.waitForResult();
            T content = jsonParser.read(output);

            return RestResult.success(content, refreshedSession);
        } catch (PodioException e) {
            if (e.isExpiredError() && tryRefresh) {
                sessionManager.refreshSession();
                return request(method, uri, item, parser, false);
            } else {
                throw e;
            }
        }
    }
}
