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

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.podio.sdk.client.RestResult;
import com.podio.sdk.domain.Session;
import com.podio.sdk.internal.utils.Utils;

public class HttpClientDelegate extends JsonClientDelegate {

    private final RequestQueue requestQueue;

    private VolleyError lastRequestError;
    private Session session;
    private String refreshUrl;

    public HttpClientDelegate(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public RestResult authorize(Uri uri, ItemParser<?> itemParser) {
        String jsonString = null;
        String url = null;

        if (Utils.notEmpty(uri)) {
            url = parseUrl(uri);
            Map<String, String> body = parseBody(uri);

            RequestFuture<String> future = RequestFuture.newFuture();
            StringRequest request = new AuthRequest(url, body, future);
            requestQueue.add(request);
            jsonString = getBlockingResponse(future);
            session = new Session(jsonString);
        }

        boolean isSuccess = Utils.notEmpty(jsonString);
        refreshUrl = isSuccess ? url : null;
        RestResult result = new RestResult(isSuccess, session, null, null);

        return result;
    }

    @Override
    public RestResult delete(Uri uri, ItemParser<?> itemParser) {
        Session resultSession = tryRefreshSession();
        String outputJson = request(Method.DELETE, uri, null);

        if (outputJson == null && lastRequestError != null
                && lastRequestError.networkResponse != null
                && lastRequestError.networkResponse.statusCode == 401) {

            // For some reason the server has invalidated our access token.
            // Try refresh the access token again.

            resultSession = tryRefreshSession();
            outputJson = request(Method.DELETE, uri, null);
        }

        boolean isSuccess = Utils.notEmpty(outputJson);
        RestResult result = new RestResult(isSuccess, resultSession, null, null);

        return result;
    }

    @Override
    public RestResult get(Uri uri, ItemParser<?> itemParser) throws InvalidParserException {
        Session resultSession = tryRefreshSession();
        String outputJson = request(Method.GET, uri, null);

        if (outputJson == null && lastRequestError != null
                && lastRequestError.networkResponse != null
                && lastRequestError.networkResponse.statusCode == 401) {

            // For some reason the server has invalidated our access token.
            // Try refresh the access token again.

            resultSession = tryRefreshSession();
            outputJson = request(Method.GET, uri, null);
        }

        boolean isSuccess = Utils.notEmpty(outputJson);
        Object item = parseJson(outputJson, itemParser);
        RestResult result = new RestResult(isSuccess, resultSession, null, item);

        return result;
    }

    @Override
    public RestResult post(Uri uri, Object item, ItemParser<?> itemParser)
            throws InvalidParserException {
        Session resultSession = tryRefreshSession();
        String inputJson = parseItem(item, itemParser);
        String outputJson = request(Method.POST, uri, inputJson);

        if (outputJson == null && lastRequestError != null
                && lastRequestError.networkResponse != null
                && lastRequestError.networkResponse.statusCode == 401) {

            // For some reason the server has invalidated our access token.
            // Try refresh the access token again.

            resultSession = tryRefreshSession();
            outputJson = request(Method.POST, uri, inputJson);
        }

        boolean isSuccess = Utils.notEmpty(outputJson);
        Object content = parseJson(outputJson, itemParser);
        RestResult result = new RestResult(isSuccess, resultSession, null, content);

        return result;
    }

    @Override
    public RestResult put(Uri uri, Object item, ItemParser<?> itemParser)
            throws InvalidParserException {
        Session resultSession = tryRefreshSession();
        String inputJson = parseItem(item, itemParser);
        String outputJson = request(Method.PUT, uri, inputJson);

        if (outputJson == null && lastRequestError != null
                && lastRequestError.networkResponse != null
                && lastRequestError.networkResponse.statusCode == 401) {

            // For some reason the server has invalidated our access token.
            // Try refresh the access token again.

            resultSession = tryRefreshSession();
            outputJson = request(Method.PUT, uri, inputJson);
        }

        boolean isSuccess = Utils.notEmpty(outputJson);
        Object content = parseJson(outputJson, itemParser);
        RestResult result = new RestResult(isSuccess, resultSession, null, content);

        return result;
    }

    /**
     * Revokes a previously stored session. The network delegate will use this
     * session object when authenticating API calls.
     * 
     * @param session
     *            The new session object to use.
     */
    public void restoreSession(String refreshUrl, Session session) {
        this.refreshUrl = refreshUrl;
        this.session = session;
    }

    private String getBlockingResponse(RequestFuture<String> future) {
        String response;
        lastRequestError = null;

        try {
            response = future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            response = null;
        } catch (ExecutionException e) {
            lastRequestError = (VolleyError) e.getCause();
            e.printStackTrace();
            response = null;
        } catch (TimeoutException e) {
            e.printStackTrace();
            response = null;
        }

        return response;
    }

    private String parseUrl(Uri uri) {
        String url = "";

        if (Utils.notEmpty(uri)) {
            url = uri.toString();

            int queryStart = url.indexOf("?");
            if (queryStart > 0) {
                url = url.substring(0, queryStart);
            }
        }

        return url;
    }

    private Map<String, String> parseBody(Uri uri) {
        Map<String, String> params = new HashMap<String, String>();

        if (Utils.notEmpty(uri)) {
            Set<String> keys = uri.getQueryParameterNames();

            if (Utils.notEmpty(keys)) {
                for (String key : keys) {
                    String value = uri.getQueryParameter(key);
                    params.put(key, value);
                }
            }
        }

        return params;
    }

    private String request(int method, Uri uri, String body) {
        String result = null;

        if (Utils.notEmpty(uri) && session != null && session.isAuthorized()) {
            String url = uri.toString();
            String accessToken = session.accessToken;
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Authorization", "Bearer " + accessToken);

            RequestFuture<String> future = RequestFuture.newFuture();
            StringRequest request = new PodioRequest(method, url, body, headers, future);

            requestQueue.add(request);
            result = getBlockingResponse(future);
        }

        return result;
    }

    private Session tryRefreshSession() {
        Session copyOfNewSession = null;

        if (session != null && session.shouldRefreshTokens()) {
            Map<String, String> refreshParams = new HashMap<String, String>();
            refreshParams.put("grant_type", "refresh_token");
            refreshParams.put("refresh_token", session.refreshToken);

            RequestFuture<String> future = RequestFuture.newFuture();
            StringRequest request = new RefreshRequest(refreshUrl, refreshParams, future);

            requestQueue.add(request);
            String resultJson = getBlockingResponse(future);

            session = new Session(resultJson);
            copyOfNewSession = new Session(resultJson);
        }

        return copyOfNewSession;
    }
}
