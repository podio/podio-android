
package com.podio.sdk.volley;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import fi.iki.elonen.NanoHTTPD;

public class MockWebServer extends NanoHTTPD {

    private JSONObject response;
    private JSONObject session;

    private HashMap<String, JSONObject> responses;

    private String requestUri;
    private String requestBody;
    private Method requestMethod;
    private Map<String, String> requestHeaders;
    private Map<String, String> requestParams;

    public MockWebServer() {
        super(8080);
        responses = new HashMap<String, JSONObject>();
    }

    public void mock_setResponse(JSONObject response) {
        this.response = response;
    }

    public void mock_setResponse(String path, JSONObject response) {
        this.responses.put(path, response);
    }

    public void mock_setSession(JSONObject session) {
        this.session = session;
    }

    public String mock_getRequestBody() {
        return requestBody;
    }

    public String mock_getRequestUri() {
        return requestUri;
    }

    public Method mock_getRequestMethod() {
        return requestMethod;
    }

    public Map<String, String> mock_getRequestHeaders() {
        return requestHeaders;
    }

    public Map<String, String> mock_getRequestParams() {
        return requestParams;
    }

    @Override
    public Response serve(String uri, Method method, Map<String, String> headers,
            Map<String, String> parms, Map<String, String> files) {

        requestUri = uri;
        requestBody = parms != null ? parms.get("NanoHttpd.QUERY_STRING") : "";

        requestMethod = method;
        requestHeaders = headers;
        requestParams = parms;

        Response result;

        if (uri.equals("/auth/token")) {
            String json = session != null ? session.toString() : "{}";
            result = new Response(json);
        } else {
            JSONObject r = responses.get(uri);
            String json = r != null ? r.toString() : response != null ? response.toString() : "{}";
            result = new Response(json);
        }

        return result;
    }
}
