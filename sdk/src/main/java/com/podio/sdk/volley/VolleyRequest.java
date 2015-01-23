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

import com.android.volley.AuthFailureError;
import com.android.volley.Cache.Entry;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.RequestFuture;
import com.podio.sdk.JsonParser;
import com.podio.sdk.PodioError;
import com.podio.sdk.Session;
import com.podio.sdk.internal.Utils;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VolleyRequest<T> extends Request<T> implements com.podio.sdk.Request<T> {

    public static ErrorListener addGlobalErrorListener(ErrorListener errorListener) {
        return VolleyCallbackManager.addGlobalErrorListener(errorListener);
    }

    public static SessionListener addGlobalSessionListener(SessionListener sessionListener) {
        return VolleyCallbackManager.addGlobalSessionListener(sessionListener);
    }

    public static ErrorListener removeGlobalErrorListener(ErrorListener errorListener) {
        return VolleyCallbackManager.addGlobalErrorListener(errorListener);
    }

    public static SessionListener removeGlobalSessionListener(SessionListener sessionListener) {
        return VolleyCallbackManager.removeGlobalSessionListener(sessionListener);
    }

    static <E> VolleyRequest<E> newRequest(com.podio.sdk.Request.Method method, String url, String body, Class<E> classOfResult) {
        RequestFuture<E> volleyRequestFuture = RequestFuture.newFuture();
        int volleyMethod = parseMethod(method);

        VolleyRequest<E> request = new VolleyRequest<E>(volleyMethod, url, classOfResult, volleyRequestFuture, false);
        request.contentType = "application/json; charset=UTF-8";
        request.headers.put("X-Time-Zone", Calendar.getInstance().getTimeZone().getID());

        if (Utils.notEmpty(Session.accessToken())) {
            request.headers.put("Authorization", "Bearer " + Session.accessToken());
        }

        request.body = body;

        return request;
    }

    static VolleyRequest<Void> newAuthRequest(String url, Map<String, String> params) {
        RequestFuture<Void> volleyRequestFuture = RequestFuture.newFuture();
        int volleyMethod = parseMethod(com.podio.sdk.Request.Method.POST);

        VolleyRequest<Void> request = new VolleyRequest<Void>(volleyMethod, url, null, volleyRequestFuture, true);
        request.contentType = "application/x-www-form-urlencoded; charset=UTF-8";
        request.params.putAll(params);

        return request;
    }

    protected static int parseMethod(com.podio.sdk.Request.Method method) {
        switch (method) {
            case DELETE:
                return com.android.volley.Request.Method.DELETE;
            case GET:
                return com.android.volley.Request.Method.GET;
            case POST:
                return com.android.volley.Request.Method.POST;
            case PUT:
                return com.android.volley.Request.Method.PUT;
            default:
                return com.android.volley.Request.Method.GET;
        }
    }

    private final VolleyCallbackManager<T> callbackManager;

    private final RequestFuture<T> volleyRequestFuture;
    private final Class<T> classOfResult;

    protected HashMap<String, String> headers;
    protected HashMap<String, String> params;
    protected String contentType;
    protected String body;

    private T result;
    private Throwable error;
    private boolean isDone;
    private boolean isAuthRequest;
    private boolean hasSessionChanged;

    protected VolleyRequest(int method, String url, Class<T> resultType, RequestFuture<T> volleyRequestFuture, boolean isAuthRequest) {
        super(method, url, volleyRequestFuture);

        setShouldCache(false);

        this.callbackManager = new VolleyCallbackManager<T>();

        this.volleyRequestFuture = volleyRequestFuture;
        this.volleyRequestFuture.setRequest(this);
        this.classOfResult = resultType;

        this.headers = new HashMap<String, String>();
        this.params = new HashMap<String, String>();
        this.body = null;

        this.hasSessionChanged = false;
        this.isAuthRequest = isAuthRequest;
    }

    @Override
    public VolleyRequest<T> withResultListener(ResultListener<T> resultListener) {
        callbackManager.addResultListener(resultListener, isDone, result);
        return this;
    }

    @Override
    public VolleyRequest<T> withErrorListener(ErrorListener errorListener) {
        callbackManager.addErrorListener(errorListener, isDone && error != null, error);
        return this;
    }

    @Override
    public VolleyRequest<T> withSessionListener(SessionListener sessionListener) {
        callbackManager.addSessionListener(sessionListener, isDone && hasSessionChanged);
        return this;
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
        String accessToken = Session.accessToken();

        if (!isAuthRequest && Utils.notEmpty(accessToken)) {
            headers.put("Authorization", "Bearer " + accessToken);
        } else {
            headers.remove("Authorization");
        }

        return headers;
    }

    @Override
    public synchronized T waitForResult(long maxSeconds) {
        try {
            wait(TimeUnit.SECONDS.toMillis(maxSeconds > 0 ? maxSeconds : 0));
        } catch (InterruptedException e) {
            callbackManager.deliverError(e);
            return null;
        }

        return result;
    }

    @Override
    public void deliverError(VolleyError error) {
        // This method is executed on the main thread. Extra care should be
        // taken on what is done here.
        isDone = true;
        callbackManager.deliverError(this.error);
    }

    @Override
    protected void deliverResponse(T result) {
        // This method is executed on the main thread. Extra care should be
        // taken on what is done here.

        this.isDone = true;

        if (hasSessionChanged) {
            callbackManager.deliverSession();
        }

        callbackManager.deliverResult(result);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (params.containsKey("refresh_token")) {
            String refreshToken = Session.refreshToken();
            params.put("refresh_token", refreshToken);
        }

        return params;
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        // This method is executed on the worker thread. It's "safe" to perform
        // JSON parsing here.

        try {
            String charSet = HttpHeaderParser.parseCharset(volleyError.networkResponse.headers);
            String errorJson = new String(volleyError.networkResponse.data, charSet);
            error = PodioError.fromJson(errorJson, volleyError.networkResponse.statusCode, volleyError);
        } catch (UnsupportedEncodingException e) {
            // The provided error JSON is provided with an unknown char-set.
            error = volleyError;
        } catch (NullPointerException e) {
            // For some reason the VollyError didn't provide a networkResponse.
            error = volleyError;
        }

        synchronized (this) {
            notifyAll();
        }

        return super.parseNetworkError(volleyError);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        // This method is executed on the worker thread. It's "safe" to perform
        // JSON parsing here.
        Response<T> response;

        try {
            Entry cacheHeaders = HttpHeaderParser.parseCacheHeaders(networkResponse);
            String charSet = HttpHeaderParser.parseCharset(networkResponse.headers);
            String json = new String(networkResponse.data, charSet);

            if (isAuthRequest) {
                Session.set(json);
                hasSessionChanged = true;
                result = null;
                response = Response.success(null, cacheHeaders);
            } else if (classOfResult == null || classOfResult == Void.class) {
                result = null;
                response = Response.success(null, cacheHeaders);
            } else {
                result = JsonParser.fromJson(json, classOfResult);
                response = Response.success(result, cacheHeaders);
            }
        } catch (UnsupportedEncodingException e) {
            // The provided response JSON is provided with an unknown char-set.
            result = null;
            response = Response.error(new ParseError(e));
        }

        synchronized (this) {
            notifyAll();
        }

        return response;
    }

    public ErrorListener removeErrorListener(ErrorListener errorListener) {
        return callbackManager.removeErrorListener(errorListener);
    }

    public ResultListener<T> removeResultListener(ResultListener<T> resultListener) {
        return callbackManager.removeResultListener(resultListener);
    }

    public SessionListener removeSessionListener(SessionListener sessionListener) {
        return callbackManager.removeSessionListener(sessionListener);
    }

}
