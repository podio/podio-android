package com.podio.sdk;

import org.json.JSONException;
import org.json.JSONObject;

import com.podio.sdk.internal.utils.Utils;

public class Session {
    public final String accessToken;
    public final String refreshToken;
    public final long expiresMillis;

    public Session(String jsonString) {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            jsonObject = null;
        } catch (NullPointerException e) {
            jsonObject = null;
        }

        if (jsonObject != null) {
            this.accessToken = jsonObject.optString("access_token", null);
            this.refreshToken = jsonObject.optString("refresh_token", null);
            this.expiresMillis = System.currentTimeMillis()
                    + (jsonObject.optLong("expires_in", 0L) * 1000);
        } else {
            this.accessToken = null;
            this.refreshToken = null;
            this.expiresMillis = 0L;
        }
    }

    public Session(String accessToken, String refreshToken, long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresMillis = System.currentTimeMillis() + (expiresIn * 1000);
    }

    public boolean isAuthorized() {
        return Utils.notAnyEmpty(accessToken, refreshToken) && expiresMillis > 0L;
    }

    public boolean shouldRefreshTokens() {
        long timeLeft = expiresMillis - System.currentTimeMillis();

        // Recommend a refresh when there is 10 minutes or less left until the
        // auth token expires.
        return timeLeft < 600000;
    }

    @Override
    public boolean equals(Object o) {
        boolean isEqual = o instanceof Session;

        if (isEqual) {
            Session other = (Session) o;
            isEqual = accessToken.equals(other.accessToken) //
                    && refreshToken.equals(other.refreshToken) //
                    && expiresMillis == other.expiresMillis;
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        int hashCode = 0;

        hashCode += accessToken != null ? accessToken.hashCode() : 0;
        hashCode += refreshToken != null ? refreshToken.hashCode() : 0;
        hashCode += Long.valueOf(expiresMillis).hashCode();

        return hashCode;
    }
}
