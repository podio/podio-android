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

import org.json.JSONException;
import org.json.JSONObject;

import com.podio.sdk.internal.Utils;

public class Session {
    private static String accessToken;
    private static String refreshToken;
    private static long expires;

    public static String accessToken() {
        return accessToken;
    }

    public static long expires() {
        return expires;
    }

    public static String refreshToken() {
        return refreshToken;
    }

    public static void set(String jsonString) {
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(jsonString);
            accessToken = jsonObject.optString("access_token", null);
            refreshToken = jsonObject.optString("refresh_token", null);

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

    public static void set(String accessToken, String refreshToken, long expires) {
        Session.accessToken = accessToken;
        Session.refreshToken = refreshToken;
        Session.expires = expires;
    }

    private Session() {
    }
}
