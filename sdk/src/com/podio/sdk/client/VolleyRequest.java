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
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.podio.sdk.PodioException;

public class VolleyRequest extends StringRequest {

    public static VolleyRequest newRequest(int method, Uri uri, String body, Map<String, String> headers) {
        RequestFuture<String> future = RequestFuture.newFuture();
        String url = uri.toString();

        VolleyRequest request = new VolleyRequest(method, url, future);
        request.contentType = "application/json; charset=UTF-8";
        request.headers.putAll(headers);
        request.body = body;

        return request;
    }

    public static VolleyRequest newRefreshRequest(Uri uri) {
        RequestFuture<String> future = RequestFuture.newFuture();
        String refreshToken = uri.getQueryParameter("refresh_token");
        String url = stripQueryParameters(uri);

        VolleyRequest request = new VolleyRequest(Method.POST, url, future);
        request.contentType = "application/json; charset=UTF-8";
        request.params.put("grant_type", "refresh_token");
        request.params.put("refresh_token", refreshToken);

        return request;
    }

    public static VolleyRequest newAuthRequest(Uri uri) {
        RequestFuture<String> future = RequestFuture.newFuture();
        Set<String> keys = uri.getQueryParameterNames();
        HashMap<String, String> params = new HashMap<String, String>();

        for (String key : keys) {
            String value = uri.getQueryParameter(key);
            params.put(key, value);
        }

        String url = stripQueryParameters(uri);

        VolleyRequest request = new VolleyRequest(Method.POST, url, future);
        request.contentType = "application/x-www-form-urlencoded; charset=UTF-8";
        request.params.putAll(params);

        return request;
    }

    private static String stripQueryParameters(Uri uri) {
        String url = uri.toString();

        int queryStart = url.indexOf("?");
        if (queryStart > 0) {
            url = url.substring(0, queryStart);
        }

        return url;
    }

    private RequestFuture<String> future;
    private HashMap<String, String> headers;
    private HashMap<String, String> params;
    private String contentType;
    private String body;

    private VolleyRequest(int method, String url, RequestFuture<String> future) {
        super(method, url, future, future);
        this.future = future;
        this.headers = new HashMap<String, String>();
        this.params = new HashMap<String, String>();
        this.body = null;

        setShouldCache(false);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return body != null ? body.getBytes() : super.getBody();
    }

    @Override
    public String getBodyContentType() {
        return contentType;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    public String waitForIt() throws PodioException {
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

}
