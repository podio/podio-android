package com.podio.sdk.client.delegate;

import java.util.HashMap;
import java.util.Map;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.podio.sdk.Session;
import com.podio.sdk.internal.utils.Utils;

public class PodioRequest extends StringRequest {

    private final String authToken;
    private final String body;

    public PodioRequest(int method, String url, String body, Session session,
            RequestFuture<String> future) {

        super(method, url, future, future);
        setShouldCache(false);

        this.authToken = session != null ? session.accessToken : "";
        this.body = body;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();

        if (Utils.notEmpty(authToken)) {
            headers.put("Authorization", "Bearer " + authToken);
        }

        return headers;
    }

    @Override
    public byte[] getPostBody() {
        byte[] bytes = body.getBytes();
        byte[] result = Base64.encode(bytes, Base64.DEFAULT);
        return result;
    }

}
