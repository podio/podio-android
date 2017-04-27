
package com.podio.sdk;

import android.content.SharedPreferences;

import com.podio.sdk.domain.File;
import com.podio.sdk.internal.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

public class Session {
    private static ISession session = null;

    private static void set(ISession newSession) {
        // unload?
        // ...
        session = newSession;
    }

    public static long expires() {
        return session.expires();
    }

    public static void addAuthorization(HashMap<String, String> headers, boolean isAuthRequest) {
        session.addAuthorization(headers, isAuthRequest);
    }

    public static Header[] getAuthorizationHeaders() {
        return session.getAuthorizationHeaders();
    }

    public static String accessTokenHash() {
        return session.getTokenHash();
    }

    public static void saveToPreferences(SharedPreferences prefs) {
        session.save(prefs);
    }

    public static void reset() {
        // TODO: refactor... should be just setting session to null
        session.reset();
    }

    public static String refreshToken() {
        // TODO: this will not work with SF auth
        String refreshToken = ((PodioSession)session).refreshToken;
        return refreshToken;
    }

    public static String getThumbnailUrl(File file) {
        // TODO: this will not work for the new "SF Auth" :(
        // boom!
        String accessToken = ((PodioSession)session).accessToken;
        return file.getThumbnailLink() + "?oauth_token=" + accessToken;
    }

    public static String getLink(File file) {
        // TODO: this will not work for the new "SF Auth" :(
        // boom!
        String accessToken = ((PodioSession)session).accessToken;
        return file.getLink() + "?oauth_token=" + accessToken;
    }

    public static String accessTokenForGCM() {
        // TODO: this will not work for the new "SF Auth" :(
        // boom!
        return ((PodioSession)session).accessToken;
    }

    public static void initShareFile(String accountId, String accessToken) {
        // TODO: make it generic so this class doesn't need to know about the specific implementation
        Session.set(new ShareFileSession(accountId, accessToken));
    }

    public static void init(SharedPreferences prefs) {
        // TODO: only supports podio
        Session.set( new PodioSession(prefs) );
    }

    public static void set(String json) {
        // TODO: only supports podio
        Session.set( new PodioSession(json) );
    }

    private interface ISession {
        void reset();
        void save(SharedPreferences prefs);
        String getTokenHash();
        Header[] getAuthorizationHeaders();
        void addAuthorization(HashMap<String, String> headers, boolean isAuthRequest);

        long expires();
    }

    private static class PodioSession implements ISession {
        private String accessToken;
        private String refreshToken;
        private String transferToken;
        private long expires;

        PodioSession(String jsonString) {
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
            } catch (JSONException | NullPointerException e) {
                // Input JSON was most likely invalid. Fallback to defaults.
                accessToken = refreshToken = null;
                expires = 0;
            }
        }

        PodioSession(SharedPreferences prefs) {
            accessToken = prefs.getString("com.podio.sdk.access_token", null);
            refreshToken = prefs.getString("com.podio.sdk.refresh_token", null);
            transferToken = prefs.getString("com.podio.sdk.transfer_token", null);
            expires = prefs.getLong("com.podio.sdk.expires", 0);
        }

        @Override
        public void reset() {
            accessToken = null;
            refreshToken =null;
            transferToken = null;
            expires = 0;
        }

        @Override
        public void save(SharedPreferences prefs) {
            prefs.edit()
                .putString("com.podio.sdk.access_token", accessToken)
                .putString("com.podio.sdk.refresh_token", refreshToken)
                .putString("com.podio.sdk.transfer_token", transferToken)
                .putLong("com.podio.sdk.expires", expires)
                .apply();
        }

        @Override
        public String getTokenHash() {
            // use actual string for now
            return accessToken == null ? "" : accessToken;
        }

        @Override
        public Header[] getAuthorizationHeaders() {
            Header[] headers = new Header[2];
            BasicHeader header = new BasicHeader("Authorization", "Bearer " + accessToken);
            headers[0] = header;
            header = new BasicHeader("X-Time-Zone", Calendar.getInstance().getTimeZone().getID());
            headers[1] = header;

            return headers;
        }

        @Override
        public void addAuthorization(HashMap<String, String> headers, boolean isAuthRequest) {
            if (!isAuthRequest && Utils.notEmpty(accessToken)) {
                headers.put("Authorization", "Bearer " + accessToken);
            } else {
                headers.remove("Authorization");
            }
        }

        @Override
        public long expires() {
            return expires;
        }
    }

    private static class ShareFileSession implements ISession {
        private String accountId;
        private String accessToken;

        public ShareFileSession(String accountId, String accessToken) {
            this.accountId = accountId;
            this.accessToken =accessToken;
        }

        @Override
        public void reset() {
            accountId = null;
            accessToken = null;
        }

        @Override
        public void save(SharedPreferences prefs) {
            // ...
        }

        @Override
        public String getTokenHash() {
            return accountId == null ? "" : accountId + accessToken;
        }

        @Override
        public Header[] getAuthorizationHeaders() {
            if ( accountId==null ) return new Header[0];

            Header[] headers = new Header[] {
                new BasicHeader("X-SF-Authorization", accountId + " " + accessToken)
            };

            return headers;
        }

        @Override
        public void addAuthorization(HashMap<String, String> headers, boolean isAuthRequest) {
            headers.put("X-SF-Authorization", accountId + " " + accessToken);
        }

        @Override
        public long expires() {
            // TODO????
            return new Date().getTime() + 24*60*60000;
        }
    }
}
