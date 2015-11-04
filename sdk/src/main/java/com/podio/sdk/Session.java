
package com.podio.sdk;

import com.podio.sdk.internal.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class Session {

    private static String accessToken;
    private static String refreshToken;
    private static String transferToken;
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

    public static String transferToken() {
        return transferToken;
    }

    public static void set(String jsonString) {
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

    public static void set(String accessToken, String refreshToken, long expires) {
        set(accessToken, refreshToken, null, expires);
    }

    public static void set(String accessToken, String refreshToken, String transferToken, long expires) {
        Session.accessToken = accessToken;
        Session.refreshToken = refreshToken;
        Session.transferToken = transferToken;
        Session.expires = expires;
    }

    private Session() {
    }
}
