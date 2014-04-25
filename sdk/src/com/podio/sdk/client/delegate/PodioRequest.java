package com.podio.sdk.client.delegate;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.podio.sdk.Session;
import com.podio.sdk.internal.utils.Utils;

public class PodioRequest extends JsonObjectRequest {

    private final Session session;

    public PodioRequest(int method, String url, JSONObject bodyContent, Session session,
            RequestFuture<JSONObject> future) {

        super(method, url, bodyContent, future, future);
        setShouldCache(false);
        this.session = session;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        String authToken = session != null ? session.accessToken : null;

        if (Utils.notEmpty(authToken)) {
            headers.put("Authorization", "OAuth oauth_token=" + authToken);
        }

        return headers;
    }

}
