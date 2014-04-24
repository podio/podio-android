package com.podio.sdk.client.delegate;

import org.json.JSONObject;

import android.util.Base64;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.podio.sdk.Credentials;
import com.podio.sdk.internal.utils.Utils;

public class AuthRequest extends JsonObjectRequest {

    private final String authBody;

    public AuthRequest(Credentials credentials, RequestFuture<JSONObject> future) {
        super(Method.POST, "https://podio.com/oauth/token", null, future, future);
        setShouldCache(false);
        authBody = buildAuthBody(credentials);
    }

    @Override
    public byte[] getBody() {
        byte[] bytes = authBody.getBytes();
        byte[] result = Base64.encode(bytes, Base64.DEFAULT);
        return result;
    }

    private String buildAppAuthBody(Credentials credentials) {
        String userGrantType = credentials.getUserGrantType();
        String userId = credentials.getUserId();
        String userToken = credentials.getUserToken();
        String clientId = credentials.getClientId();
        String clientSecret = credentials.getClientSecret();

        return new StringBuilder() //
                .append("grant_type=").append(userGrantType) //
                .append("&app_id=").append(userId) //
                .append("&app_token=").append(userToken) //
                .append("&client_id=").append(clientId) //
                .append("&client_secret=").append(clientSecret) //
                .toString();
    }

    private String buildUserAuthBody(Credentials credentials) {
        String userGrantType = credentials.getUserGrantType();
        String userId = credentials.getUserId();
        String userToken = credentials.getUserToken();
        String clientId = credentials.getClientId();
        String clientSecret = credentials.getClientSecret();

        return new StringBuilder() //
                .append("grant_type=").append(userGrantType) //
                .append("&username=").append(userId) //
                .append("&password=").append(userToken) //
                .append("&client_id=").append(clientId) //
                .append("&client_secret=").append(clientSecret) //
                .toString();
    }

    private String buildAuthBody(Credentials credentials) {
        String result = "";
        String grantType = credentials.getUserGrantType();

        if (Utils.notEmpty(grantType)) {
            if (grantType.equals("app")) {
                result = buildAppAuthBody(credentials);
            } else if (grantType.equals("password")) {
                result = buildUserAuthBody(credentials);
            }
        }

        return result;
    }

}
