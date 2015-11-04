/*
 * Copyright (C) 2015 Citrix Systems, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.podio.sdk.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache.Entry;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;
import com.podio.sdk.ApiError;
import com.podio.sdk.ConnectionError;
import com.podio.sdk.NetworkError;
import com.podio.sdk.NoResponseError;
import com.podio.sdk.PodioError;
import com.podio.sdk.Session;
import com.podio.sdk.internal.Utils;
import com.podio.sdk.json.JsonParser;

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

    static <E> VolleyRequest<E> newRequest(String userAgent, com.podio.sdk.Request.Method method, String url, String body, Class<E> classOfResult) {
        int volleyMethod = parseMethod(method);

        VolleyRequest<E> request = new VolleyRequest<E>(volleyMethod, url, classOfResult, false);
        request.contentType = "application/json; charset=UTF-8";
        if (userAgent != null) {
            request.headers.put("User-agent", userAgent);
        }
        request.headers.put("X-Time-Zone", Calendar.getInstance().getTimeZone().getID());
        request.body = Utils.notEmpty(body) ? body.getBytes() : null;

        return request;
    }

    static VolleyRequest<Void> newAuthRequest(String userAgent, String url, Map<String, String> params) {
        int volleyMethod = parseMethod(com.podio.sdk.Request.Method.POST);

        VolleyRequest<Void> request = new VolleyRequest<Void>(volleyMethod, url, null, true);
        if (userAgent != null) {
            request.headers.put("User-agent", userAgent);
        }
        request.headers.put("X-Time-Zone", Calendar.getInstance().getTimeZone().getID());
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

    private final Class<T> classOfResult;
    protected HashMap<String, String> headers;
    protected HashMap<String, String> params;
    protected String contentType;
    protected byte[] body;

    private T result;
    private PodioError error;
    private boolean isDone;
    private boolean isAuthRequest;
    private boolean hasSessionChanged;

    protected VolleyRequest(int method, String url, Class<T> resultType, boolean isAuthRequest) {
        super(method, url, null);
        setShouldCache(false);

        this.callbackManager = new VolleyCallbackManager<T>();
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
        return Utils.notEmpty(body) ? body : super.getBody();
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
    public synchronized T waitForResult(long maxSeconds) throws PodioError {
        // This is still awkward as we might end up blocking the delivery of
        // a client side error (see
        try {
            wait(TimeUnit.SECONDS.toMillis(Math.max(maxSeconds, 0)));
        } catch (InterruptedException e) {
            callbackManager.deliverError(e);
        }

        if (error != null) {
            throw error;
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

        isDone = true;

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

        if (volleyError instanceof NoConnectionError) {
            error = new ConnectionError(volleyError);
        } else if (volleyError instanceof TimeoutError) {
            error = new NoResponseError(volleyError);
        } else if (volleyError instanceof com.android.volley.NetworkError) {
            error = new NetworkError(volleyError);
        } else {
            String errorJson = getResponseBody(volleyError.networkResponse);
            int responseCode = getResponseCode(volleyError.networkResponse);

            if (Utils.notEmpty(errorJson) && responseCode > 0) {
                try {
                    error = new ApiError(errorJson, responseCode, volleyError);
                } catch (JsonSyntaxException jsonSyntaxException) {
                    handleNoneJsonError(volleyError, responseCode);
                }
            } else {
                handleNoneJsonError(volleyError, responseCode);
            }
        }

        synchronized (this) {
            notifyAll();
        }

        return volleyError;
    }

    private void handleNoneJsonError(VolleyError volleyError, int responseCode) {
        try {
            if (responseCode > 0) {
                error = new PodioError(volleyError, responseCode);
            } else {
                error = new PodioError(volleyError);
            }
        } catch (Exception e) {
            error = new PodioError("Unknown Error");
            error.setStackTrace(e.getStackTrace());
        }
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
        } catch (OutOfMemoryError e) {

            String dataSize = "\nnetworkResponse body size: " + (networkResponse.data == null ? "0" : networkResponse.data.length + "");
            String moreInfoOnCrash = e.getMessage() + "\nclassOfResult: " + classOfResult + "\nurl: " + getOriginUrl() + dataSize;
            OutOfMemoryError exceptionWithMoreInfo = new OutOfMemoryError(moreInfoOnCrash);
            exceptionWithMoreInfo.setStackTrace(e.getStackTrace());
            throw exceptionWithMoreInfo;
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

    private String getResponseBody(NetworkResponse networkResponse) {
        try {
            String charSet = HttpHeaderParser.parseCharset(networkResponse.headers);
            return new String(networkResponse.data, charSet);
        } catch (UnsupportedEncodingException e) {
            // The provided error JSON is provided with an unknown char-set.
            return null;
        } catch (NullPointerException e) {
            // For some reason the VolleyError didn't provide a networkResponse.
            return null;
        }
    }

    private int getResponseCode(NetworkResponse networkResponse) {
        return networkResponse != null ? networkResponse.statusCode : 0;
    }
}
