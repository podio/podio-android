
package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;

/**
 * A Provider implementation that offers means of authenticating with the Podio API. This provider
 * doesn't provide any content, but rather the login service.
 *
 */
public class ClientProvider extends Provider {

    /**
     * Enables means of user authentication, where the caller authenticates with a user name and a
     * password.
     *
     * @param username
     *         The user name to log in with.
     * @param password
     *         The corresponding password.
     *
     * @return A request future, enabling the caller to hook in optional callback implementations.
     */
    public Request<Void> authenticateWithUserCredentials(String username, String password) {
        return client.authenticateWithUserCredentials(username, password);
    }

    /**
     * Enables means of user authentication, where the caller authenticates with a Podio app id and
     * app token.
     *
     * @param appId
     *         The app id to log in with.
     * @param appToken
     *         The corresponding app token.
     *
     * @return A request future, enabling the caller to hook in optional callback implementations.
     */
    public Request<Void> authenticateWithAppCredentials(String appId, String appToken) {
        return client.authenticateWithAppCredentials(appId, appToken);
    }

    /**
     * Enables means of user authentication, where the caller authenticates with a previously issued
     * transfer token.
     *
     * @param transferToken
     *         The transfer token to log in with.
     *
     * @return A request future, enabling the caller to hook in optional callback implementations.
     */
    public Request<Void> authenticateWithTransferToken(String transferToken) {
        return client.authenticateWithTransferToken(transferToken);
    }

    @Override
    protected <T> Request<T> delete(Filter filter) {
        throw new UnsupportedOperationException("This implementation can't manipulate content.");
    }

    /**
     * Enables means of forcing a refresh of auth tokens. *DEPRECATED* This call may interfere with
     * the internal execution flow of the SDK and should be avoided at all cost.
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
