package com.podio.sdk.client.delegate;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.podio.sdk.Credentials;
import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.client.RestResult;
import com.podio.sdk.internal.utils.Utils;
import com.podio.sdk.parser.ItemToJsonParser;
import com.podio.sdk.parser.JsonToItemParser;

public class HttpClientDelegate implements RestClientDelegate {

    private final RequestQueue requestQueue;

    private VolleyError lastRequestError;
    private ItemToJsonParser itemToJsonParser;
    private JsonToItemParser jsonToItemParser;

    private Credentials credentials;

    public HttpClientDelegate(Context context, Credentials credentials) {
        this.requestQueue = Volley.newRequestQueue(context);
        this.itemToJsonParser = new ItemToJsonParser();
        this.jsonToItemParser = new JsonToItemParser();
        this.credentials = credentials;
    }

    @Override
    public RestResult delete(Uri uri) {
        String jsonString = null;

        if (Utils.notEmpty(uri)) {
            int method = Request.Method.DELETE;
            String url = uri.toString();
            JSONObject params = null;
            jsonString = performBlockingRequest(method, url, params);
        }

        boolean isSuccess = jsonString != null;
        String message = null;
        List<?> items = null;
        RestResult result = new RestResult(isSuccess, message, items);

        return result;
    }

    @Override
    public RestResult get(Uri uri, Class<?> classOfResult) {
        String jsonString = null;

        if (Utils.notEmpty(uri)) {
            int method = Request.Method.GET;
            String url = uri.toString();
            JSONObject params = null;
            jsonString = performBlockingRequest(method, url, params);
        }

        boolean isSuccess = jsonString != null;
        String message = null;
        Object item = jsonToItemParser.parse(jsonString, classOfResult);
        RestResult result = new RestResult(isSuccess, message, item);

        return result;
    }

    @Override
    public RestResult post(Uri uri, Object item, Class<?> classOfItem) {
        String jsonString = null;

        if (Utils.notEmpty(uri)) {
            JSONObject params;

            try {
                String json = itemToJsonParser.parse(item, classOfItem);
                params = Utils.notEmpty(json) ? new JSONObject(json) : null;
            } catch (JSONException e) {
                params = null;
            }

            int method = Request.Method.POST;
            String url = uri.toString();
            jsonString = performBlockingRequest(method, url, params);
        }

        boolean isSuccess = jsonString != null;
        String message = null;
        Object content = jsonToItemParser.parse(jsonString, classOfItem);
        RestResult result = new RestResult(isSuccess, message, content);

        return result;
    }

    @Override
    public RestResult put(Uri uri, Object item, Class<?> classOfItem) {
        String jsonString = null;

        if (Utils.notEmpty(uri)) {
            JSONObject params;

            try {
                String json = itemToJsonParser.parse(item, classOfItem);
                params = Utils.notEmpty(json) ? new JSONObject(json) : null;
            } catch (JSONException e) {
                params = null;
            }

            int method = Request.Method.PUT;
            String url = uri.toString();
            jsonString = performBlockingRequest(method, url, params);
        }

        boolean isSuccess = jsonString != null;
        String message = null;
        Object content = jsonToItemParser.parse(jsonString, classOfItem);
        RestResult result = new RestResult(isSuccess, message, content);

        return result;
    }

    /**
     * Sets the parser used for parsing content items when performing an HTTP
     * POST or PUT request. The parser will take the item object, parse data
     * from its fields and create a new JSON string from it.
     * 
     * @param itemToJsonParser
     *            The parser to use for extracting item data.
     */
    public void setItemToJsonParser(ItemToJsonParser itemToJsonParser) {
        if (itemToJsonParser != null) {
            this.itemToJsonParser = itemToJsonParser;
        }
    }

    /**
     * Sets the parser used for parsing the response json when performing an
     * HTTP GET request. The parser will take the json string, parse its
     * attributes and create new corresponding item objects from it.
     * 
     * @param jsonToItemParser
     *            The parser to use for extracting cursor data.
     */
    public void setJsonToItemParser(JsonToItemParser jsonToItemParser) {
        if (jsonToItemParser != null) {
            this.jsonToItemParser = jsonToItemParser;
        }
    }

    private String performBlockingRequest(int method, String url, JSONObject body) {
        JSONObject result = null;

        if (credentials != null) {
            if (!credentials.isAuthorized()) {
                JSONObject authResult = authorize(credentials);
                updateTokens(credentials, authResult);
            }

            if (credentials.shouldRefreshTokens()) {
                JSONObject refreshResult = refresh(credentials);
                updateTokens(credentials, refreshResult);
            }

            if (credentials.isAuthorized() && !credentials.shouldRefreshTokens()) {
                result = perform(method, url, body, credentials);
            }
        }

        return result != null ? result.toString() : null;
    }

    private JSONObject getBlockingResponse(RequestFuture<JSONObject> future) {
        JSONObject response;
        lastRequestError = null;

        try {
            response = future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            response = null;
        } catch (ExecutionException e) {
            lastRequestError = (VolleyError) e.getCause();
            e.printStackTrace();
            response = null;
        } catch (TimeoutException e) {
            e.printStackTrace();
            response = null;
        }

        return response;
    }

    private JSONObject authorize(Credentials credentials) {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new AuthRequest(credentials, future);
        requestQueue.add(request);
        JSONObject result = getBlockingResponse(future);

        return result;
    }

    private JSONObject perform(int method, String url, JSONObject bodyContent,
            Credentials credentials) {

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new PodioRequest(method, url, bodyContent, credentials, future);
        requestQueue.add(request);
        JSONObject result = getBlockingResponse(future);

        if (result == null && lastRequestError != null && lastRequestError.networkResponse != null) {

            // For some reason the server has invalidated our access token.
            if (lastRequestError.networkResponse.statusCode == 401) {
                // Force refresh the access token.
                credentials.forceExpired();
                JSONObject refreshResult = refresh(credentials);
                updateTokens(credentials, refreshResult);

                // Perform the request again.
                future = RequestFuture.newFuture();
                request = new PodioRequest(method, url, bodyContent, credentials, future);
                requestQueue.add(request);
                result = getBlockingResponse(future);
            }
        }

        return result;
    }

    private JSONObject refresh(Credentials credentials) {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new RefreshRequest(credentials, future);
        requestQueue.add(request);
        JSONObject result = getBlockingResponse(future);

        return result;
    }

    private void updateTokens(Credentials credentials, JSONObject responseBody) {
        if (responseBody != null) {
            String authToken = responseBody.optString("access_token");
            String refreshToken = responseBody.optString("refresh_token");
            long expiresIn = responseBody.optLong("expires_in", -1L);
            credentials.setTokens(authToken, refreshToken, expiresIn);
        } else {
            credentials.setTokens(null, null, -1L);
        }
    }
}
