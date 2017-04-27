
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
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 */
public class AndroidAsyncHttpRequest<T> implements Request<T>, Request.SessionListener {
    private long TEN_MINUTES = 600000;

    private AsyncHttpClient client;
    private Context context;
    private String url;
    private File file;
    private final Class<T> classOfResult;
    private CallbackManager<T> callbackManager;

    private T result;
    private boolean isDone;
    private PodioError error;
    private boolean attemptedReauth;

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
        this.attemptedReauth = false;
    }

    private boolean isSessionAboutToExpire() {
        return System.currentTimeMillis() > (Session.expires()*1000L - TEN_MINUTES);
    }

    @Override
    public boolean onSessionChanged() {
        runRequest();

        return false;
    }

    public void performRequest() {
        if (isSessionAboutToExpire() && !attemptedReauth) {
            attemptedReauth = true;
            Podio.client.forceRefreshTokens().withSessionListener(this);
        } else {
            runRequest();
        }
    }

    private void runRequest() {
        try {
            RequestParams params = new RequestParams();
            params.put("source", file);
            params.put("filename", file.getName());
            client.post(context, url, getHeaders(), params, null
                    , new BaseJsonHttpResponseHandler<T>() {

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
                    if (statusCode == HttpStatus.SC_UNAUTHORIZED && !attemptedReauth) {
                        attemptedReauth = true;
                        Podio.client.forceRefreshTokens().withSessionListener(AndroidAsyncHttpRequest.this);
                    } else {
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
                }
            });
        } catch (FileNotFoundException e) {
            error = new PodioError(e);
            deliverError();
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
        return Session.getAuthorizationHeaders();
    }
}
