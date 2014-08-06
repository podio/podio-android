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

package com.podio.sdk.provider;

import java.util.concurrent.Future;

import com.podio.sdk.PodioFilter;
import com.podio.sdk.RestClient;
import com.podio.sdk.ResultListener;
import com.podio.sdk.client.RequestFuture;
import com.podio.sdk.client.RestResult;
import com.podio.sdk.domain.Session;
import com.podio.sdk.filter.SessionFilter;
import com.podio.sdk.parser.JsonParser;

public class SessionProvider extends BasicPodioProvider {

    /**
     * Authenticates the caller with the given user credentials. On success a
     * new session object with the access and refresh tokens will be delivered
     * through the given {@link ResultListener}.
     * 
     * @param username
     *        The user name of the Podio account to authenticate with.
     * @param password
     *        The corresponding password of the Podio account.
     * @return A ticket which the caller can use to identify this request with.
     */
    public RequestFuture<Session> authenticateWithUserCredentials(String username, String password) {
        PodioFilter filter = new SessionFilter()
                .withUserCredentials(username, password);

        return request(RestClient.Operation.POST, filter, null, JsonParser.fromClass(Session.class));
    }

    /**
     * Authenticates the caller with the given app credentials. On success a new
     * session object with the access and refresh tokens will be delivered
     * through the given {@link ResultListener}.
     * 
     * @param appId
     *        The id of the app to authenticate with.
     * @param appToken
     *        The token that has been generated for a particular app.
     * @return A ticket which the caller can use to identify this request with.
     */
    public RequestFuture<Session> authenticateWithAppCredentials(String appId, String appToken) {
        PodioFilter filter = new SessionFilter()
                .withAppCredentials(appId, appToken);

        return request(RestClient.Operation.POST, filter, null, JsonParser.fromClass(Session.class));
    }

    /**
     * Forces the Podio SDK to refresh its access token based on an external
     * refresh token. If the external refresh token is empty, then the internal
     * token will be used instead.
     * <p>
     * <strong>YOU SHOULD NORMALLY NEVER EVER CALL THIS METHOD</strong> as the
     * SDK manages its own session, giving you "read-only" access to it. This
     * method is here only for legacy apps to use and is a target for removal at
     * any time without any prior notification.
     * 
     * @param refreshToken
     *        An optional refresh token the caller wants to use.
     * @return A ticket which the caller can use to identify this request with.
     */
    @Deprecated
    public Future<RestResult<Session>> forceRefreshAccessToken(String refreshToken) {
        PodioFilter filter = new SessionFilter()
                .withRefreshToken(refreshToken);

        return request(RestClient.Operation.POST, filter, null, JsonParser.fromClass(Session.class));
    }
}
