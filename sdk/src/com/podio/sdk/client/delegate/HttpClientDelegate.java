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
import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.Session;
import com.podio.sdk.SessionListener;
import com.podio.sdk.client.RestResult;
import com.podio.sdk.internal.utils.Utils;
import com.podio.sdk.parser.ItemToJsonParser;
import com.podio.sdk.parser.JsonToItemParser;

public class HttpClientDelegate implements RestClientDelegate {

    private final RequestQueue requestQueue;

    private VolleyError lastRequestError;
    private ItemToJsonParser itemToJsonParser;
    private JsonToItemParser jsonToItemParser;

    private Session session;
    private SessionListener sessionListener;

    public HttpClientDelegate(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
        this.itemToJsonParser = new ItemToJsonParser();
        this.jsonToItemParser = new JsonToItemParser();
    }

    @Override
    public RestResult authorize(Uri uri) {
        JSONObject jsonObject = null;

        if (Utils.notEmpty(uri)) {
            String url = uri.toString();
            String body = uri.getQuery();

            int queryStart = url.indexOf(body) - 1; // -1 for the "?"
            if (queryStart > 0) {
                url = url.substring(0, queryStart);
            }

            jsonObject = authorize(url, body);
            updateSession(jsonObject);
        }

        boolean isSuccess = jsonObject != null;
        RestResult result = new RestResult(isSuccess, null, null);

        return result;
    }

    @Override
    public RestResult delete(Uri uri) {
        JSONObject jsonObject = null;

        if (Utils.notEmpty(uri)) {
            int method = Request.Method.DELETE;
            String url = uri.toString();
            JSONObject params = null;
            jsonObject = request(method, url, params);
        }

        boolean isSuccess = jsonObject != null;
        String message = null;
        List<?> items = null;
        RestResult result = new RestResult(isSuccess, message, items);

        return result;
    }

    @Override
    public RestResult get(Uri uri, Class<?> classOfResult) {
        JSONObject jsonObject = null;

        if (Utils.notEmpty(uri)) {
            int method = Request.Method.GET;
            String url = uri.toString();
            JSONObject params = null;
            jsonObject = request(method, url, params);
        }

        boolean isSuccess = jsonObject != null;
        String message = null;
        String jsonString = isSuccess ? jsonObject.toString() : null;
        Object item = jsonToItemParser.parse(jsonString, classOfResult);
        RestResult result = new RestResult(isSuccess, message, item);

        return result;
    }

    @Override
    public RestResult post(Uri uri, Object item, Class<?> classOfItem) {
        JSONObject jsonObject = null;

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
            jsonObject = request(method, url, params);
        }

        boolean isSuccess = jsonObject != null;
        String message = null;
        String jsonString = isSuccess ? jsonObject.toString() : null;
        Object content = jsonToItemParser.parse(jsonString, classOfItem);
        RestResult result = new RestResult(isSuccess, message, content);

        return result;
    }

    @Override
    public RestResult put(Uri uri, Object item, Class<?> classOfItem) {
        JSONObject jsonObject = null;

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
            jsonObject = request(method, url, params);
        }

        boolean isSuccess = jsonObject != null;
        String message = null;
        String jsonString = isSuccess ? jsonObject.toString() : null;
        Object content = jsonToItemParser.parse(jsonString, classOfItem);
        RestResult result = new RestResult(isSuccess, message, content);

        return result;
    }

    /**
     * Revokes a previously stored session. The network delegate will use this
     * session object when authenticating API calls.
     * 
     * @param session
     *            The new session object to use.
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * Sets the new session listener callback. Every time the session variables
     * are refreshed, this callback will be notified, offering the listening
     * party to persist the session.
     * 
     * @param sessionListener
     *            The callback implementation.
     */
    public void setSessionListener(SessionListener sessionListener) {
        this.sessionListener = sessionListener;
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

    private JSONObject request(int method, String url, JSONObject body) {
        JSONObject result = null;

        if (session != null && session.isAuthorized()) {
            if (session.shouldRefreshTokens()) {
                JSONObject refreshResult = refresh(session);
                updateSession(refreshResult);
            }

            if (session.isAuthorized() && !session.shouldRefreshTokens()) {
                result = perform(method, url, body, session);
            }
        }

        return result;
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

    private JSONObject authorize(String url, String body) {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new AuthRequest(url, body, future);
        requestQueue.add(request);
        JSONObject result = getBlockingResponse(future);

        return result;
    }

    private JSONObject perform(int method, String url, JSONObject bodyContent, Session session) {

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new PodioRequest(method, url, bodyContent, session, future);
        requestQueue.add(request);
        JSONObject result = getBlockingResponse(future);

        if (result == null && lastRequestError != null && lastRequestError.networkResponse != null) {

            // For some reason the server has invalidated our access token.
            if (lastRequestError.networkResponse.statusCode == 401) {
                // Force refresh the access token.
                session = new Session(null);
                JSONObject refreshResult = refresh(session);
                session = new Session(refreshResult);

                // Perform the request again.
                future = RequestFuture.newFuture();
                request = new PodioRequest(method, url, bodyContent, session, future);
                requestQueue.add(request);
                result = getBlockingResponse(future);
            }
        }

        return result;
    }

    private JSONObject refresh(Session session) {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new RefreshRequest(session, future);
        requestQueue.add(request);
        JSONObject result = getBlockingResponse(future);

        return result;
    }

    private void updateSession(JSONObject sessionData) {
        session = new Session(sessionData);

        if (sessionListener != null) {
            sessionListener.onSessionChange(session);
        }
    }
}
