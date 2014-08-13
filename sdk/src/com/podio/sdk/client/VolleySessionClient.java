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

import android.content.Context;
import android.net.Uri;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.podio.sdk.RestClient;
import com.podio.sdk.SessionManager;
import com.podio.sdk.domain.Session;
import com.podio.sdk.filter.SessionFilter;
import com.podio.sdk.internal.Utils;

/**
 * @author László Urszuly
 */
public class VolleySessionClient extends QueuedRestClient implements SessionManager {
    private static final String SCHEME = "https";

    private RequestQueue requestQueue;
    private Session session;
    private String clientId;
    private String clientSecret;

    /**
     * @param scheme
     * @param authority
     */
    public VolleySessionClient(Context context, String authority) {
        super(SCHEME, authority, 1);
        this.requestQueue = Volley.newRequestQueue(context);
        clientId = clientSecret = null;
    }

    @Override
    public Session checkSession() {
        if (session == null) {
            throw new IllegalStateException("No session is active");
        }

        if (!session.isAuthorized()) {
            throw new IllegalStateException("Session is not authorized");
        }

        if (session.shouldRefreshTokens()) {
            refreshSession();
            return session;
        }

        return null;
    }

    @Override
    public String getAccessToken() {
        return session != null ? session.accessToken : null;
    }

    @Override
    public void refreshSession() {
        Uri uri = new SessionFilter()
                .withClientCredentials(clientId, clientSecret)
                .withRefreshToken(session.refreshToken)
                .buildUri(getScheme(), getAuthority());

        VolleyRequest request = VolleyRequest.newRefreshRequest(uri);
        requestQueue.add(request);

        String output = request.waitForResult();
        setSession(new Session(output));
    }

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> RestResult<T> handleRequest(RestRequest<T> restRequest) {
        RestClient.Operation operation = restRequest.getOperation();

        switch (operation) {
        case POST:
            String grantType = extractGrantTypeFromRequest(restRequest);
            String preferredToken = extractRefreshTokenFromRequest(restRequest);
            VolleyRequest request;

            if ("refresh_token".equals(grantType)) {
                String token = Utils.isEmpty(preferredToken) ? session.refreshToken : preferredToken;
                Uri uri = buildRequestUri(restRequest, token);
                request = VolleyRequest.newRefreshRequest(uri);
            } else {
                Uri uri = buildRequestUri(restRequest, null);
                request = VolleyRequest.newAuthRequest(uri);
            }

            requestQueue.add(request);

            String resultJson = request.waitForResult();
            setSession(new Session(resultJson));
            return (RestResult<T>) RestResult.success(session);

        default:
            throw new UnsupportedOperationException("Unsupported operation: " + operation.name());
        }
    }

    /**
     * Initializes the provider with the given client id and secret.
     * 
     * @param clientId
     *        The user client id.
     * @param clientSecret
     *        The user client secret.
     */
    public void setup(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    RestResult<Session> executeVolleyRequest(VolleyRequest request) {
        requestQueue.add(request);
        String resultJson = request.waitForResult();
        Session session = new Session(resultJson);
        setSession(session);

        return RestResult.success(session);
    }

    Uri buildRequestUri(RestRequest<?> restRequest, String refreshToken) {
        SessionFilter filter = ((SessionFilter) restRequest.getFilter()).withClientCredentials(
                clientId, clientSecret);

        String scheme = getScheme();
        String authority = getAuthority();

        return Utils.isEmpty(refreshToken) ? filter.buildUri(scheme, authority) : filter
                .withRefreshToken(refreshToken).buildUri(scheme, authority);
    }

    String extractGrantTypeFromRequest(RestRequest<?> restRequest) {
        SessionFilter filter = (SessionFilter) restRequest.getFilter();
        Uri dummyUri = filter.buildUri("null", "null");
        String grantType = dummyUri.getQueryParameter("grant_type");

        return grantType;
    }

    String extractRefreshTokenFromRequest(RestRequest<?> restRequest) {
        SessionFilter filter = (SessionFilter) restRequest.getFilter();
        Uri dummyUri = filter.buildUri("null", "null");
        String refreshToken = dummyUri.getQueryParameter("refresh_token");

        return refreshToken;
    }

}
