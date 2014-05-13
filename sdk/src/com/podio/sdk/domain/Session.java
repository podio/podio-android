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

import org.json.JSONException;
import org.json.JSONObject;

import com.podio.sdk.internal.utils.Utils;

public final class Session {
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

            if (jsonObject.has("expires")) {
                this.expiresMillis = jsonObject.optLong("expires", 0L);
            } else if (jsonObject.has("expires_in")) {
                this.expiresMillis = System.currentTimeMillis()
                        + jsonObject.optLong("expires_in", 0L) * 1000;
            } else {
                this.expiresMillis = 0L;
            }
        } else {
            this.accessToken = null;
            this.refreshToken = null;
            this.expiresMillis = 0L;
        }
    }

    public Session(String accessToken, String refreshToken, long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresMillis = System.currentTimeMillis() + expiresIn * 1000;
    }

    public boolean isAuthorized() {
        return Utils.notAnyEmpty(accessToken, refreshToken) && expiresMillis > 0L;
    }

    public boolean notAuthorized() {
        return !isAuthorized();
    }

    public boolean shouldRefreshTokens() {
        long currentTimeMillis = System.currentTimeMillis();
        long timeLeft = expiresMillis - currentTimeMillis;

        // Recommend a refresh when there is 10 minutes or less left until the
        // auth token expires.
        return timeLeft < 600000;
    }

    public String toJson() {
        String result;

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("access_token", accessToken);
            jsonObject.put("refresh_token", refreshToken);
            jsonObject.put("expires", expiresMillis);

            result = jsonObject.toString();
        } catch (JSONException e) {
            result = null;
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        boolean isEqual = o instanceof Session;

        if (isEqual) {
            Session other = (Session) o;

            boolean accessTokensEquals = accessToken == null && other.accessToken == null
                    || accessToken.equals(other.accessToken);

            boolean refreshTokensEquals = refreshToken == null && other.refreshToken == null
                    || refreshToken.equals(other.refreshToken);

            boolean timeStampsEquals = expiresMillis == other.expiresMillis;

            isEqual = accessTokensEquals && refreshTokensEquals && timeStampsEquals;
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
