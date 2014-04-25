package com.podio.sdk.client.delegate;

import org.json.JSONObject;

import android.util.Base64;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

public class AuthRequest extends JsonObjectRequest {

    private final String authBody;

    public AuthRequest(String url, String body, RequestFuture<JSONObject> future) {
        super(Method.POST, url, null, future, future);
        setShouldCache(false);
        authBody = body;
    }

    @Override
    public byte[] getBody() {
        byte[] bytes = authBody.getBytes();
        byte[] result = Base64.encode(bytes, Base64.DEFAULT);
        return result;
    }

}
