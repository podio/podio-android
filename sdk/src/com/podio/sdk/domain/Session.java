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

package com.podio.sdk.domain;

import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.podio.sdk.internal.Utils;

public class Session {
    public final String accessToken;
    public final String refreshToken;
    public final long expiresIn;
    public final long expires;

    public Session(String jsonString) {
        JSONObject jsonObject = null;
        String accessToken = null;
        String refreshToken = null;
        long expiresIn = 0;
        long expires = 0;

        try {
            jsonObject = new JSONObject(jsonString);
            accessToken = jsonObject.optString("access_token", null);
            refreshToken = jsonObject.optString("refresh_token", null);

            if (jsonObject.has("expires")) {
                expires = jsonObject.optLong("expires", 0L);
                expiresIn = expires - currentTimeSeconds();
            } else if (jsonObject.has("expires_in")) {
                expiresIn = jsonObject.optLong("expires_in", 0L);
                expires = currentTimeSeconds() + expiresIn;
            }
        } catch (JSONException e) {
            // Input JSON was most likely invalid. Fallback to defaults.
            accessToken = refreshToken = null;
            expires = expiresIn = 0;
        } catch (NullPointerException e) {
            // Input JSON was most likely a null pointer. Fallback to defaults.
            accessToken = refreshToken = null;
            expires = expiresIn = 0;
        }

        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.expires = expires;
    }

    public Session(String accessToken, String refreshToken, long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.expires = currentTimeSeconds() + this.expiresIn;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (getClass() != o.getClass()) {
            return false;
        }

        Session other = (Session) o;
        if (accessToken == null) {
            if (other.accessToken != null) {
                return false;
            }
        } else if (!accessToken.equals(other.accessToken)) {
            return false;
        }

        if (refreshToken == null) {
            if (other.refreshToken != null) {
                return false;
            }
        } else if (!refreshToken.equals(other.refreshToken)) {
            return false;
        }

        if (expires != other.expires) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 43;
        int result = 1;

        result = prime * result + (accessToken == null ? 0 : accessToken.hashCode());
        result = prime * result + (refreshToken == null ? 0 : refreshToken.hashCode());
        result = prime * result + Long.valueOf(expires).hashCode();

        return result;
    }

    public boolean isAuthorized() {
        return Utils.notAnyEmpty(accessToken, refreshToken) && expires > 0L;
    }

    public boolean shouldRefreshTokens() {
        // Recommend a refresh when there is less than 10 minutes left until the
        // auth token expires.
        long timeLeft = expires - currentTimeSeconds();
        return timeLeft < 600L;
    }

    public String toJson() {
        String result;

        try {
            result = new JSONObject()
                    .put("access_token", accessToken)
                    .put("refresh_token", refreshToken)
                    .put("expires", expires)
                    .toString();
        } catch (JSONException e) {
            result = null;
        }

        return result;
    }

    private long currentTimeSeconds() {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    }
}
