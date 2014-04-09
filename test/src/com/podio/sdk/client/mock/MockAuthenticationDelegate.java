package com.podio.sdk.client.mock;

import com.podio.sdk.client.authentication.AuthenticationDelegate;

public class MockAuthenticationDelegate implements AuthenticationDelegate {
    public static final String TEST_TOKEN = "test_token";

    @Override
    public String getRefreshToken() {
        return TEST_TOKEN;
    }

    @Override
    public String getAuthToken() {
        return TEST_TOKEN;
    }

}
