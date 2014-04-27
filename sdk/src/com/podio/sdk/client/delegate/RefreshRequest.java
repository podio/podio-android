package com.podio.sdk.client.delegate;

import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

public class RefreshRequest extends StringRequest {
    private final Map<String, String> params;

    public RefreshRequest(String url, Map<String, String> params, RequestFuture<String> future) {
        super(Method.POST, url, future, future);
        setShouldCache(false);

        this.params = params;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
