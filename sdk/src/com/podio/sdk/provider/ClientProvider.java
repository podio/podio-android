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

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;

/**
 * A Provider implementation that offers means of authenticating with the Podio
 * API. This provider doesn't provide any content, but rather the login service.
 * 
 * @author László Urszuly
 */
public class ClientProvider extends Provider {

    /**
     * Enables means of user authentication, where the caller authenticates with
     * a user name and a password.
     * 
     * @param username
     *        The user name to log in with.
     * @param password
     *        The corresponding password.
     * @return A request future, enabling the caller to hook in optional
     *         callback implementations.
     */
    public Request<Void> authenticateWithUserCredentials(String username, String password) {
        return client.authenticateWithUserCredentials(username, password);
    }

    /**
     * Enables means of user authentication, where the caller authenticates with
     * a Podio app id and app token.
     * 
     * @param appId
     *        The app id to log in with.
     * @param appToken
     *        The corresponding app token.
     * @return A request future, enabling the caller to hook in optional
     *         callback implementations.
     */
    public Request<Void> authenticateWithAppCredentials(String appId, String appToken) {
        return client.authenticateWithAppCredentials(appId, appToken);
    }

    @Override
    protected <T> Request<T> delete(Filter filter) {
        throw new UnsupportedOperationException("This implementation can't manipulate content.");
    }

    /**
     * Enables means of forcing a refresh of auth tokens. *DEPRECATED* This call
     * may interfere with the internal execution flow of the SDK and should be
     * avoided at all cost.
     */
    @Deprecated
    public Request<Void> forceRefreshTokens() {
        return client.forceRefreshTokens();
    }

    @Override
    protected <T> Request<T> get(Filter filter, Class<T> classOfResult) {
        throw new UnsupportedOperationException("This implementation can't manipulate content.");
    }

    @Override
    protected <T> Request<T> post(Filter filter, Object item, Class<T> classOfItem) {
        throw new UnsupportedOperationException("This implementation can't manipulate content.");
    }

    @Override
    protected <T> Request<T> put(Filter filter, Object item, Class<T> classOfItem) {
        throw new UnsupportedOperationException("This implementation can't manipulate content.");
    }

}
