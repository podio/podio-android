package com.podio.sdk.domain;

public class SessionFilter extends ItemFilter {

    public SessionFilter() {
        super("oauth/token");
    }

    public SessionFilter withClientCredentials(String clientId, String clientSecret) {
        addQueryParameter("client_id", clientId);
        addQueryParameter("client_secret", clientSecret);
        return this;
    }

    public SessionFilter withUserCredentials(String username, String password) {
        addQueryParameter("grant_type", "password");
        addQueryParameter("username", username);
        addQueryParameter("password", password);
        return this;
    }

    public SessionFilter withAppCredentials(String appId, String appToken) {
        addQueryParameter("grant_type", "app");
        addQueryParameter("app_id", appId);
        addQueryParameter("app_token", appToken);
        return this;
    }
}
