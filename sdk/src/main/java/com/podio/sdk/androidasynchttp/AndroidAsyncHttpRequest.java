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
package com.podio.sdk.androidasynchttp;

import android.content.Context;

import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.podio.sdk.ApiError;
import com.podio.sdk.ConnectionError;
import com.podio.sdk.NoResponseError;
import com.podio.sdk.Podio;
import com.podio.sdk.PodioError;
import com.podio.sdk.Request;
import com.podio.sdk.Session;
import com.podio.sdk.internal.CallbackManager;
import com.podio.sdk.json.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * @author Tobias Lindberg
 */
public class AndroidAsyncHttpRequest<T> implements Request<T>, Request.SessionListener {
    private long TEN_MINUTES = 600000;
    private static final String CONTENT_TYPE = "application/json; charset=UTF-8";

    private AsyncHttpClient client;
    private Context context;
    private String url;
    private File file;
    private final Class<T> classOfResult;
    private CallbackManager<T> callbackManager;

    private T result;
    private boolean isDone;
    private PodioError error;

    @Override
    public T waitForResult(long maxSeconds) throws PodioError {
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
    public Request<T> withResultListener(ResultListener<T> contentListener) {
        callbackManager.addResultListener(contentListener, isDone, result);
        return this;
    }

    @Override
    public Request<T> withErrorListener(ErrorListener errorListener) {
        callbackManager.addErrorListener(errorListener, isDone && error != null, error);
        return null;
    }

    @Override
    public Request<T> withSessionListener(SessionListener sessionListener) {
        throw new UnsupportedOperationException("AndroidAsyncHttp does not support SessionListeners");
    }

    protected AndroidAsyncHttpRequest(AsyncHttpClient client, Context context, String url, File file, Class<T> resultType) {
        this.client = client;
        this.context = context;
        this.url = url;
        this.file = file;
        this.classOfResult = resultType;
        this.callbackManager = new CallbackManager();
        if (isSessionAboutToExpire()) {
            Podio.client.forceRefreshTokens().withSessionListener(this);
        } else {
            performRequest(client, context, url, file);
        }
    }

    private boolean isSessionAboutToExpire() {
        return System.currentTimeMillis() > (Session.expires() - TEN_MINUTES);
    }

    @Override
    public boolean onSessionChanged(String authToken, String refreshToken, String transferToken, long expires) {
        performRequest(client, context, url, file);

        return false;
    }

    public void performRequest(AsyncHttpClient client, Context context, String url, File file) {
        try {
            RequestParams params = new RequestParams();
            params.put("source", file);
            params.put("filename", file.getName());
            client.post(context, url, getHeaders(), params, CONTENT_TYPE, new BaseJsonHttpResponseHandler<T>() {

                @Override
                protected T parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                    T result;
                    if (isFailure || classOfResult == null || classOfResult == Void.class) {
                        result = null;
                    } else {
                        result = JsonParser.fromJson(rawJsonData, classOfResult);
                    }
                    return result;
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, T response) {
                    result = response;
                    deliverResponse();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, T errorResponse) {
                    if (throwable instanceof ConnectTimeoutException || throwable instanceof SocketTimeoutException) {
                        error = new NoResponseError(throwable);
                    } else if (rawJsonData != null) {
                        try {
                            error = new ApiError(rawJsonData, statusCode, throwable);
                        } catch (JsonSyntaxException jsonSyntaxException) {
                            error = new PodioError(throwable);
                        }
                    } else {
                        error = new ConnectionError(throwable);
                    }
                    deliverError();
                }
            });
        } catch (FileNotFoundException e) {
            error = new PodioError(e);
        }
    }

    private void deliverError() {
        isDone = true;
        callbackManager.deliverError(this.error);
    }

    private void deliverResponse() {
        isDone = true;
        callbackManager.deliverResult(result);
    }

    public Header[] getHeaders() {
        Header[] headers = new Header[2];
        String accessToken = Session.accessToken();
        BasicHeader header = new BasicHeader("Authorization", "Bearer " + accessToken);
        headers[0] = header;
        header = new BasicHeader("X-Time-Zone", Calendar.getInstance().getTimeZone().getID());
        headers[1] = header;

        return headers;
    }
}
