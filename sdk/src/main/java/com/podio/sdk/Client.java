
package com.podio.sdk;

public interface Client {
    public static final int CLIENT_DEFAULT_TIMEOUT_MS = 30000;

    public Request<Void> authenticateWithUserCredentials(String username, String password);

    public Request<Void> authenticateWithAppCredentials(String appId, String appToken);

    public Request<Void> authenticateWithTransferToken(String transferToken);

    @Deprecated
    public Request<Void> forceRefreshTokens();

    public <T> Request<T> request(Request.Method method, Filter filter, Object requestData, Class<T> classOfExpectedResult);

}
