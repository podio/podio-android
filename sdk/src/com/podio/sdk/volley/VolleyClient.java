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
package com.podio.sdk.volley;

import java.util.HashMap;
import java.util.Set;

import javax.net.ssl.SSLSocketFactory;

import android.content.Context;
import android.net.Uri;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.podio.sdk.Client;
import com.podio.sdk.Filter;
import com.podio.sdk.JsonParser;
import com.podio.sdk.PodioError;
import com.podio.sdk.Request;
import com.podio.sdk.Session;
import com.podio.sdk.internal.Utils;

public class VolleyClient implements Client, Request.ErrorListener, Request.ResultListener<Void> {

    static class AuthPath extends Filter {

        protected AuthPath() {
            super("oauth/token");
        }

        AuthPath withClientCredentials(String clientId, String clientSecret) {
            addQueryParameter("client_id", clientId);
            addQueryParameter("client_secret", clientSecret);
            return this;
        }

        AuthPath withUserCredentials(String username, String password) {
            addQueryParameter("grant_type", "password");
            addQueryParameter("username", username);
            addQueryParameter("password", password);
            return this;
        }

        AuthPath withAppCredentials(String appId, String appToken) {
            addQueryParameter("grant_type", "app");
            addQueryParameter("app_id", appId);
            addQueryParameter("app_token", appToken);
            return this;
        }

        AuthPath withRefreshToken(String refreshToken) {
            addQueryParameter("grant_type", "refresh_token");
            addQueryParameter("refresh_token", refreshToken);
            return this;
        }

    }

    private String scheme;
    private String authority;
    private RequestQueue volleyRequestQueue;
    private RequestQueue volleySessionQueue;
    private String clientId;
    private String clientSecret;
    private boolean isRefreshing;
    private VolleyRequest<?> currentRequest;

    @Override
    public Request<Void> authenticateWithUserCredentials(String username, String password) {
        Uri uri = new AuthPath()
                .withClientCredentials(clientId, clientSecret)
                .withUserCredentials(username, password)
                .buildUri(scheme, authority);

        String url = parseUrl(uri);
        HashMap<String, String> params = parseParams(uri);
        VolleyRequest<Void> request = VolleyRequest.newAuthRequest(url, params);
        volleyRequestQueue.add(request);

        return request;
    }

    @Override
    public Request<Void> authenticateWithAppCredentials(String appId, String appToken) {
        Uri uri = new AuthPath()
                .withClientCredentials(clientId, clientSecret)
                .withAppCredentials(appId, appToken)
                .buildUri(scheme, authority);

        String url = parseUrl(uri);
        HashMap<String, String> params = parseParams(uri);
        VolleyRequest<Void> request = VolleyRequest.newAuthRequest(url, params);
        volleyRequestQueue.add(request);

        return request;
    }

    @Override
    public boolean onRequestPerformed(Void nothing) {
        currentRequest.setSessionChanged(true);
        volleySessionQueue.add(currentRequest);
        volleyRequestQueue.start();
        return true;
    }

    @Override
    public boolean onErrorOccured(Throwable cause) {
        if (isRefreshing) {
            // An attempt to refresh the session has been made, but it failed.
            // Clear the request queue as all pending requests will fail without
            // a valid session.
            volleyRequestQueue.stop();

            VolleyError error;

            if (cause instanceof VolleyError) {
                error = (VolleyError) cause;
            } else if (cause.getCause() instanceof VolleyError) {
                error = (VolleyError) cause.getCause();
            } else {
                error = new VolleyError(cause);
            }

            currentRequest.removeErrorListener(this);
            currentRequest.deliverError(error);

            return true;
        } else if (cause instanceof PodioError && ((PodioError) cause).getStatusCode() == 401) {
            // The request has failed due to a 401 server response, try to
            // resolve the issue by refreshing the session.
            isRefreshing = true;
            volleyRequestQueue.stop();

            Uri uri = new AuthPath()
                    .withClientCredentials(clientId, clientSecret)
                    .withRefreshToken(Session.refreshToken())
                    .buildUri(scheme, authority);

            String url = parseUrl(uri);
            HashMap<String, String> params = parseParams(uri);
            VolleyRequest<Void> request = VolleyRequest
                    .newAuthRequest(url, params)
                    .withResultListener(this)
                    .withErrorListener(this);
            volleySessionQueue.add(request);

            return true;
        }

        return false;
    }

    @Override
    public <T> Request<T> request(Request.Method method, Filter filter, Object item, Class<T> classOfItem) {
        Uri uri = filter.buildUri(scheme, authority);
        String url = parseUrl(uri);
        String body = item != null ? JsonParser.toJson(item) : null;

        VolleyRequest<T> request = VolleyRequest.newRequest(method, url, body, classOfItem);
        request.withErrorListener(this);
        volleyRequestQueue.add(request);

        return request;
    }

    public void setup(Context context, String scheme, String authority, String clientId, String clientSecret, SSLSocketFactory sslSocketFactory) {
        this.scheme = scheme;
        this.authority = authority;
        this.clientId = clientId;
        this.clientSecret = clientSecret;

        if (sslSocketFactory == null) {
            this.volleyRequestQueue = Volley.newRequestQueue(context);
            this.volleySessionQueue = Volley.newRequestQueue(context);
        } else {
            HurlStack stack = new HurlStack(null, sslSocketFactory);
            this.volleyRequestQueue = Volley.newRequestQueue(context, stack);
            this.volleySessionQueue = Volley.newRequestQueue(context, stack);
        }
    }

    protected HashMap<String, String> parseParams(Uri uri) {
        Set<String> keys = uri.getQueryParameterNames();
        HashMap<String, String> params = new HashMap<String, String>();

        for (String key : keys) {
            String value = uri.getQueryParameter(key);
            params.put(key, value);
        }

        return params;
    }

    protected String parseUrl(Uri uri) {
        if (Utils.isEmpty(uri)) {
            return null;
        }

        String url = uri.toString();
        int queryStart = url.indexOf("?");

        if (queryStart > 0) {
            url = url.substring(0, queryStart);
        }

        return url;
    }

}
