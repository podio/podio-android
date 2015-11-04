
package com.podio.sdk.volley;

import android.content.Context;
import android.net.Uri;

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
import com.podio.sdk.Request;
import com.podio.sdk.Session;
import com.podio.sdk.internal.Utils;
import com.podio.sdk.json.JsonParser;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

public class VolleyClient implements Client {

    static class AuthPath extends Filter {

        protected AuthPath() {
            super("oauth/token");
        }

        AuthPath withUserCredentials(String clientId, String clientSecret, String username, String password) {
            addQueryParameter("client_id", clientId);
            addQueryParameter("client_secret", clientSecret);
            addQueryParameter("grant_type", "password");
            addQueryParameter("username", username);
            addQueryParameter("password", password);
            return this;
        }

        AuthPath withAppCredentials(String clientId, String clientSecret, String appId, String appToken) {
            addQueryParameter("client_id", clientId);
            addQueryParameter("client_secret", clientSecret);
            addQueryParameter("grant_type", "app");
            addQueryParameter("app_id", appId);
            addQueryParameter("app_token", appToken);
            return this;
        }

        AuthPath withRefreshToken(String clientId, String clientSecret, String refreshToken) {
            addQueryParameter("client_id", clientId);
            addQueryParameter("client_secret", clientSecret);
            addQueryParameter("grant_type", "refresh_token");
            addQueryParameter("refresh_token", refreshToken);
            return this;
        }

        AuthPath withTransferToken(String clientId, String clientSecret, String transerToken) {
            addQueryParameter("client_id", clientId);
            addQueryParameter("client_secret", clientSecret);
            addQueryParameter("grant_type", "transfer_token");
            addQueryParameter("transfer_token", transerToken);
            return this;
        }

    }

    private final class VolleyRetryPolicy extends DefaultRetryPolicy {
        private final String originalAccessToken;

        private VolleyRetryPolicy(String referenceAccessToken) {
            super(CLIENT_DEFAULT_TIMEOUT_MS, 1, 1.0f);
            this.originalAccessToken = referenceAccessToken;
        }

        @Override
        public void retry(VolleyError error) throws VolleyError {
            super.retry(error);
            String accessToken = Utils.notEmpty(originalAccessToken) ? originalAccessToken : "";

            // If the access token has changed since this request was originally executed (say, as a
            // result of an other request refreshing it), the 401 status isn't necessarily valid any
            // more, hence, we should only re-authenticate if our access token is intact.
            if (error instanceof AuthFailureError && accessToken.equals(Session.accessToken())) {
                Uri uri = buildAuthUri();

                if (uri == null) {
                    // Opt out if we can't re-authenticate.
                    clearRequestQueue();
                    throw error;
                }

                String url = parseUrl(uri);
                HashMap<String, String> params = parseParams(uri);

                // Re-authenticate on a prioritized request queue.
                VolleyRequest<Void> reAuthRequest = VolleyRequest.newAuthRequest(userAgent, url, params);
                reAuthRequest.setRetryPolicy(new DefaultRetryPolicy(CLIENT_DEFAULT_TIMEOUT_MS, 0, 0));
                addToRefreshQueue(reAuthRequest);

                reAuthRequest.withErrorListener(new Request.ErrorListener() {
                    @Override
                    public boolean onErrorOccurred(Throwable cause) {
                        clearRequestQueue();
                        return false;
                    }
                }).waitForResult(TimeUnit.MILLISECONDS.toSeconds(CLIENT_DEFAULT_TIMEOUT_MS));
            }
        }
    }

    protected String clientId;
    protected String clientSecret;
    protected String scheme;
    protected String authority;
    protected String userAgent;

    // All implementations and instances will share these request queues.
    private static RequestQueue volleyRequestQueue;
    private static RequestQueue volleyRefreshQueue;

    @Override
    public Request<Void> authenticateWithUserCredentials(String username, String password) {
        return authenticate(new AuthPath()
                .withUserCredentials(clientId, clientSecret, username, password)
                .buildUri(scheme, authority));
    }

    @Override
    public Request<Void> authenticateWithAppCredentials(String appId, String appToken) {
        return authenticate(new AuthPath()
                .withAppCredentials(clientId, clientSecret, appId, appToken)
                .buildUri(scheme, authority));
    }

    @Override
    public Request<Void> authenticateWithTransferToken(String transferToken) {
        return authenticate(new AuthPath()
                .withTransferToken(clientId, clientSecret, transferToken)
                .buildUri(scheme, authority));
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
        VolleyRequest<Void> authRequest = VolleyRequest.newAuthRequest(userAgent, url, params);

        // Re-authenticate on a prioritized request queue.
        addToRefreshQueue(authRequest);

        return authRequest;
    }

    @Override
    public <T> Request<T> request(Request.Method method, Filter filter, Object item, Class<T> classOfResult) {
        String url = filter.buildUri(scheme, authority).toString();
        String body = item != null ? JsonParser.toJson(item) : null;

        VolleyRequest<T> request = VolleyRequest.newRequest(userAgent, method, url, body, classOfResult);
        request.setRetryPolicy(new VolleyRetryPolicy(Session.accessToken()));
        addToRequestQueue(request);

        return request;
    }

    public synchronized void setup(Context context, String scheme, String authority, String clientId, String clientSecret, String userAgent, SSLSocketFactory sslSocketFactory) {
        this.scheme = scheme;
        this.authority = authority;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.userAgent = userAgent;

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
        clearRequestQueue();
        clearRefreshQueue();

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

    protected synchronized void addToRefreshQueue(com.android.volley.Request<?> request) {
        if (request != null) {
            volleyRefreshQueue.add(request);
        }
    }

    protected synchronized void addToRequestQueue(com.android.volley.Request<?> request) {
        if (request != null) {
            volleyRequestQueue.add(request);
        }
    }

    protected synchronized Request<Void> authenticate(Uri uri) {
        String url = parseUrl(uri);
        HashMap<String, String> params = parseParams(uri);
        VolleyRequest<Void> request = VolleyRequest.newAuthRequest(userAgent, url, params);

        // It seems Volley takes the connection timeout from the assigned RetryPolicy (defaults to
        // 2.5 seconds). This particular RetryPolicy allows a 30 second connection timeout, zero
        // retries and no back-off multiplier for this authentication request.
        request.setRetryPolicy(new DefaultRetryPolicy(CLIENT_DEFAULT_TIMEOUT_MS, 0, 0));
        addToRefreshQueue(request);

        return request;
    }

    protected Uri buildAuthUri() {
        Uri result = null;
        String refreshToken = Session.refreshToken();

        if (Utils.notEmpty(refreshToken)) {
            result = new AuthPath()
                    .withRefreshToken(clientId, clientSecret, refreshToken)
                    .buildUri(scheme, authority);
        }

        return result;
    }

    protected synchronized void clearRefreshQueue() {
        volleyRefreshQueue.cancelAll(new RequestFilter() {
            @Override
            public boolean apply(com.android.volley.Request<?> request) {
                return true;
            }
        });
    }

    protected synchronized void clearRequestQueue() {
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
