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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

public class VolleyRequest<T> extends Request<T> implements com.podio.sdk.Request<T> {

    private static final ArrayList<ErrorListener> GLOBAL_ERROR_LISTENERS = new ArrayList<ErrorListener>();
    private static final ArrayList<SessionListener> GLOBAL_SESSION_LISTENERS = new ArrayList<SessionListener>();

    public static ErrorListener addGlobalErrorListener(ErrorListener errorListener) {
        return errorListener != null && GLOBAL_ERROR_LISTENERS.add(errorListener) ?
                errorListener :
                null;
    }

    public static SessionListener addGlobalSessionListener(SessionListener sessionListener) {
        return sessionListener != null && GLOBAL_SESSION_LISTENERS.add(sessionListener) ?
                sessionListener :
                null;
    }

    public static ErrorListener removeGlobalErrorListener(ErrorListener errorListener) {
        int index = GLOBAL_ERROR_LISTENERS.indexOf(errorListener);

        return GLOBAL_ERROR_LISTENERS.contains(errorListener) ?
                GLOBAL_ERROR_LISTENERS.remove(index) :
                null;
    }

    public static SessionListener removeGlobalSessionListener(SessionListener sessionListener) {
        int index = GLOBAL_SESSION_LISTENERS.indexOf(sessionListener);

        return GLOBAL_SESSION_LISTENERS.contains(sessionListener) ?
                GLOBAL_SESSION_LISTENERS.remove(index) :
                null;
    }

