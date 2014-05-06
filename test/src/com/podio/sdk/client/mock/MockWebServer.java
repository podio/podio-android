/*
 *  Copyright (C) 2014 Copyright Citrix Systems, Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of 
 *  this software and associated documentation files (the "Software"), to deal in 
 *  the Software without restriction, including without limitation the rights to 
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
 *  of the Software, and to permit persons to whom the Software is furnished to 
 *  do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all 
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 *  SOFTWARE.
 */

package com.podio.sdk.client.mock;

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
