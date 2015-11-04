
package com.podio.sdk.androidasynchttp;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.podio.sdk.Client;
import com.podio.sdk.Filter;
import com.podio.sdk.Request;
import com.podio.sdk.provider.FileProvider;

import java.io.File;

import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;

/**
 * @author Tobias Lindberg
 */
public class AndroidAsyncHttpClient implements Client {

    protected String scheme;
    protected String authority;

    private AsyncHttpClient client;
    private Context context;

    @Override
    public Request<Void> authenticateWithUserCredentials(String username, String password) {
        throw new UnsupportedOperationException("AndroidAsyncHttpClient only supports uploading to our Files API.");
    }

    @Override
    public Request<Void> authenticateWithAppCredentials(String appId, String appToken) {
        throw new UnsupportedOperationException("AndroidAsyncHttpClient only supports uploading to our Files API.");
    }

    @Override
    public Request<Void> authenticateWithTransferToken(String transferToken) {
        throw new UnsupportedOperationException("AndroidAsyncHttpClient only supports uploading to our Files API.");
    }

    @Override
    @Deprecated
    public Request<Void> forceRefreshTokens() {
        throw new UnsupportedOperationException("AndroidAsyncHttpClient only supports uploading to our Files API.");
    }

    @Override
    public <T> Request<T> request(Request.Method method, Filter filter, Object item, Class<T> classOfResult) {
        if (filter instanceof FileProvider.FileFilter && method == Request.Method.POST && item instanceof File) {
            String url = filter.buildUri(scheme, authority).toString();
            AndroidAsyncHttpRequest<T> request = new AndroidAsyncHttpRequest<>(client, context, url, (File) item, classOfResult);
            request.performRequest();

            return request;
        } else {
            throw new UnsupportedOperationException("AndroidAsyncHttpClient only supports uploading to our Files API.");
        }
    }

    public synchronized void setup(Context context, String scheme, String authority, String userAgent, SSLSocketFactory sslSocketFactory) {
        this.context = context;
        this.scheme = scheme;
        this.authority = authority;

        client = new AsyncHttpClient();
        client.setTimeout(CLIENT_DEFAULT_TIMEOUT_MS);
        if (userAgent != null) {
            client.setUserAgent(userAgent);
        }
        if (sslSocketFactory != null) {
            client.setSSLSocketFactory(sslSocketFactory);
        }
    }

}