    static <E> VolleyRequest<E> newRequest(com.podio.sdk.Request.Method method, String url, String body, Class<E> classOfResult) {
        RequestFuture<E> volleyRequestFuture = RequestFuture.newFuture();
        int volleyMethod = parseMethod(method);

        VolleyRequest<E> request = new VolleyRequest<E>(volleyMethod, url, classOfResult, volleyRequestFuture, false);
        request.contentType = "application/json; charset=UTF-8";

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

    private final ArrayList<AuthErrorListener<T>> authErrorListeners;
    private final ArrayList<ResultListener<T>> resultListeners;
    private final ArrayList<SessionListener> sessionListeners;
    private final ArrayList<ErrorListener> errorListeners;

    private final RequestFuture<T> volleyRequestFuture;
    private final Class<T> classOfResult;

    protected HashMap<String, String> headers;
    protected HashMap<String, String> params;
    protected String contentType;
    protected String body;

    private T result;
    private Throwable error;
    private boolean isAuthRequest;
    private boolean hasSessionChanged;

    protected VolleyRequest(int method, String url, Class<T> resultType, RequestFuture<T> volleyRequestFuture, boolean isAuthRequest) {
        super(method, url, volleyRequestFuture);

        this.authErrorListeners = new ArrayList<AuthErrorListener<T>>();
        this.resultListeners = new ArrayList<ResultListener<T>>();
        this.sessionListeners = new ArrayList<SessionListener>();
        this.errorListeners = new ArrayList<ErrorListener>();

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
    public boolean cancel(boolean mayInterruptIfRunning) {
        return volleyRequestFuture.cancel(mayInterruptIfRunning);
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return volleyRequestFuture.get();
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return volleyRequestFuture.get(timeout, unit);
    }

    @Override
    public boolean isCancelled() {
        return volleyRequestFuture.isCancelled();
    }

    @Override
    public boolean isDone() {
        return volleyRequestFuture.isDone();
    }

    @Override
    public VolleyRequest<T> withResultListener(ResultListener<T> resultListener) {
        if (resultListener != null) {
            resultListeners.add(resultListener);

            if (isDone() && result != null) {
                resultListener.onRequestPerformed(result);
            }
        }

        return this;
    }

    @Override
    public VolleyRequest<T> withErrorListener(ErrorListener errorListener) {
        if (errorListener != null) {
            errorListeners.add(errorListener);

            if (isDone() && error != null) {
                errorListener.onErrorOccured(error);
            }
        }

        return this;
    }

    @Override
    public VolleyRequest<T> withSessionListener(SessionListener sessionListener) {
        if (sessionListener != null) {
            sessionListeners.add(sessionListener);

            if (isDone() && hasSessionChanged) {
                sessionListener.onSessionChanged(Session.accessToken(), Session.refreshToken(), Session.expires());
            }
        }

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
        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    @Override
    public void deliverError(VolleyError error) {
        // This method is executed on the main thread. Extra care should be
        // taken on what is done here.

        if (isAuthError(this.error)) {
            for (AuthErrorListener<T> authErrorListener : authErrorListeners) {
                if (authErrorListener.onAuthErrorOccured(this)) {
                    // The callback consumed the event, stop the bubbling.
                    return;
                }
            }
        }

        for (ErrorListener errorListener : errorListeners) {
            if (errorListener.onErrorOccured(this.error)) {
                // The callback consumed the event, stop the bubbling.
                return;
            }
        }

        for (ErrorListener errorListener : GLOBAL_ERROR_LISTENERS) {
            if (errorListener.onErrorOccured(this.error)) {
                // The callback consumed the event, stop the bubbling.
                return;
            }
        }

    }

    @Override
    protected void deliverResponse(T result) {
        // This method is executed on the main thread. Extra care should be
        // taken on what is done here.

        this.result = result;

        if (hasSessionChanged) {
            deliverSession();
        }

        for (ResultListener<T> resultListener : resultListeners) {
            if (resultListener.onRequestPerformed(result)) {
                // The callback consumed the event, stop the bubbling.
                break;
            }
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        // This method is executed on the worker thread. It's "safe" to perform
        // JSON parsing here.

        try {
            String charSet = HttpHeaderParser.parseCharset(volleyError.networkResponse.headers);
            String errorJson = new String(volleyError.networkResponse.data, charSet);
            error = PodioError.fromJson(errorJson, volleyError.networkResponse.statusCode, error);
        } catch (UnsupportedEncodingException e) {
            // The provided error JSON is provided with an unknown char-set.
            error = volleyError;
        } catch (NullPointerException e) {
            // For some reason the VollyError didn't provide a networkResponse.
            error = volleyError;
        }

        return super.parseNetworkError(volleyError);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        // This method is executed on the worker thread. It's "safe" to perform
        // JSON parsing here.

        try {
            Entry cacheHeaders = HttpHeaderParser.parseCacheHeaders(response);
            String charSet = HttpHeaderParser.parseCharset(response.headers);
            String json = new String(response.data, charSet);

            if (isAuthRequest) {
                Session.set(json);
                hasSessionChanged = true;
                return Response.success(null, cacheHeaders);
            } else if (classOfResult == null || classOfResult == Void.class) {
                return Response.success(null, cacheHeaders);
            } else {
                T result = JsonParser.fromJson(json, classOfResult);
                return Response.success(result, cacheHeaders);
            }
        } catch (UnsupportedEncodingException e) {
            // The provided response JSON is provided with an unknown char-set.
            return Response.error(new ParseError(e));
        }
    }

    public ResultListener<T> removeResultListener(ResultListener<T> resultListener) {
        int index = resultListeners.indexOf(resultListener);

        return resultListeners.contains(resultListener) ?
                resultListeners.remove(index) :
                null;
    }

    public ErrorListener removeErrorListener(ErrorListener errorListener) {
        int index = errorListeners.indexOf(errorListener);

        return errorListeners.contains(errorListener) ?
                errorListeners.remove(index) :
                null;
    }

    public SessionListener removeSessionListener(SessionListener sessionListener) {
        int index = sessionListeners.indexOf(sessionListener);

        return sessionListeners.contains(sessionListener) ?
                sessionListeners.remove(index) :
                null;
    }

    VolleyRequest<T> withAuthErrorListener(AuthErrorListener<T> authErrorListener) {
        if (authErrorListener != null) {
            authErrorListeners.add(authErrorListener);

            if (isDone() && isAuthError(error)) {
                authErrorListener.onAuthErrorOccured(this);
            }
        }

        return this;
    }

    private void deliverSession() {
        // This method is executed on the main thread. Extra care should be
        // taken on what is done here.
        String accessToken = Session.accessToken();
        String refreshToken = Session.refreshToken();
        long expires = Session.expires();

        for (SessionListener sessionListener : sessionListeners) {
            if (sessionListener.onSessionChanged(accessToken, refreshToken, expires)) {
                // The callback consumed the event, stop the bubbling.
                return;
            }
        }

        for (SessionListener sessionListener : GLOBAL_SESSION_LISTENERS) {
            if (sessionListener.onSessionChanged(accessToken, refreshToken, expires)) {
                // The callback consumed the event, stop the bubbling.
                return;
            }
        }
    }

    private boolean isAuthError(Throwable error) {
        if (error instanceof PodioError) {
            PodioError e = (PodioError) error;
            return e.isExpiredError();
        } else if (error instanceof VolleyError) {
            VolleyError e = (VolleyError) error;
            return e.networkResponse != null && e.networkResponse.statusCode == 401;
        } else {
            return false;
        }
    }
}
