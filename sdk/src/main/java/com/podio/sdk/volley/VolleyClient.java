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

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue.RequestFilter;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.podio.sdk.Client;
import com.podio.sdk.Filter;
import com.podio.sdk.JsonParser;
import com.podio.sdk.Request;
import com.podio.sdk.Session;
import com.podio.sdk.internal.Utils;

import java.util.HashMap;
import java.util.Set;

import javax.net.ssl.SSLSocketFactory;

public class VolleyClient implements Client {

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

    private final class VolleyRetryPolicy extends DefaultRetryPolicy {
        private final VolleyRequest<?> originalRequest;

        private VolleyRetryPolicy(VolleyRequest<?> originalRequest) {
            super(0, 1, 0.0f);
            this.originalRequest = originalRequest;
        }

        @Override
        public void retry(VolleyError error) throws VolleyError {
            super.retry(error);

            if (error instanceof AuthFailureError) {
                // Prepare to re-authenticate.
                Uri uri = buildAuthUri();

                // Opt out if we can't re-authenticate.
                if (uri == null) {
                    clearRequestQueue();
                    throw error;
                }

                String url = parseUrl(uri);
                HashMap<String, String> params = parseParams(uri);

                // Re-authenticate on a prioritized request queue.
                final Object lock = new Object();
                synchronized (lock) {
                    VolleyRequest<Void> reAuthRequest = VolleyRequest.newAuthRequest(url, params)
                            .withSessionListener(new Request.SessionListener() {
                                @Override
                                public boolean onSessionChanged(String authToken, String refreshToken, long expires) {
                                    synchronized (lock) {
                                        lock.notify();
                                    }
                                    return false;
                                }
                            })
                            .withErrorListener(new Request.ErrorListener() {
                                @Override
                                public boolean onErrorOccured(Throwable cause) {
                                    synchronized (lock) {
                                        lock.notify();
                                    }
                                    return false;
                                }
                            });
                    volleyRefreshQueue.add(reAuthRequest);

                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Log.d("MYTAG", "Released");
            }
        }
    }

    protected String clientId;
    protected String clientSecret;
    protected String scheme;
    protected String authority;

    private static RequestQueue volleyRequestQueue;
    private static RequestQueue volleyRefreshQueue;

    @Override
    public Request<Void> authenticateWithUserCredentials(String username, String password) {
        Uri uri = new AuthPath()
                .withClientCredentials(clientId, clientSecret)
                .withUserCredentials(username, password)
                .buildUri(scheme, authority);

        String url = parseUrl(uri);
        HashMap<String, String> params = parseParams(uri);
        VolleyRequest<Void> request = VolleyRequest.newAuthRequest(url, params);
        volleyRefreshQueue.add(request);

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
        volleyRefreshQueue.add(request);

        return request;
    }

    @Override
    @Deprecated
    public Request<Void> forceRefreshTokens() {
        // Prepare to re-authenticate.
        Uri uri = buildAuthUri();

        // Opt out if we can't re-authenticate.
        if (uri == null) {
            clearRequestQueue();
            return null;
        }

        String url = parseUrl(uri);
        HashMap<String, String> params = parseParams(uri);
        VolleyRequest<Void> authRequest = VolleyRequest.newAuthRequest(url, params);

        // Re-authenticate on a prioritized request queue.
        volleyRefreshQueue.add(authRequest);

        return authRequest;
    }

    @Override
    public <T> Request<T> request(Request.Method method, Filter filter, Object item, Class<T> classOfItem) {
        // Prepare the request.

        String url = filter.buildUri(scheme, authority).toString();
        String body = item != null ? JsonParser.toJson(item) : null;
        VolleyRequest<T> request = VolleyRequest.newRequest(method, url, body, classOfItem);

        // Make us aware of any authentication errors.
        request.setRetryPolicy(new VolleyRetryPolicy(request));

        // Enqueue the request.
        volleyRequestQueue.add(request);

        return request;
    }

    public void setup(Context context, String scheme, String authority, String clientId, String clientSecret, SSLSocketFactory sslSocketFactory) {
        this.scheme = scheme;
        this.authority = authority;
        this.clientId = clientId;
        this.clientSecret = clientSecret;

        // Ensure the expected request queues exists.
        if (sslSocketFactory == null) {
            if (volleyRequestQueue == null) {
                volleyRequestQueue = Volley.newRequestQueue(context);
                volleyRequestQueue.start();
            }

            if (volleyRefreshQueue == null) {
                volleyRefreshQueue = Volley.newRequestQueue(context);
                volleyRefreshQueue.start();
            }
        } else if (volleyRequestQueue == null || volleyRefreshQueue == null) {
            HurlStack stack = new HurlStack(null, sslSocketFactory);

            if (volleyRequestQueue == null) {
                volleyRequestQueue = Volley.newRequestQueue(context, stack);
                volleyRequestQueue.start();
            }

            if (volleyRefreshQueue == null) {
                volleyRefreshQueue = Volley.newRequestQueue(context, stack);
                volleyRefreshQueue.start();
            }
        }

        // Clear out any and all queued requests.
        // TODO: It's a bit unclear what will happen to any processing requests.
        RequestFilter blindRequestFilter = new RequestFilter() {

            @Override
            public boolean apply(com.android.volley.Request<?> request) {
                return true;
            }

        };

        volleyRequestQueue.cancelAll(blindRequestFilter);
        volleyRefreshQueue.cancelAll(blindRequestFilter);

        // Clear out any cached content in the request queues.
        Cache requestCache = volleyRequestQueue.getCache();
        if (requestCache != null) {
            requestCache.clear();
        }

        Cache priorityCache = volleyRefreshQueue.getCache();
        if (priorityCache != null) {
            priorityCache.clear();
        }
    }

    protected void addToRequestQueue(com.android.volley.Request<?> request) {
        if (request != null) {
            volleyRequestQueue.add(request);
        }
    }

    protected Uri buildAuthUri() {
        Uri result = null;
        String refreshToken = Session.refreshToken();

        if (Utils.notEmpty(refreshToken)) {
            result = new AuthPath()
                    .withClientCredentials(clientId, clientSecret)
                    .withRefreshToken(refreshToken)
                    .buildUri(scheme, authority);
        }

        return result;
    }

    protected void clearRequestQueue() {
        volleyRequestQueue.cancelAll(new RequestFilter() {

            @Override
            public boolean apply(com.android.volley.Request<?> request) {
                return true;
            }

        });
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
