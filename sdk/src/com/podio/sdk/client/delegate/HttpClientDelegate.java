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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.client.RestResult;
import com.podio.sdk.internal.utils.Utils;
import com.podio.sdk.parser.ItemToJsonParser;
import com.podio.sdk.parser.JsonToItemParser;

public class HttpClientDelegate implements RestClientDelegate {

    private final RequestQueue requestQueue;

    private ItemToJsonParser itemToJsonParser;
    private JsonToItemParser jsonToItemParser;

    public HttpClientDelegate(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        itemToJsonParser = new ItemToJsonParser();
        jsonToItemParser = new JsonToItemParser();
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

    private String performBlockingRequest(int method, String url, JSONObject params) {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(method, url, params, future, future);
        requestQueue.add(request);
        JSONObject response;

        try {
            response = future.get(10, TimeUnit.SECONDS);
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

        return response != null ? response.toString() : null;
    }
}
