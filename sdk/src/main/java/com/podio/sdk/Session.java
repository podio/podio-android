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

package com.podio.sdk;

import com.podio.sdk.internal.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class Session {

    private String accessToken;
    private String refreshToken;
    private String transferToken;
    private long expires;

    public String accessToken() {
        return accessToken;
    }

    public long expires() {
        return expires;
    }

    public String refreshToken() {
        return refreshToken;
    }

    public String transferToken() {
        return transferToken;
    }

    public void set(String jsonString) {
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(jsonString);
            accessToken = jsonObject.optString("access_token", null);
            refreshToken = jsonObject.optString("refresh_token", null);
            transferToken = jsonObject.optString("transfer_token", null);

            if (jsonObject.has("expires")) {
                expires = jsonObject.optLong("expires", 0L);
            } else if (jsonObject.has("expires_in")) {
                long expiresIn = jsonObject.optLong("expires_in", 0L);
                expires = Utils.currentTimeSeconds() + expiresIn;
            }
        } catch (JSONException e) {
            // Input JSON was most likely invalid. Fallback to defaults.
            accessToken = refreshToken = null;
            expires = 0;
        } catch (NullPointerException e) {
            // Input JSON was most likely a null pointer. Fallback to defaults.
            accessToken = refreshToken = null;
            expires = 0;
        }
    }

    public void set(String accessToken, String refreshToken, long expires) {
        set(accessToken, refreshToken, null, expires);
    }

    public void set(String accessToken, String refreshToken, String transferToken, long expires) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.transferToken = transferToken;
        this.expires = expires;
    }
}
