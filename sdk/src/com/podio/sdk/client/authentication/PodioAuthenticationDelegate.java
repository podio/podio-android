package com.podio.sdk.client.authentication;

public class PodioAuthenticationDelegate implements AuthenticationDelegate {

    @Override
    public String getAuthToken() {
        return "podio";
    }

    @Override
    public String getRefreshToken() {
        return "podio";
    }

}
