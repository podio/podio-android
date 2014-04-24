package com.podio.sdk.client.delegate;

import org.json.JSONObject;

import android.util.Base64;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.podio.sdk.Credentials;

public class RefreshRequest extends JsonObjectRequest {
    private final String authBody;

    public RefreshRequest(Credentials credentials, RequestFuture<JSONObject> future) {
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

    private String buildAuthBody(Credentials credentials) {
        String refreshToken = credentials.getRefreshToken();

        return new StringBuilder() //
                .append("grant_type=refresh_token") //
                .append("&refresh_token=").append(refreshToken) //
                .toString();
    }

}
