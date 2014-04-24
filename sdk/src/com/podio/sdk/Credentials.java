package com.podio.sdk;

import com.podio.sdk.internal.utils.Utils;

public final class Credentials {
    private String clientId;
    private String clientSecret;

    private String userId;
    private String userToken;
    private String userGrantType;

    private String authToken;
    private String refreshToken;
    private long expires;

    public Credentials(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public void forceExpired() {
        this.expires = System.currentTimeMillis();
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getUserGrantType() {
        return userGrantType;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserToken() {
        return userToken;
    }

    public boolean isAuthorized() {
        return Utils.notAnyEmpty(authToken, refreshToken) && expires > 0;
    }

    public void setTokens(String authToken, String refreshToken, long expiresIn) {
        this.authToken = authToken;
        this.refreshToken = refreshToken;
        this.expires = System.currentTimeMillis() + (expiresIn * 1000);
    }

    public void setUserCredentials(String userId, String userToken, String userGrantType) {
        this.userId = userId;
        this.userToken = userToken;
        this.userGrantType = userGrantType;
    }

    public boolean shouldRefreshTokens() {
        long timeLeft = expires - System.currentTimeMillis();

        // Recommend a refresh when there is 10 minutes or less left until the
        // auth token expires.
        return timeLeft < 600000;
    }
}
