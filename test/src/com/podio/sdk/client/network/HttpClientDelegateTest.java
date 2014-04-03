package com.podio.sdk.client.network;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Instrumentation;
import android.content.Context;
import android.net.Uri;
import android.test.InstrumentationTestCase;

import com.podio.sdk.client.mock.MockWebServer;

import fi.iki.elonen.NanoHTTPD.Method;

public class HttpClientDelegateTest extends InstrumentationTestCase {

    private MockWebServer mockWebServer;
    private HttpClientDelegate target;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mockWebServer = new MockWebServer("localhost", 8080);
        mockWebServer.start();

        Instrumentation instrumentation = getInstrumentation();
        Context context = instrumentation.getTargetContext();
        target = new HttpClientDelegate(context);
    }

    @Override
    protected void tearDown() throws Exception {
        mockWebServer.stop();
        mockWebServer = null;
        super.tearDown();
    }

    /**
     * Verifies that the {@link HttpClientDelegate} returns a valid result
     * string even if the caller provides an empty URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty URI to the network helpers delete method.
     * 
     * 2. Verify that a string representation of an empty JSON is returned.
     * 
     * </pre>
     */
    public void testDeleteHandlesEmptyUriCorrectly() {
        String result = target.delete(Uri.EMPTY);
        assertEquals("{}", result);
    }

    /**
     * Verifies that the {@link HttpClientDelegate} returns a valid result
     * string even if the caller provides a null-pointer URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty URI to the network helpers delete method.
     * 
     * 2. Verify that a string representation of an empty JSON is returned.
     * 
     * </pre>
     */
    public void testDeleteHandlesNullUriCorrectly() {
        String result = target.delete(null);
        assertEquals("{}", result);
    }

    /**
     * Verifies that the delete method in the {@link HttpClientDelegate}
     * triggers a DELETE method request in the web server.
     * 
     * <pre>
     * 
     * 1. Verify that the mock web server is up and running.
     * 
     * 2. Make a call to the delete method.
     * 
     * 3. Verify that the mock web server received a DELETE request.
     * 
     * </pre>
     */
    public void testDeleteRequestsCorrectMethod() {
        assertEquals(true, mockWebServer.wasStarted());
        assertEquals(true, mockWebServer.isAlive());

        target.delete(Uri.parse("http://localhost:8080"));
        Method servedMethod = mockWebServer.getServedMethod();
        assertEquals(Method.DELETE, servedMethod);
    }

    /**
     * Verifies that the Android Volley library integration works as expected
     * for the {@link HttpClientDelegate} delete method.
     * 
     * <pre>
     * 
     * 1. Verify that the mock web server is up and running.
     * 
     * 2. Add the expected mock result to the mock web server.
     * 
     * 3. Make a call to the HttpClientDelegate delete method.
     * 
     * 4. Verify that the call went through the Volley integration and the
     *      expected (mock) result is returned from the mock web server.
     * 
     * </pre>
     * 
     * @throws IOException
     * @throws JSONException
     */
    public void testDeleteVolleyIntegrationReturnsCorrectJsonResponse() throws IOException,
            JSONException {
        assertEquals(true, mockWebServer.wasStarted());
        assertEquals(true, mockWebServer.isAlive());

        JSONObject mockResponse = new JSONObject();
        mockResponse.put("value", "test");
        mockWebServer.setMockResponse(mockResponse);

        String resultJson = target.delete(Uri.parse("http://localhost:8080"));

        assertNotNull(resultJson);
        assertEquals(mockResponse.toString(), resultJson);
    }

    /**
     * Verifies that the {@link HttpClientDelegate} returns a valid result
     * string even if the caller provides an empty URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty URI to the network helpers get method.
     * 
     * 2. Verify that a string representation of an empty JSON is returned.
     * 
     * </pre>
     */
    public void testGetHandlesEmptyUriCorrectly() {
        String result = target.get(Uri.EMPTY);
        assertEquals("{}", result);
    }

    /**
     * Verifies that the {@link HttpClientDelegate} returns a valid result
     * string even if the caller provides a null-pointer URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty URI to the network helpers get method.
     * 
     * 2. Verify that a string representation of an empty JSON is returned.
     * 
     * </pre>
     */
    public void testGetHandlesNullUriCorrectly() {
        String result = target.delete(null);
        assertEquals("{}", result);
    }

    /**
     * Verifies that the get method in the {@link HttpClientDelegate} triggers a
     * GET method request in the web server.
     * 
     * <pre>
     * 
     * 1. Verify that the mock web server is up and running.
     * 
     * 2. Make a call to the get method.
     * 
     * 3. Verify that the mock web server received a GET request.
     * 
     * </pre>
     */
    public void testGetRequestsCorrectMethod() {
        assertEquals(true, mockWebServer.wasStarted());
        assertEquals(true, mockWebServer.isAlive());

        target.get(Uri.parse("http://localhost:8080"));
        Method servedMethod = mockWebServer.getServedMethod();
        assertEquals(Method.GET, servedMethod);
    }

    /**
     * Verifies that the Android Volley library integration works as expected
     * for the HttpClientDelegate get method.
     * 
     * <pre>
     * 
     * 1. Verify that the mock web server is up and running.
     * 
     * 2. Add the expected mock result to the mock web server.
     * 
     * 3. Make a call to the HttpClientDelegate get method.
     * 
     * 4. Verify that the call went through the Volley integration and the
     *      expected (mock) result is returned from the mock web server.
     * 
     * </pre>
     * 
     * @throws IOException
     * @throws JSONException
     */
    public void testGetVolleyIntegrationReturnsCorrectJsonResponse() throws IOException,
            JSONException {
        assertEquals(true, mockWebServer.wasStarted());
        assertEquals(true, mockWebServer.isAlive());

        JSONObject mockResponse = new JSONObject();
        mockResponse.put("value", "test");
        mockWebServer.setMockResponse(mockResponse);

        String resultJson = target.get(Uri.parse("http://localhost:8080"));

        assertNotNull(resultJson);
        assertEquals(mockResponse.toString(), resultJson);
    }

    /**
     * Verifies that the {@link HttpClientDelegate} returns a valid result
     * string even if the caller provides an empty URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty URI to the network helpers post method.
     * 
     * 2. Verify that a string representation of an empty JSON is returned.
     * 
     * </pre>
     */
    public void testPostHandlesEmptyUriCorrectly() {
        String result = target.post(Uri.EMPTY, null);
        assertEquals("{}", result);
    }

    /**
     * Verifies that the {@link HttpClientDelegate} returns a valid result
     * string even if the caller provides a null-pointer URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty URI to the network helpers post method.
     * 
     * 2. Verify that a string representation of an empty JSON is returned.
     * 
     * </pre>
     */
    public void testPostHandlesNullUriCorrectly() {
        String result = target.post(null, null);
        assertEquals("{}", result);
    }

    /**
     * Verifies that the post method in the {@link HttpClientDelegate} triggers
     * a POST method request in the web server.
     * 
     * <pre>
     * 
     * 1. Verify that the mock web server is up and running.
     * 
     * 2. Make a call to the post method.
     * 
     * 3. Verify that the mock web server received a POST request.
     * 
     * </pre>
     */
    public void testPostRequestsCorrectMethod() {
        assertEquals(true, mockWebServer.wasStarted());
        assertEquals(true, mockWebServer.isAlive());

        target.post(Uri.parse("http://localhost:8080"), null);
        Method servedMethod = mockWebServer.getServedMethod();
        assertEquals(Method.POST, servedMethod);
    }

    /**
     * Verifies that the Android Volley library integration works as expected
     * for the HttpClientDelegate post method.
     * 
     * <pre>
     * 
     * 1. Verify that the mock web server is up and running.
     * 
     * 2. Add the expected mock result to the mock web server.
     * 
     * 3. Make a call to the HttpClientDelegate post method.
     * 
     * 4. Verify that the call went through the Volley integration and the
     *      expected (mock) result is returned from the mock web server.
     * 
     * </pre>
     * 
     * @throws IOException
     * @throws JSONException
     */
    public void testPostVolleyIntegrationReturnsCorrectJsonResponse() throws IOException,
            JSONException {
        assertEquals(true, mockWebServer.wasStarted());
        assertEquals(true, mockWebServer.isAlive());

        JSONObject mockResponse = new JSONObject();
        mockResponse.put("value", "test");
        mockWebServer.setMockResponse(mockResponse);

        String resultJson = target.post(Uri.parse("http://localhost:8080"), null);

        assertNotNull(resultJson);
        assertEquals(mockResponse.toString(), resultJson);
    }

    /**
     * Verifies that the {@link HttpClientDelegate} returns a valid result
     * string even if the caller provides an empty URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty URI to the network helpers put method.
     * 
     * 2. Verify that a string representation of an empty JSON is returned.
     * 
     * </pre>
     */
    public void testPutHandlesEmptyUriCorrectly() {
        String result = target.put(Uri.EMPTY, null);
        assertEquals("{}", result);
    }

    /**
     * Verifies that the {@link HttpClientDelegate} returns a valid result
     * string even if the caller provides a null-pointer URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty URI to the network helpers put method.
     * 
     * 2. Verify that a string representation of an empty JSON is returned.
     * 
     * </pre>
     */
    public void testPutHandlesNullUriCorrectly() {
        String result = target.put(null, null);
        assertEquals("{}", result);
    }

    /**
     * Verifies that the put method in the {@link HttpClientDelegate} triggers a
     * PUT method request in the web server.
     * 
     * <pre>
     * 
     * 1. Verify that the mock web server is up and running.
     * 
     * 2. Make a call to the put method.
     * 
     * 3. Verify that the mock web server received a PUT request.
     * 
     * </pre>
     */
    public void testPutRequestsCorrectMethod() {
        assertEquals(true, mockWebServer.wasStarted());
        assertEquals(true, mockWebServer.isAlive());

        target.put(Uri.parse("http://localhost:8080"), null);
        Method servedMethod = mockWebServer.getServedMethod();
        assertEquals(Method.PUT, servedMethod);
    }

    /**
     * Verifies that the Android Volley library integration works as expected
     * for the HttpClientDelegate put method.
     * 
     * <pre>
     * 
     * 1. Verify that the mock web server is up and running.
     * 
     * 2. Add the expected mock result to the mock web server.
     * 
     * 3. Make a call to the HttpClientDelegate put method.
     * 
     * 4. Verify that the call went through the Volley integration and the
     *      expected (mock) result is returned from the mock web server.
     * 
     * </pre>
     * 
     * @throws IOException
     * @throws JSONException
     */
    public void testPutVolleyIntegrationReturnsCorrectJsonResponse() throws IOException,
            JSONException {
        assertEquals(true, mockWebServer.wasStarted());
        assertEquals(true, mockWebServer.isAlive());

        JSONObject mockResponse = new JSONObject();
        mockResponse.put("value", "test");
        mockWebServer.setMockResponse(mockResponse);

        String resultJson = target.put(Uri.parse("http://localhost:8080"), null);

        assertNotNull(resultJson);
        assertEquals(mockResponse.toString(), resultJson);
    }
}
