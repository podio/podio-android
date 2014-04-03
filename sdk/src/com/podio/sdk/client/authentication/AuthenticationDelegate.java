package com.podio.sdk.client.authentication;

public interface AuthenticationDelegate {

    public String getAuthToken();

    public String getRefreshToken();
}
