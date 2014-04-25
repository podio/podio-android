package com.podio.sdk;

import org.json.JSONObject;

import com.podio.sdk.internal.utils.Utils;

public class Session {
    public final String accessToken;
    public final String refreshToken;
    public final long expiresInMillis;

    public Session(JSONObject httpResult) {
        if (httpResult != null) {
            this.accessToken = httpResult.optString("access_token", null);
            this.refreshToken = httpResult.optString("refresh_token", null);
            this.expiresInMillis = System.currentTimeMillis()
                    + (httpResult.optLong("expires_in", 0L) * 1000);
        } else {
            this.accessToken = null;
            this.refreshToken = null;
            this.expiresInMillis = 0L;
        }
    }

    public Session(String accessToken, String refreshToken, long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresInMillis = System.currentTimeMillis() + (expiresIn * 1000);
    }

    public boolean isAuthorized() {
        return Utils.notAnyEmpty(accessToken, refreshToken) && expiresInMillis > 0L;
    }

    public boolean shouldRefreshTokens() {
        long timeLeft = expiresInMillis - System.currentTimeMillis();

        // Recommend a refresh when there is 10 minutes or less left until the
        // auth token expires.
        return timeLeft < 600000;
    }
}
