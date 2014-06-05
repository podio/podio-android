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

package com.podio.sdk.client.delegate;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Instrumentation;
import android.content.Context;
import android.net.Uri;
import android.test.InstrumentationTestCase;

import com.podio.sdk.PodioParser;
import com.podio.sdk.client.RestResult;
import com.podio.sdk.client.delegate.mock.MockContentItem;
import com.podio.sdk.client.delegate.mock.MockItemParser;
import com.podio.sdk.client.mock.MockWebServer;
import com.podio.sdk.domain.Session;
import com.podio.sdk.provider.BasicPodioProvider;

import fi.iki.elonen.NanoHTTPD.Method;

public class HttpClientDelegateTest extends InstrumentationTestCase {

    private MockWebServer mockWebServer;
    private PodioParser<MockContentItem> itemParser;
    private HttpClientDelegate target;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        Instrumentation instrumentation = getInstrumentation();
        Context context = instrumentation.getTargetContext();
        Session session = new Session("accessToken", "refreshToken", 3600L);

        itemParser = PodioParser.fromClass(MockContentItem.class);

        target = new HttpClientDelegate(context);
        target.restoreSession("http://localhost:8080/auth/token", session);
    }

    @Override
    protected void tearDown() throws Exception {
        mockWebServer.stop();
        mockWebServer = null;
        super.tearDown();
    }

    /**
     * Verifies that a authorize operation request won't throw an exception if
     * the delegate doesn't have an assigned item parser.
     * 
     * <pre>
     * 
     * 1. Create a new {@link SQLiteClientDelegate} object.
     * 
     * 2. Make sure it has a null pointer {@link BasicPodioProvider} assigned to it.
     * 
     * 3. Request a authorize operation.
     * 
     * 4. Verify that a {@link InvalidParserException} has not been thrown.
     * 
     * </pre>
     */
    public void testAuthorizeDoesNotThrowExceptionOnNullPointerParser() {
    	target.authorize(Uri.parse("http://localhost:8080"), null);
    }

    /**
     * Verifies that the {@link HttpClientDelegate} returns a valid result with
     * a no-success flag set if the caller provides an empty URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty URI to the network helpers authorize method.
     * 
     * 2. Verify that a RestResult is returned.
     * 
     * 3. Verify that the success flag is set to false.
     * 
     * </pre>
     */
    public void testAuthorizeHandlesEmptyUriCorrectly() {
    	try {    	
    		target.authorize(Uri.EMPTY, itemParser);
			fail("Should have thrown exception");
		} catch (IllegalArgumentException e) {
		}
    }

    /**
     * Verifies that the {@link HttpClientDelegate} returns a valid result with
     * a no-success flag set if the caller provides a null-pointer URI.
     * 
     * <pre>
     * 
     * 1. Pass on a null-pointer URI to the network helpers authorize method.
     * 
     * 2. Verify that a RestResult is returned.
     * 
     * 3. Verify that the success flag is set to false.
     * 
     * </pre>
     */
    public void testAuthorizeHandlesNullUriCorrectly() {
    	try {    	
    		target.authorize(null, itemParser);
    		fail("Should have thrown exception");
		} catch (IllegalArgumentException e) {
		}
    }

    /**
     * Verifies that the {@link HttpClientDelegate} posts the query parameters
     * of the authorize uri as content body.
     * 
     * <pre>
     * 
     * 1. Verify that the mock web server is up and running.
     * 
     * 2. Make a call to the network helpers authorize method with a Uri that
     *      has query parameters.
     * 
     * 3. Verify that the mockWebServer received the query parameters as body
     *      content.
     * 
     * </pre>
     */
    public void testAuthorizePostsQueryParametersAsBody() {
        assertEquals(true, mockWebServer.wasStarted());
        assertEquals(true, mockWebServer.isAlive());

        JSONObject mockResponse = new JSONObject();
        mockWebServer.mock_setResponse(mockResponse);

        String mockAuthQueryParams = "key1=value1&key2=value2&key3=value3";
        String mockAuthUri = "http://localhost:8080";

        Uri authUri = Uri.parse(mockAuthUri + "?" + mockAuthQueryParams);
        target.authorize(authUri, itemParser);

        String requestBody = mockWebServer.mock_getRequestBody();
        String[] particlesArray = requestBody.split("&");
        List<String> particlesList = Arrays.asList(particlesArray);

        assertEquals(true, particlesList.contains("key1=value1"));
        assertEquals(true, particlesList.contains("key2=value2"));
        assertEquals(true, particlesList.contains("key3=value3"));
    }

    /**
     * Verifies that the Android Volley library integration works as expected
     * for the {@link HttpClientDelegate} authorize method.
     * 
     * <pre>
     * 
     * 1. Verify that the mock web server is up and running.
     * 
     * 2. Make a call to the network helpers authorize method.
     * 
     * 3. Verify that the call went through the Volley integration and the
     *      result has the success flag set to true.
     * 
     * </pre>
     * 
     * @throws JSONException
     */
    public void testAuthorizeVolleyIntegrationReturnsCorrectJsonResponse() throws JSONException {
        assertEquals(true, mockWebServer.wasStarted());
        assertEquals(true, mockWebServer.isAlive());

        JSONObject mockResponse = new JSONObject();
        mockResponse.put("value", "test");
        mockWebServer.mock_setResponse(mockResponse);

        RestResult result = target.authorize(Uri.parse("http://localhost:8080"), itemParser);
        assertNotNull(result);
        assertEquals(true, result.isSuccess());

        Method servedMethod = mockWebServer.mock_getRequestMethod();
        assertEquals(Method.POST, servedMethod);
    }

    /**
     * Verifies that a delete operation request won't throw an exception if the
     * delegate doesn't have an assigned item parser.
     * 
     * <pre>
     * 
     * 1. Create a new {@link SQLiteClientDelegate} object.
     * 
     * 2. Make sure it has a null pointer {@link BasicPodioProvider} assigned to it.
     * 
     * 3. Request a delete operation.
     * 
     * 4. Verify that a {@link InvalidParserException} has been thrown.
     * 
     * </pre>
     */
    public void testDeleteThrowExceptionOnNullPointerParser() {
    	try {
    		target.delete(Uri.parse("http://localhost:8080"), null);
    		fail("Should have thrown exception");
    	} catch (NullPointerException e) {
    	}
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
    	try {
    		target.delete(Uri.EMPTY, itemParser);
    		fail("Should have thrown exception");
    	} catch (IllegalArgumentException e) {
    	}
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
    	try {    	
    		target.delete(null, itemParser);
    		fail("Should have thrown exception");
		} catch (IllegalArgumentException e) {
		}
    }

    /**
     * Verifies that the delete request automatically and silently refreshes an
     * expired access token.
     * 
     * <pre>
     * 
     * 1. Verify that the mock web server is up and running.
     * 
     * 2. Revoke a custom session in the network helper with an expired
     *      access token.
     * 
     * 3. Make a call to the {@link HttpClientDelegate} delete method.
     * 
     * 4. Verify that the result contains a different session.
     * 
     * </pre>
     */
    public void testDeleteRefreshesExpiredToken() throws JSONException {
        assertEquals(true, mockWebServer.wasStarted());
        assertEquals(true, mockWebServer.isAlive());

        String mockNewSessionJsonString = "{ access_token:\"newaccesstoken\", refresh_token:\"newrefreshtoken\", expires_in:3600 }";
        JSONObject mockNewSessionJsonObject = new JSONObject(mockNewSessionJsonString);
        mockWebServer.mock_setSession(mockNewSessionJsonObject);

        Session mockExpiredSession = new Session("expiredaccesstoken", "stillvalidrefreshtoken",
                -3600000L);
        target.restoreSession("http://localhost:8080/auth/token", mockExpiredSession);

        Uri uri = Uri.parse("http://localhost:8080");
        RestResult result = target.delete(uri, itemParser);

        boolean isNewSessionValid = result.session().expiresMillis > System.currentTimeMillis();
        assertEquals(true, isNewSessionValid);
    }

    /**
     * Verifies that the Android Volley library integration works as expected
     * for the {@link HttpClientDelegate} delete method.
     * 
     * <pre>
     * 
     * 1. Verify that the mock web server is up and running.
     * 
     * 2. Make a call to the HttpClientDelegate delete method.
     * 
     * 3. Verify that the call went through the Volley integration and the
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
        mockWebServer.mock_setResponse(mockResponse);

        RestResult result = target.delete(Uri.parse("http://localhost:8080"), itemParser);
        assertNotNull(result);
        assertEquals(true, result.isSuccess());

        Method servedMethod = mockWebServer.mock_getRequestMethod();
        assertEquals(Method.DELETE, servedMethod);
    }

    /**
     * Verifies that a get operation request will throw an exception if the
     * delegate doesn't have an assigned item parser.
     * 
     * <pre>
     * 
     * 1. Create a new {@link SQLiteClientDelegate} object.
     * 
     * 2. Make sure it has a null pointer {@link BasicPodioProvider} assigned to it.
     * 
     * 3. Request a get operation.
     * 
     * 4. Verify that a {@link InvalidParserException} has been thrown.
     * 
     * </pre>
     */
    public void testGetDoesThrowExceptionOnNullPointerParser() {
        try {
            target.get(Uri.parse("http://localhost:8080"), null);
            fail("Should have thrown exception");
        } catch (NullPointerException e) {
        }
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
		try {
			target.get(Uri.EMPTY, itemParser);
			fail("Should have thrown exception");
		} catch (IllegalArgumentException e) {
		}
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
    	try {    	
	        target.get(null, itemParser);
			fail("Should have thrown exception");
		} catch (IllegalArgumentException e) {
		}
    }

    /**
     * Verifies that the get request automatically and silently refreshes an
     * expired access token.
     * 
     * <pre>
     * 
     * 1. Verify that the mock web server is up and running.
     * 
     * 2. Revoke a custom session in the network helper with an expired
     *      access token.
     * 
     * 3. Make a call to the {@link HttpClientDelegate} get method.
     * 
     * 4. Verify that the result contains a different session.
     * 
     * </pre>
     */
    public void testGetRefreshesExpiredToken() throws JSONException {
        assertEquals(true, mockWebServer.wasStarted());
        assertEquals(true, mockWebServer.isAlive());

        String mockNewSessionJsonString = "{ access_token:\"newaccesstoken\", refresh_token:\"newrefreshtoken\", expires_in:3600 }";
        JSONObject mockNewSessionJsonObject = new JSONObject(mockNewSessionJsonString);
        mockWebServer.mock_setSession(mockNewSessionJsonObject);

        Session mockExpiredSession = new Session("expiredaccesstoken", "stillvalidrefreshtoken",
                -3600000L);
        target.restoreSession("http://localhost:8080/auth/token", mockExpiredSession);

        Uri uri = Uri.parse("http://localhost:8080");
        RestResult result = target.get(uri, itemParser);

        boolean isNewSessionValid = result.session().expiresMillis > System.currentTimeMillis();
        assertEquals(true, isNewSessionValid);
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
        mockWebServer.mock_setResponse(mockResponse);

        RestResult result = target.get(uri, itemParser);
        assertNotNull(result);
        assertNotNull(result.item());
        assertEquals(true, result.isSuccess());

        MockContentItem item = (MockContentItem) result.item();
        PodioParser<MockContentItem> parser = PodioParser.fromClass(MockContentItem.class);
        String fetchedJson = parser.parseToJson(item);
        String mockedJson = mockResponse.toString();
        assertEquals(mockedJson, fetchedJson);

        Method servedMethod = mockWebServer.mock_getRequestMethod();
        assertEquals(Method.GET, servedMethod);
    }

    /**
     * Verifies that the given {@link PodioParser} is used for parsing items to
     * JSON when one is set.
     * 
     * <pre>
     * 
     * 1. Verify that the mock web server is up and running.
     * 
     * 2. Set a mock ItemParser to the target {@link HttpClientDelegate} that
     *      delivers a known fake JSON.
     * 
     * 3. Perform a post request on the test delegate.
     * 
     * 4. Verify that the mock web server received the fake JSON object
     *      produced by the explicitly set mock ItemToJsonParser.
     * 
     * </pre>
     */
    public void testNewItemParserIsUsedForParsingToJsonWhenExplicitlySet() throws JSONException {
        assertEquals(true, mockWebServer.wasStarted());
        assertEquals(true, mockWebServer.isAlive());

        JSONObject mockResponse = new JSONObject();
        mockWebServer.mock_setResponse(mockResponse);

        Uri uri = Uri.parse("http://localhost:8080");
        String fakeJsonString = "{item: fake}";
        Object fakeItemObject = new Object();

        target.post(uri, new Object(), new MockItemParser(fakeJsonString, fakeItemObject));

        String requestBody = mockWebServer.mock_getRequestBody();
        assertEquals(fakeJsonString, requestBody);
    }

    /**
     * Verifies that the given {@link PodioParser} is used for parsing JSON to
     * domain objects when one is set.
     * 
     * <pre>
     * 
     * 1. Verify that the mock web server is up and running.
     * 
     * 2. Set a mock ItemParser to the target {@link HttpClientDelegate} that
     *      delivers a known fake domain object representation.
     * 
     * 3. Perform a get request on the test delegate.
     * 
     * 4. Verify that the returned {@link RestResult} contains the fake domain
     *      model object produced by the explicitly set mock JsonToItemParser.
     * 
     * </pre>
     */
    public void testNewItemParserIsUsedForParsintToItemWhenExplicitlySet() throws JSONException {
        assertEquals(true, mockWebServer.wasStarted());
        assertEquals(true, mockWebServer.isAlive());

        JSONObject mockResponse = new JSONObject();
        mockWebServer.mock_setResponse(mockResponse);

        Uri uri = Uri.parse("http://localhost:8080");
        String fakeJsonString = "{}";
        Object fakeDomainObject = new Object();

        RestResult result = target.get(uri, new MockItemParser(fakeJsonString, fakeDomainObject));

        assertNotNull(result);
        assertEquals(fakeDomainObject, result.item());
    }

    /**
     * Verifies that a post operation request will throw an exception if the
     * delegate doesn't have an assigned item parser.
     * 
     * <pre>
     * 
     * 1. Create a new {@link SQLiteClientDelegate} object.
     * 
     * 2. Make sure it has a null pointer {@link BasicPodioProvider} assigned to it.
     * 
     * 3. Request a post operation.
     * 
     * 4. Verify that a {@link InvalidParserException} has been thrown.
     * 
     * </pre>
     */
    public void testPostDoesThrowExceptionOnNullPointerParser() {
        try {
            target.post(Uri.parse("http://localhost:8080"), new Object(), null);
            fail("Should have thrown exception");
        } catch (NullPointerException e) {
        }
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
		try {
			target.post(Uri.EMPTY, null, itemParser);
			fail("Should have thrown exception");
		} catch (IllegalArgumentException e) {
		}
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
    	try {    	
	        target.post(null, null, itemParser);
			fail("Should have thrown exception");
		} catch (IllegalArgumentException e) {
		}
    }

    /**
     * Verifies that the post request automatically and silently refreshes an
     * expired access token.
     * 
     * <pre>
     * 
     * 1. Verify that the mock web server is up and running.
     * 
     * 2. Revoke a custom session in the network helper with an expired
     *      access token.
     * 
     * 3. Make a call to the {@link HttpClientDelegate} post method.
     * 
     * 4. Verify that the result contains a different session.
     * 
     * </pre>
     */
    public void testPostRefreshesExpiredToken() throws JSONException {
        assertEquals(true, mockWebServer.wasStarted());
        assertEquals(true, mockWebServer.isAlive());

        String mockNewSessionJsonString = "{ access_token:\"newaccesstoken\", refresh_token:\"newrefreshtoken\", expires_in:3600 }";
        JSONObject mockNewSessionJsonObject = new JSONObject(mockNewSessionJsonString);
        mockWebServer.mock_setSession(mockNewSessionJsonObject);

        Session mockExpiredSession = new Session("expiredaccesstoken", "stillvalidrefreshtoken",
                -3600000L);
        target.restoreSession("http://localhost:8080/auth/token", mockExpiredSession);

        Uri uri = Uri.parse("http://localhost:8080");
        RestResult result = target.post(uri, new Object(), itemParser);

        boolean isNewSessionValid = result.session().expiresMillis > System.currentTimeMillis();
        assertEquals(true, isNewSessionValid);
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
        RestResult result = target.post(uri, item, itemParser);
        assertNotNull(result);
        assertEquals(true, result.isSuccess());

        Method servedMethod = mockWebServer.mock_getRequestMethod();
        assertEquals(Method.POST, servedMethod);
    }

    /**
     * Verifies that a put operation request will throw an exception if the
     * delegate doesn't have an assigned item parser.
     * 
     * <pre>
     * 
     * 1. Create a new {@link SQLiteClientDelegate} object.
     * 
     * 2. Make sure it has a null pointer {@link BasicPodioProvider} assigned to it.
     * 
     * 3. Request a put operation.
     * 
     * 4. Verify that a {@link InvalidParserException} has been thrown.
     * 
     * </pre>
     */
    public void testPutDoesThrowExceptionOnNullPointerParser() {
        try {
            target.put(Uri.parse("http://localhost:8080"), new Object(), null);
            fail("Should have thrown exception");
        } catch (NullPointerException e) {
        }
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
        try {
        	target.put(Uri.EMPTY, null, itemParser);
        	fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
        }
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
    	try {    	
    		target.put(null, null, itemParser);
    		fail("Should have thrown exception");
    	} catch (IllegalArgumentException e) {
    	}
    }

    /**
     * Verifies that the put request automatically and silently refreshes an
     * expired access token.
     * 
     * <pre>
     * 
     * 1. Verify that the mock web server is up and running.
     * 
     * 2. Revoke a custom session in the network helper with an expired
     *      access token.
     * 
     * 3. Make a call to the {@link HttpClientDelegate} put method.
     * 
     * 4. Verify that the result contains a different session.
     * 
     * </pre>
     */
    public void testPutRefreshesExpiredToken() throws JSONException {
        assertEquals(true, mockWebServer.wasStarted());
        assertEquals(true, mockWebServer.isAlive());

        String mockNewSessionJsonString = "{ access_token:\"newaccesstoken\", refresh_token:\"newrefreshtoken\", expires_in:3600 }";
        JSONObject mockNewSessionJsonObject = new JSONObject(mockNewSessionJsonString);
        mockWebServer.mock_setSession(mockNewSessionJsonObject);

        Session mockExpiredSession = new Session("expiredaccesstoken", "stillvalidrefreshtoken",
                -3600000L);
        target.restoreSession("http://localhost:8080/auth/token", mockExpiredSession);

        Uri uri = Uri.parse("http://localhost:8080");
        RestResult result = target.put(uri, new Object(), itemParser);

        boolean isNewSessionValid = result.session().expiresMillis > System.currentTimeMillis();
        assertEquals(true, isNewSessionValid);
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
        RestResult result = target.put(uri, item, itemParser);
        assertNotNull(result);
        assertEquals(true, result.isSuccess());

        Method servedMethod = mockWebServer.mock_getRequestMethod();
        assertEquals(Method.PUT, servedMethod);
    }
}
