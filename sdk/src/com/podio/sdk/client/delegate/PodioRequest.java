package com.podio.sdk.client.delegate;

import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

public class PodioRequest extends StringRequest {

    private final Map<String, String> headers;
    private final String body;

    public PodioRequest(int method, String url, String body, Map<String, String> headers,
            RequestFuture<String> future) {

        super(method, url, future, future);
        setShouldCache(false);

        this.headers = headers;
        this.body = body;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    @Override
    public byte[] getBody() {
        byte[] bytes = body.getBytes();
        return bytes;
    }

}
