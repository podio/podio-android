package com.podio.sdk.client.network;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.podio.sdk.internal.utils.Utils;

public class HttpClientDelegate implements NetworkClientDelegate {

    private final RequestQueue requestQueue;

    public HttpClientDelegate(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public String delete(Uri uri) {
        String result = "{}";

        if (Utils.notEmpty(uri)) {
            int method = Request.Method.DELETE;
            String url = uri.toString();
            JSONObject params = null;
            result = performBlockingRequest(method, url, params);
        }

        return result;
    }

    @Override
    public String get(Uri uri) {
        String result = "{}";

        if (Utils.notEmpty(uri)) {
            int method = Request.Method.GET;
            String url = uri.toString();
            JSONObject params = null;
            result = performBlockingRequest(method, url, params);
        }

        return result;
    }

    @Override
    public String post(Uri uri, String json) {
        String result = "{}";

        if (Utils.notEmpty(uri)) {
            JSONObject params;

            try {
                params = Utils.notEmpty(json) ? new JSONObject(json) : null;
            } catch (JSONException e) {
                params = null;
            }

            int method = Request.Method.POST;
            String url = uri.toString();
            result = performBlockingRequest(method, url, params);
        }

        return result;
    }

    @Override
    public String put(Uri uri, String json) {
        String result = "{}";

        if (Utils.notEmpty(uri)) {
            JSONObject params;

            try {
                params = Utils.notEmpty(json) ? new JSONObject(json) : null;
            } catch (JSONException e) {
                params = null;
            }

            int method = Request.Method.PUT;
            String url = uri.toString();
            result = performBlockingRequest(method, url, params);
        }

        return result;
    }

    private String performBlockingRequest(int method, String url, JSONObject params) {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(method, url, params, future, future);
        requestQueue.add(request);
        JSONObject response;

        try {
            response = future.get(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            response = null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            response = null;
        } catch (TimeoutException e) {
            e.printStackTrace();
            response = null;
        }

        return response != null ? response.toString() : "{}";
    }
}
