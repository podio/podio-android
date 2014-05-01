package com.podio.sdk.client;

import android.app.Instrumentation;
import android.content.Context;
import android.net.Uri;
import android.test.InstrumentationTestCase;

import com.podio.sdk.Filter;
import com.podio.sdk.client.delegate.mock.MockRestClientDelegate;
import com.podio.sdk.domain.PodioFilter;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.internal.request.ResultListener;
import com.podio.test.TestUtils;

public class CachedRestClientTest extends InstrumentationTestCase {

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
            protected void reportResult(Object ticket, ResultListener resultListener,
                    RestResult result) {

                // This call is still running on the worker thread. The super
                // implementation (QueuedRestClient) will make sure that the
                // given result listener is called on the main thread, but since
                // that thread is blocked in the test, this is our last chance
                // to manually release the blockades.

                actualReportCount++;
                if (actualReportCount >= expectedReportCount) {
                    TestUtils.releaseBlockedThread();
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
        RestRequest request = buildRestRequest(RestOperation.AUTHORIZE, "path");
        targetRestClient.enqueue(request);

        TestUtils.blockThread();

        assertNull(targetDatabaseDelegate.mock_getAuthorizeUri());
        assertEquals(REFERENCE_NETWORK_URI, targetNetworkDelegate.mock_getAuthorizeUri());
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
        targetNetworkDelegate.mock_setMockAuthorizeResult(new RestResult(true, null, null));
        targetDatabaseDelegate.mock_setMockAuthorizeResult(new RestResult(true, null, null));

        RestRequest request = buildRestRequest(RestOperation.AUTHORIZE);
        targetRestClient.enqueue(request);

        TestUtils.blockThread();

        assertEquals(1, targetNetworkDelegate.mock_getAuthorizeCallCount());
        assertEquals(0, targetDatabaseDelegate.mock_getAuthorizeCallCount());
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
            boolean didReachThisPoint = true;
            assertFalse(didReachThisPoint);
        } catch (IllegalArgumentException e) {
            boolean didThrowException = true;
            assertTrue(didThrowException);
        }

        // Verify exception for cache delegate.
        try {
            new CachedRestClient(null, null, new MockRestClientDelegate(), null, 0);
            boolean didReachThisPoint = true;
            assertFalse(didReachThisPoint);
        } catch (IllegalArgumentException e) {
            boolean didThrowException = true;
            assertTrue(didThrowException);
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
        RestRequest request = buildRestRequest(RestOperation.DELETE, "path");
        targetRestClient.enqueue(request);

        TestUtils.blockThread();

        assertNull(targetDatabaseDelegate.mock_getDeleteUri());
        assertEquals(REFERENCE_NETWORK_URI, targetNetworkDelegate.mock_getDeleteUri());
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
        targetNetworkDelegate.mock_setMockDeleteResult(new RestResult(true, null, null));
        targetDatabaseDelegate.mock_setMockDeleteResult(new RestResult(true, null, null));
        targetDatabaseDelegate.mock_setMockGetResult(new RestResult(true, null, new Object()));

        RestRequest request = buildRestRequest(RestOperation.DELETE);
        targetRestClient.enqueue(request);

        TestUtils.blockThread();

        assertEquals(1, targetNetworkDelegate.mock_getDeleteCallCount());
        assertEquals(1, targetDatabaseDelegate.mock_getDeleteCallCount());
        assertEquals(1, targetDatabaseDelegate.mock_getGetCallCount());
    }

    /**
     * Verifies that the expected Uri delegated to both the
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
     * 5. Verify that the expected Uri delegated to the mocked database
     *      delegate.
     * 
     * 6. Verify that the expected Uri is delegated to the mocked network
     *      delegate.
     * 
     * </pre>
     */
    public void testGetRequestDelegatesCorrectUri() {
        RestRequest request = buildRestRequest(RestOperation.GET, "path");
        targetRestClient.enqueue(request);

        TestUtils.blockThread();

        assertEquals(REFERENCE_CONTENT_URI, targetDatabaseDelegate.mock_getGetUri());
        assertEquals(REFERENCE_NETWORK_URI, targetNetworkDelegate.mock_getGetUri());
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
        targetNetworkDelegate.mock_setMockGetResult(new RestResult(true, null, new Object()));
        targetDatabaseDelegate.mock_setMockPostResult(new RestResult(true, null, null));
        targetDatabaseDelegate.mock_setMockGetResult(new RestResult(true, null, new Object()));

        expectedReportCount = 4;

        RestRequest request = buildRestRequest(RestOperation.GET);
        targetRestClient.enqueue(request);

        TestUtils.blockThread();

        assertEquals(2, targetDatabaseDelegate.mock_getGetCallCount());
        assertEquals(1, targetNetworkDelegate.mock_getGetCallCount());
        assertEquals(1, targetDatabaseDelegate.mock_getPostCallCount());
    }

    /**
     * Verifies that the expected Uri delegated to the
     * {@link MockHttpClientDelegate}, while there is no Uri delegated at all to
     * the {@link MockDatabaseClientDelegate} when performing a post request.
     * 
     * <pre>
     * 
     * 2. Add a mocked {@link DatabaseClientDelegate} to the client.
     * 
     * 3. Add a mocked {@link NetworkClientDelegate} to the client.
     * 
     * 4. Perform a "post" request.
     * 
     * 5. Verify that there is no Uri delegated to the mocked database
     *      delegate.
     * 
     * 6. Verify that the expected Uri is delegated to the mocked network
     *      delegate.
     * 
     * </pre>
     */
    public void testPostRequestDelegatesCorrectUri() {
        RestRequest request = buildRestRequest(RestOperation.POST, "path");
        targetRestClient.enqueue(request);

        TestUtils.blockThread();

        assertNull(targetDatabaseDelegate.mock_getPostUri());
        assertEquals(REFERENCE_NETWORK_URI, targetNetworkDelegate.mock_getPostUri());
    }

    /**
     * Verifies that only the network delegate is triggered when a "post" rest
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
     * 4. Perform a "post" request.
     * 
     * 5. Verify that the mocked database delegate is not triggered.
     * 
     * 6. Verify that the mocked network delegate is triggered.
     * 
     * </pre>
     */
    public void testPostRequestTriggersOnlyNetworkDelegate() {
        targetNetworkDelegate.mock_setMockPostResult(new RestResult(true, null, null));
        targetDatabaseDelegate.mock_setMockPostResult(new RestResult(true, null, null));

        RestRequest request = buildRestRequest(RestOperation.POST);
        targetRestClient.enqueue(request);

        TestUtils.blockThread();

        assertEquals(1, targetNetworkDelegate.mock_getPostCallCount());
        assertEquals(1, targetDatabaseDelegate.mock_getPostCallCount());
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
        RestRequest request = buildRestRequest(RestOperation.PUT, "path");
        targetRestClient.enqueue(request);

        TestUtils.blockThread();

        assertNull(targetDatabaseDelegate.mock_getPutUri());
        assertEquals(REFERENCE_NETWORK_URI, targetNetworkDelegate.mock_getPutUri());
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
        targetNetworkDelegate.mock_setMockPutResult(new RestResult(true, null, null));
        targetDatabaseDelegate.mock_setMockPutResult(new RestResult(true, null, null));

        RestRequest request = buildRestRequest(RestOperation.PUT);
        targetRestClient.enqueue(request);

        TestUtils.blockThread();

        assertEquals(1, targetNetworkDelegate.mock_getPutCallCount());
        assertEquals(1, targetDatabaseDelegate.mock_getPutCallCount());
    }

    private RestRequest buildRestRequest(RestOperation operation) {
        return buildRestRequest(operation, "test");
    }

    private RestRequest buildRestRequest(RestOperation operation, String path) {
        Filter filter = path == null ? new PodioFilter() : new PodioFilter(path);

        RestRequest request = new RestRequest() //
                .setContent(new Object()) //
                .setOperation(operation) //
                .setResultListener(null) //
                .setFilter(filter) //
                .setTicket(filter);

        return request;
    }

}
