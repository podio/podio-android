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

package com.podio.sdk.client;

import android.app.Instrumentation;
import android.content.Context;
import android.net.Uri;

import com.podio.sdk.client.delegate.mock.MockRestClientDelegate;
import com.podio.sdk.filter.BasicPodioFilter;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.test.TestUtils;
import com.podio.test.ThreadedTestCase;

public class CachedRestClientTest extends ThreadedTestCase {

    private static final Uri REFERENCE_CONTENT_URI = Uri //
            .parse("content://test.authority/path");

    private static final Uri REFERENCE_NETWORK_URI = Uri //
            .parse("https://test.authority/path");

    private MockRestClientDelegate targetDatabaseDelegate;
    private MockRestClientDelegate targetNetworkDelegate;
    private CachedRestClient targetRestClient;

    private int actualReportCount;
    private int expectedReportCount;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Instrumentation instrumentation = getInstrumentation();
        Context context = instrumentation.getContext();

        actualReportCount = 0;
        expectedReportCount = 1;

        targetDatabaseDelegate = new MockRestClientDelegate();
        targetNetworkDelegate = new MockRestClientDelegate();

        targetRestClient = new CachedRestClient(context, "test.authority", targetNetworkDelegate,
                targetDatabaseDelegate, 10) {
            @Override
			protected <T> void reportResult(RestRequest<T> request, RestResult<T> result) {

                // This call is still running on the worker thread. The super
                // implementation (QueuedRestClient) will make sure that the
                // given result listener is called on the main thread, but since
                // that thread is blocked in the test, this is our last chance
                // to manually release the blockades.

                actualReportCount++;
                if (actualReportCount >= expectedReportCount) {
                    TestUtils.completed();
                }

                // The last step in the reporting flow (the one that is executed
                // on the main thread) is intentionally ignored for now.
            }
        };
    }

    /**
     * Verifies that the expected Uri is delegated to the
     * {@link MockHttpClientDelegate}, while there is no Uri delegated at all to
     * the {@link MockDatabaseClientDelegate} when performing a authorize
     * request.
     * 
     * <pre>
     * 
     * 2. Add a mocked {@link DatabaseClientDelegate} to the client.
     * 
     * 3. Add a mocked {@link NetworkClientDelegate} to the client.
     * 
     * 4. Perform a "authorize" request.
     * 
     * 5. Verify that there is no Uri delegated to the mocked database
     *      delegate.
     * 
     * 6. Verify that the expected Uri is delegated to the mocked network
     *      delegate.
     * 
     * </pre>
     */
    public void testAuthorizeRequestDelegatesCorrectUri() {
        RestRequest<Object> request = buildRestRequest(RestOperation.AUTHORIZE, "path");
        targetRestClient.enqueue(request);

        assertTrue(TestUtils.waitUntilCompletion());

        assertNull(targetDatabaseDelegate.getUri(RestOperation.AUTHORIZE));
        assertEquals(REFERENCE_NETWORK_URI, targetNetworkDelegate.getUri(RestOperation.AUTHORIZE));
    }

    /**
     * Verifies that only the network delegate is triggered when a "authorize"
     * rest request is called.
     * 
     * <pre>
     * 
     * 1. Create a new CachedRestClient.
     * 
     * 2. Add a mocked {@link DatabaseClientDelegate} to the client.
     * 
     * 3. Add a mocked {@link NetworkClientDelegate} to the client.
     * 
     * 4. Perform a "authorize" request.
     * 
     * 5. Verify that the mocked database delegate is not triggered.
     * 
     * 6. Verify that the mocked network delegate is triggered.
     * 
     * </pre>
     */
    public void testAuthorizeRequestTriggersOnlyNetworkDelegate() {
        RestRequest<Object> request = buildRestRequest(RestOperation.AUTHORIZE);
        targetRestClient.enqueue(request);

        assertTrue(TestUtils.waitUntilCompletion());

        assertEquals(1, targetNetworkDelegate.getCalls(RestOperation.AUTHORIZE));
        assertEquals(0, targetDatabaseDelegate.getCalls(RestOperation.AUTHORIZE));
    }

    /**
     * Verifies that an IllegalArgumentException is thrown when trying to create
     * a CachedRestClient with null pointer delegates.
     * 
     * <pre>
     * 
     * 1. Create a new CachedRestClient with a null pointer network delegate.
     * 
     * 2. Verify that an IllegalArgumentException was thrown.
     * 
     * 3. Create a new CachedRestClient with a null pointer cache delegate.
     * 
     * 4. Verify that an IllegalArgumentException was thrown.
     * 
     * </pre>
     */
    public void testConstructorThrowsIllegalArgumentExceptionOnInvalidDelegates() {
        // Verify exception for network delegate.
        try {
            new CachedRestClient(null, null, null, new MockRestClientDelegate(), 0);
            fail("Should have thrown exception");
        } catch (NullPointerException e) {
        }

        // Verify exception for cache delegate.
        try {
            new CachedRestClient(null, null, new MockRestClientDelegate(), null, 0);
            fail("Should have thrown exception");
        } catch (NullPointerException e) {
        }
    }

    /**
     * Verifies that the expected Uri delegated to the
     * {@link MockHttpClientDelegate}, while there is no Uri delegated at all to
     * the {@link MockDatabaseClientDelegate} when performing a delete request.
     * 
     * <pre>
     * 
     * 2. Add a mocked {@link DatabaseClientDelegate} to the client.
     * 
     * 3. Add a mocked {@link NetworkClientDelegate} to the client.
     * 
     * 4. Perform a "delete" request.
     * 
     * 5. Verify that there is no Uri delegated to the mocked database
     *      delegate.
     * 
     * 6. Verify that the expected Uri is delegated to the mocked network
     *      delegate.
     * 
     * </pre>
     */
    public void testDeleteRequestDelegatesCorrectUri() {
        RestRequest<Object> request = buildRestRequest(RestOperation.DELETE, "path");
        targetRestClient.enqueue(request);

        assertTrue(TestUtils.waitUntilCompletion());

        assertEquals(targetDatabaseDelegate.getCalls(RestOperation.AUTHORIZE), 0); //TODO: Is this really correct?
        assertEquals(REFERENCE_NETWORK_URI, targetNetworkDelegate.getUri(RestOperation.DELETE));
    }

    /**
     * Verifies that only the network delegate is triggered when a "delete" rest
     * request is called.
     * 
     * <pre>
     * 
     * 1. Create a new CachedRestClient.
     * 
     * 2. Add a mocked {@link DatabaseClientDelegate} to the client.
     * 
     * 3. Add a mocked {@link NetworkClientDelegate} to the client.
     * 
     * 4. Perform a "delete" request.
     * 
     * 5. Verify that the mocked database delegate is not triggered.
     * 
     * 6. Verify that the mocked network delegate is triggered.
     * 
     * </pre>
     */
    public void testDeleteRequestTriggersOnlyNetworkDelegate() {
        RestRequest<Object> request = buildRestRequest(RestOperation.DELETE);
        targetRestClient.enqueue(request);

        assertTrue(TestUtils.waitUntilCompletion());

        assertEquals(1, targetNetworkDelegate.getCalls(RestOperation.DELETE));
        assertEquals(1, targetDatabaseDelegate.getCalls(RestOperation.DELETE));
        assertEquals(1, targetDatabaseDelegate.getCalls(RestOperation.GET));
    }

    /**
     * Verifies that the expected Uri is delegated to both the
     * {@link MockHttpClientDelegate}, and the
     * {@link MockDatabaseClientDelegate} when performing a get request.
     * 
     * <pre>
     * 
     * 2. Add a mocked {@link DatabaseClientDelegate} to the client.
     * 
     * 3. Add a mocked {@link NetworkClientDelegate} to the client.
     * 
     * 4. Perform a "get" request.
     * 
     * 5. Verify that the expected Uri is delegated to the mocked database
     *      delegate.
     * 
     * 6. Verify that the expected Uri is delegated to the mocked network
     *      delegate.
     * 
     * </pre>
     */
    public void testGetRequestDelegatesCorrectUri() {        
        expectedReportCount = 2;
        
        RestRequest<Object> request = buildRestRequest(RestOperation.GET, "path");
        targetRestClient.enqueue(request);

        assertTrue(TestUtils.waitUntilCompletion());

        assertEquals(REFERENCE_CONTENT_URI, targetDatabaseDelegate.getUri(RestOperation.GET));
        assertEquals(REFERENCE_NETWORK_URI, targetNetworkDelegate.getUri(RestOperation.GET));
    }

    /**
     * Verifies that both the database delegate and the network delegate are
     * triggered when a "get" rest request is called.
     * 
     * <pre>
     * 
     * 1. Create a new CachedRestClient.
     * 
     * 2. Add a mocked {@link DatabaseClientDelegate} to the client.
     * 
     * 3. Add a mocked {@link NetworkClientDelegate} to the client.
     * 
     * 4. Perform a "get" request.
     * 
     * 5. Verify that the mocked database delegate is triggered.
     * 
     * 6. Verify that the mocked network delegate is triggered.
     * 
     * </pre>
     */
    public void testGetRequestTriggersBothClientDelegates() {
        expectedReportCount = 2;

        RestRequest<Object> request = buildRestRequest(RestOperation.GET);
        targetRestClient.enqueue(request);

        assertTrue(TestUtils.waitUntilCompletion());

        assertEquals(2, targetDatabaseDelegate.getCalls(RestOperation.GET));
        assertEquals(1, targetNetworkDelegate.getCalls(RestOperation.GET));
        assertEquals(1, targetDatabaseDelegate.getCalls(RestOperation.POST));
    }

    /**
     * Verifies that the expected Uri is delegated to both the
     * {@link MockHttpClientDelegate}, and the
     * {@link MockDatabaseClientDelegate} when performing a post request.
     * 
     * <pre>
     * 
     * 2. Add a mocked {@link DatabaseClientDelegate} to the client.
     * 
     * 3. Add a mocked {@link NetworkClientDelegate} to the client.
     * 
     * 4. Perform a "post" request.
     * 
     * 5. Verify that the expected Uri is delegated to the mocked database
     *      delegate.
     * 
     * 6. Verify that the expected Uri is delegated to the mocked network
     *      delegate.
     * 
     * </pre>
     */
    public void testPostRequestDelegatesCorrectUri() {
        expectedReportCount = 2;
        
        RestRequest<Object> request = buildRestRequest(RestOperation.POST, "path");
        targetRestClient.enqueue(request);

        assertTrue(TestUtils.waitUntilCompletion());

        assertEquals(REFERENCE_CONTENT_URI, targetDatabaseDelegate.getUri(RestOperation.POST));
        assertEquals(REFERENCE_NETWORK_URI, targetNetworkDelegate.getUri(RestOperation.POST));
    }

    /**
     * Verifies that both the database delegate and the network delegate are
     * triggered when a "post" rest request is called.
     * 
     * <pre>
     * 
     * 1. Create a new CachedRestClient.
     * 
     * 2. Add a mocked {@link DatabaseClientDelegate} to the client.
     * 
     * 3. Add a mocked {@link NetworkClientDelegate} to the client.
     * 
     * 4. Perform a "post" request.
     * 
     * 5. Verify that the mocked database delegate is triggered.
     * 
     * 6. Verify that the mocked network delegate is triggered.
     * 
     * </pre>
     */
    public void testPostRequestTriggersBothClientDelegates() {
        expectedReportCount = 2;
        
        RestRequest<Object> request = buildRestRequest(RestOperation.POST);
        targetRestClient.enqueue(request);
        
        assertTrue(TestUtils.waitUntilCompletion());

        assertEquals(1, targetNetworkDelegate.getCalls(RestOperation.POST));
        assertEquals(2, targetDatabaseDelegate.getCalls(RestOperation.POST));
    }

    /**
     * Verifies that the expected Uri delegated to the
     * {@link MockHttpClientDelegate}, while there is no Uri delegated at all to
     * the {@link MockDatabaseClientDelegate} when performing a put request.
     * 
     * <pre>
     * 
     * 2. Add a mocked {@link DatabaseClientDelegate} to the client.
     * 
     * 3. Add a mocked {@link NetworkClientDelegate} to the client.
     * 
     * 4. Perform a "put" request.
     * 
     * 5. Verify that there is no Uri delegated to the mocked database
     *      delegate.
     * 
     * 6. Verify that the expected Uri is delegated to the mocked network
     *      delegate.
     * 
     * </pre>
     */
    public void testPutRequestDelegatesCorrectUri() {
        RestRequest<Object> request = buildRestRequest(RestOperation.PUT, "path");
        targetRestClient.enqueue(request);

        assertTrue(TestUtils.waitUntilCompletion());

        assertEquals(REFERENCE_CONTENT_URI, targetDatabaseDelegate.getUri(RestOperation.POST));
        assertEquals(REFERENCE_NETWORK_URI, targetNetworkDelegate.getUri(RestOperation.PUT));
    }

    /**
     * Verifies that only the network delegate is triggered when a "put" rest
     * request is called.
     * 
     * <pre>
     * 
     * 1. Create a new CachedRestClient.
     * 
     * 2. Add a mocked {@link DatabaseClientDelegate} to the client.
     * 
     * 3. Add a mocked {@link NetworkClientDelegate} to the client.
     * 
     * 4. Perform a "put" request.
     * 
     * 5. Verify that the mocked database delegate is not triggered.
     * 
     * 6. Verify that the mocked network delegate is triggered.
     * 
     * </pre>
     */
    public void testPutRequestTriggersOnlyNetworkDelegate() {
        RestRequest<Object> request = buildRestRequest(RestOperation.PUT);
        targetRestClient.enqueue(request);

        assertTrue(TestUtils.waitUntilCompletion());

        assertEquals(1, targetNetworkDelegate.getCalls(RestOperation.PUT));
        assertEquals(1, targetDatabaseDelegate.getCalls(RestOperation.POST));
    }

    private RestRequest<Object> buildRestRequest(RestOperation operation) {
        return buildRestRequest(operation, "test");
    }

    private RestRequest<Object> buildRestRequest(RestOperation operation, String path) {
        BasicPodioFilter filter = new BasicPodioFilter(path);
		
        return new RestRequest<Object>() //
                .setContent(new Object()) //
                .setOperation(operation) //
                .setResultListener(null) //
                .setFilter(filter) //
                .setTicket(filter);
    }

}
