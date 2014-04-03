package com.podio.sdk.client.mock;

import java.util.Map;

import org.json.JSONObject;

import fi.iki.elonen.NanoHTTPD;

public class MockWebServer extends NanoHTTPD {

    private JSONObject response;
    private Method servedMethod;
    private Map<String, String> servedHeaders;

    public MockWebServer(String host, int port) {
        super(host, port);
    }

    public void setMockResponse(JSONObject response) {
        this.response = response;
    }

    public Method getServedMethod() {
        return servedMethod;
    }

    public Map<String, String> getServedHeaders() {
        return servedHeaders;
    }

    @Override
    public Response serve(String uri, Method method, Map<String, String> headers,
            Map<String, String> parms, Map<String, String> files) {

        servedMethod = method;
        servedHeaders = headers;

        String json = response != null ? response.toString() : "{}";
        Response response = new Response(json);

        return response;
    }
}
