package com.podio.sdk.client.delegate;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Instrumentation;
import android.content.Context;
import android.net.Uri;
import android.test.InstrumentationTestCase;

import com.podio.sdk.client.RestResult;
import com.podio.sdk.client.delegate.mock.MockContentItem;
import com.podio.sdk.client.mock.MockWebServer;
import com.podio.sdk.domain.Session;
import com.podio.sdk.parser.ItemToJsonParser;

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
        target.setSession(new Session("accessToken", "refreshToken", 3600L));
    }

    @Override
    protected void tearDown() throws Exception {
        mockWebServer.stop();
        mockWebServer = null;
        super.tearDown();
    }

    /**
     * Verifies that the {@link HttpClientDelegate} returns a valid result with
     * a no-success flag set if the caller provides an empty URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty URI to the network helpers delete method.
     * 
     * 2. Verify that a RestResult is returned.
     * 
     * 3. Verify that the success flag is set to false.
     * 
     * </pre>
     */
    public void testDeleteHandlesEmptyUriCorrectly() {
        RestResult result = target.delete(Uri.EMPTY);
        assertNotNull(result);
        assertEquals(false, result.isSuccess());
    }

    /**
     * Verifies that the {@link HttpClientDelegate} returns a valid result with
     * a no-success flag set if the caller provides a null-pointer URI.
     * 
     * <pre>
     * 
     * 1. Pass on a null-pointer URI to the network helpers delete method.
     * 
     * 2. Verify that a RestResult is returned.
     * 
     * 3. Verify that the success flag is set to false.
     * 
     * </pre>
     */
    public void testDeleteHandlesNullUriCorrectly() {
        RestResult result = target.delete(null);
        assertNotNull(result);
        assertEquals(false, result.isSuccess());
    }

    /**
     * Verifies that the Android Volley library integration works as expected
     * for the {@link HttpClientDelegate} delete method.
     * 
     * <pre>
     * 
     * 1. Verify that the mock web server is up and running.
     * 
     * 3. Make a call to the HttpClientDelegate delete method.
     * 
     * 4. Verify that the call went through the Volley integration and the
     *      result has the success flag set to true.
     * 
     * </pre>
     * 
     * @throws JSONException
     */
    public void testDeleteVolleyIntegrationReturnsCorrectJsonResponse() throws JSONException {
        assertEquals(true, mockWebServer.wasStarted());
        assertEquals(true, mockWebServer.isAlive());

        JSONObject mockResponse = new JSONObject();
        mockResponse.put("value", "test");
        mockWebServer.setMockResponse(mockResponse);

        RestResult result = target.delete(Uri.parse("http://localhost:8080"));
        assertNotNull(result);
        assertEquals(true, result.isSuccess());

        Method servedMethod = mockWebServer.getServedMethod();
        assertEquals(Method.DELETE, servedMethod);

    }

    /**
     * Verifies that the {@link HttpClientDelegate} returns a valid result with
     * a no-success flag set if the caller provides an empty URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty URI to the network helpers get method.
     * 
     * 2. Verify that a RestResult is returned.
     * 
     * 3. Verify that the success flag is set to false.
     * 
     * </pre>
     */
    public void testGetHandlesEmptyUriCorrectly() {
        RestResult result = target.get(Uri.EMPTY, null);
        assertNotNull(result);
        assertEquals(false, result.isSuccess());
    }

    /**
     * Verifies that the {@link HttpClientDelegate} returns a valid result with
     * a no-success flag set if the caller provides a null-pointer URI.
     * 
     * <pre>
     * 
     * 1. Pass on a null-pointer URI to the network helpers get method.
     * 
     * 2. Verify that a RestResult is returned.
     * 
     * 3. Verify that the success flag is set to false.
     * 
     * </pre>
     */
    public void testGetHandlesNullUriCorrectly() {
        RestResult result = target.get(null, null);
        assertNotNull(result);
        assertEquals(false, result.isSuccess());
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
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public void testGetVolleyIntegrationReturnsCorrectJsonResponse() throws JSONException,
            UnsupportedEncodingException {

        assertEquals(true, mockWebServer.wasStarted());
        assertEquals(true, mockWebServer.isAlive());

        Uri uri = Uri.parse("http://localhost:8080");

        JSONObject mockResponse = new JSONObject();
        mockResponse.put("uri", "test.uri");
        mockResponse.put("json", "{text: 'test'}");
        mockWebServer.setMockResponse(mockResponse);

        RestResult result = target.get(uri, MockContentItem.class);
        assertNotNull(result);
        assertNotNull(result.item());
        assertEquals(true, result.isSuccess());

        MockContentItem item = (MockContentItem) result.item();
        ItemToJsonParser parser = new ItemToJsonParser();
        String fetchedJson = parser.parse(item, MockContentItem.class);
        String mockedJson = mockResponse.toString();
        assertEquals(mockedJson, fetchedJson);

        Method servedMethod = mockWebServer.getServedMethod();
        assertEquals(Method.GET, servedMethod);
    }

    /**
     * Verifies that the {@link HttpClientDelegate} returns a valid result with
     * a no-success flag set if the caller provides an empty URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty URI to the network helpers post method.
     * 
     * 2. Verify that a RestResult is returned.
     * 
     * 3. Verify that the success flag is set to false.
     * 
     * </pre>
     */
    public void testPostHandlesEmptyUriCorrectly() {
        RestResult result = target.post(Uri.EMPTY, null, null);
        assertNotNull(result);
        assertEquals(false, result.isSuccess());
    }

    /**
     * Verifies that the {@link HttpClientDelegate} returns a valid result with
     * a no-success flag set if the caller provides a null-pointer URI.
     * 
     * <pre>
     * 
     * 1. Pass on a null-pointer URI to the network helpers post method.
     * 
     * 2. Verify that a RestResult is returned.
     * 
     * 3. Verify that the success flag is set to false.
     * 
     * </pre>
     */
    public void testPostHandlesNullUriCorrectly() {
        RestResult result = target.post(null, null, null);
        assertNotNull(result);
        assertEquals(false, result.isSuccess());
    }

    /**
     * Verifies that the Android Volley library integration works as expected
     * for the HttpClientDelegate post method.
     * 
     * <pre>
     * 
     * 1. Verify that the mock web server is up and running.
     * 
     * 2. Make a call to the HttpClientDelegate post method.
     * 
     * 3. Verify that the call went through the Volley integration and the
     *      result has the success flag set to true.
     * 
     * </pre>
     * 
     * @throws JSONException
     */
    public void testPostVolleyIntegrationReturnsCorrectStatus() throws JSONException {
        assertEquals(true, mockWebServer.wasStarted());
        assertEquals(true, mockWebServer.isAlive());

        Uri uri = Uri.parse("http://localhost:8080");

        MockContentItem item = new MockContentItem(uri.toString(), "{text: 'test'}");
        RestResult result = target.post(uri, item, MockContentItem.class);
        assertNotNull(result);
        assertEquals(true, result.isSuccess());

        Method servedMethod = mockWebServer.getServedMethod();
        assertEquals(Method.POST, servedMethod);
    }

    /**
     * Verifies that the {@link HttpClientDelegate} returns a valid result with
     * a no-success flag set if the caller provides an empty URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty URI to the network helpers put method.
     * 
     * 2. Verify that a RestResult is returned.
     * 
     * 3. Verify that the success flag is set to false.
     * 
     * </pre>
     */
    public void testPutHandlesEmptyUriCorrectly() {
        RestResult result = target.put(Uri.EMPTY, null, null);
        assertNotNull(result);
        assertEquals(false, result.isSuccess());
    }

    /**
     * Verifies that the {@link HttpClientDelegate} returns a valid result with
     * a no-success flag set if the caller provides a null-pointer URI.
     * 
     * <pre>
     * 
     * 1. Pass on a null-pointer URI to the network helpers put method.
     * 
     * 2. Verify that a RestResult is returned.
     * 
     * 3. Verify that the success flag is set to false.
     * 
     * </pre>
     */
    public void testPutHandlesNullUriCorrectly() {
        RestResult result = target.put(null, null, null);
        assertNotNull(result);
        assertEquals(false, result.isSuccess());
    }

    /**
     * Verifies that the Android Volley library integration works as expected
     * for the HttpClientDelegate put method.
     * 
     * <pre>
     * 
     * 1. Verify that the mock web server is up and running.
     * 
     * 2. Make a call to the HttpClientDelegate put method.
     * 
     * 4. Verify that the call went through the Volley integration and the
     *      result has the success flag set to true.
     * 
     * </pre>
     * 
     * @throws JSONException
     */
    public void testPutVolleyIntegrationReturnsCorrectJsonResponse() throws JSONException {
        assertEquals(true, mockWebServer.wasStarted());
        assertEquals(true, mockWebServer.isAlive());

        MockContentItem item = new MockContentItem("http://localhost:8080", "{text: 'test'}");
        Uri uri = Uri.parse(item.uri);
        RestResult result = target.put(uri, item, MockContentItem.class);
        assertNotNull(result);
        assertEquals(true, result.isSuccess());

        Method servedMethod = mockWebServer.getServedMethod();
        assertEquals(Method.PUT, servedMethod);
    }
}
