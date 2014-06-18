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

package com.podio.sdk.client.delegate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.content.Context;
import android.net.Uri;

import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.podio.sdk.PodioParser;
import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.client.PodioException;
import com.podio.sdk.client.RestResult;
import com.podio.sdk.domain.Session;
import com.podio.sdk.internal.utils.Utils;

public class HttpClientDelegate implements RestClientDelegate {

    private final RequestQueue requestQueue;

    private Session session;
    private String refreshUrl;

    public HttpClientDelegate(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public RestResult<Session> authorize(Uri uri) throws PodioException {
        if (Utils.isEmpty(uri)) {
            throw new IllegalArgumentException("uri cannot be empty");
        }

        String url = removeQuery(uri);
        Map<String, String> params = queryToBody(uri);

        boolean success = authorizeRequest(url, params);

        if (success) {
            this.refreshUrl = url;

            return RestResult.success(session);
        } else {
            return RestResult.failure();
        }
    }

    @Override
    public <T> RestResult<T> delete(Uri uri, PodioParser<? extends T> parser) throws PodioException {
        return request(Method.DELETE, uri, null, parser, true);
    }

    @Override
    public <T> RestResult<T> get(Uri uri, PodioParser<? extends T> parser) throws PodioException {
        return request(Method.GET, uri, null, parser, true);
    }

    @Override
    public <T> RestResult<T> post(Uri uri, Object item, PodioParser<? extends T> parser) throws PodioException {
        return request(Method.POST, uri, item, parser, true);
    }

    @Override
    public <T> RestResult<T> put(Uri uri, Object item, PodioParser<? extends T> parser) throws PodioException {
        return request(Method.PUT, uri, item, parser, true);
    }

    /**
     * Revokes a previously stored session. The network delegate will use this
     * session object when authenticating API calls.
     * 
     * @param session
     *        The new session object to use.
     */
    public void restoreSession(String refreshUrl, Session session) {
        this.refreshUrl = refreshUrl;
        this.session = session;
    }

    private String getBlockingResponse(RequestFuture<String> future) throws PodioException {
        String json;

        try {
            json = future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            PodioException podioException = getPodioException(e);
            throw podioException;
        } catch (ExecutionException e) {
            PodioException podioException = getPodioException(e);
            throw podioException;
        } catch (TimeoutException e) {
            PodioException podioException = getPodioException(e);
            throw podioException;
        }

        return json;
    }

    private PodioException getPodioException(Exception cause) {
        if (cause instanceof ExecutionException) {
            VolleyError error = (VolleyError) cause.getCause();

            if (error == null || error.networkResponse == null || error.networkResponse.data == null) {
                return new PodioException(cause);
            }

            NetworkResponse response = error.networkResponse;
            byte[] errorData = response.data;
            int statusCode = response.statusCode;

            String json = new String(errorData);
            Gson gson = new Gson();

            PodioException exception = gson.fromJson(json, PodioException.class);
            exception.initCause(cause);
            exception.initStatusCode(statusCode);

            return exception;
        } else {
            return new PodioException(cause);
        }
    }

    private String removeQuery(Uri uri) {
        String url = uri.toString();

        int queryStart = url.indexOf("?");
        if (queryStart > 0) {
            url = url.substring(0, queryStart);
        }

        return url;
    }

    private Map<String, String> queryToBody(Uri uri) {
        Map<String, String> params = new HashMap<String, String>();

        Set<String> keys = uri.getQueryParameterNames();
        for (String key : keys) {
            String value = uri.getQueryParameter(key);
            params.put(key, value);
        }

        return params;
    }

    private <T> RestResult<T> request(int method, Uri uri, Object item, PodioParser<? extends T> parser, boolean tryRefresh) throws PodioException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + session.accessToken);

        String body = item != null ? body = parser.parseToJson(item) : null;

        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest request = new PodioRequest(method, uri.toString(), body, headers, future);
        requestQueue.add(request);

        try {
            boolean didRefreshSession = checkSession();
            String output = getBlockingResponse(future);
            T content = parser.parseToItem(output);

            return new RestResult<T>(true, didRefreshSession ? session : null, null, content);
        } catch (PodioException e) {
            if (e.isExpiredError() && tryRefresh) {
                refreshSession();
                return request(method, uri, item, parser, false);
            } else {
                throw e;
            }
        }
    }

    private boolean refreshSession() throws PodioException {
        Map<String, String> refreshParams = new HashMap<String, String>();
        refreshParams.put("grant_type", "refresh_token");
        refreshParams.put("refresh_token", session.refreshToken);

        return authorizeRequest(refreshUrl, refreshParams);
    }

    private boolean authorizeRequest(String url, Map<String, String> params) throws PodioException {
        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest request = new RefreshRequest(refreshUrl, params, future);

        requestQueue.add(request);

        String resultJson = getBlockingResponse(future);
        session = new Session(resultJson);

        return true;
    }

    private boolean checkSession() throws PodioException {
        if (session == null) {
            throw new IllegalStateException("No session is active");
        }

        if (!session.isAuthorized()) {
            throw new IllegalStateException("Session is not authorized");
        }

        if (session.shouldRefreshTokens()) {
            refreshSession();
            return true;
        }

        return false;
    }
}
