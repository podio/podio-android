package com.podio.sdk.client.delegate;

import android.util.Base64;

import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.podio.sdk.Session;

public class RefreshRequest extends StringRequest {
    private final String authBody;

    public RefreshRequest(String url, Session session, RequestFuture<String> future) {
        super(Method.POST, url, future, future);
        setShouldCache(false);
        authBody = buildAuthBody(session);
    }

    @Override
    public byte[] getPostBody() {
        byte[] bytes = authBody.getBytes();
        byte[] result = Base64.encode(bytes, Base64.DEFAULT);
        return result;
    }

    private String buildAuthBody(Session session) {
        return new StringBuilder() //
                .append("grant_type=refresh_token") //
                .append("&refresh_token=").append(session.refreshToken) //
                .toString();
    }

}
