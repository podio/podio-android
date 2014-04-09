package com.podio.sdk.client;

import android.app.Instrumentation;
import android.content.Context;
import android.net.Uri;
import android.test.InstrumentationTestCase;

import com.podio.sdk.Filter;
import com.podio.sdk.client.database.DatabaseClientDelegate;
import com.podio.sdk.client.mock.MockAuthenticationDelegate;
import com.podio.sdk.client.mock.MockDatabaseClientDelegate;
import com.podio.sdk.client.mock.MockNetworkClientDelegate;
import com.podio.sdk.client.network.NetworkClientDelegate;
import com.podio.sdk.domain.ItemFilter;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.internal.request.ResultListener;
import com.podio.test.TestUtils;

public class CachedRestClientTest extends InstrumentationTestCase {

    private static final Uri REFERENCE_CONTENT_URI = Uri //
            .parse("content://test.authority/path");
    private static final Uri REFERENCE_NETWORK_URI = Uri //
            .parse("https://test.authority/path?oauth_token=" //
                    + MockAuthenticationDelegate.TEST_TOKEN);

    private MockDatabaseClientDelegate targetDatabaseDelegate;
    private MockNetworkClientDelegate targetNetworkDelegate;
    private CachedRestClient targetRestClient;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Instrumentation instrumentation = getInstrumentation();
        Context context = instrumentation.getContext();

        targetDatabaseDelegate = new MockDatabaseClientDelegate();
        targetNetworkDelegate = new MockNetworkClientDelegate();

        targetRestClient = new CachedRestClient(context, "test.authority") {
            @Override
            protected void reportResult(Object ticket, ResultListener resultListener,
                    RestResult result) {

                // This call is still run on the worker thread. The super
                // implementation will make sure that the given resultListener
                // is called from the parent thread, but in our case that thread
                // is blocked while we're waiting for worker thread to complete.

                TestUtils.releaseBlockedThread();
                super.reportResult(ticket, resultListener, result);
            }
        };
        targetRestClient.setDatabaseDelegate(targetDatabaseDelegate);
        targetRestClient.setNetworkDelegate(targetNetworkDelegate);
        targetRestClient.setAuthenticationDelegate(new MockAuthenticationDelegate());
    }

    /**
     * Verifies that the expected Uri delegated to the
     * {@link MockNetworkClientDelegate}, while there is no Uri delegated at all
     * to the {@link MockDatabaseClientDelegate} when performing a delete
     * request.
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
        targetRestClient.perform(request);

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
        RestRequest request = buildRestRequest(RestOperation.DELETE);
        targetRestClient.perform(request);

        TestUtils.blockThread();

        assertEquals(false, targetDatabaseDelegate.mock_isDeleteCalled());
        assertEquals(true, targetNetworkDelegate.mock_isDeleteCalled());
    }

    /**
     * Verifies that the expected Uri delegated to both the
     * {@link MockNetworkClientDelegate}, and the
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
        targetRestClient.perform(request);

        TestUtils.blockThread();

        assertEquals(REFERENCE_CONTENT_URI, targetDatabaseDelegate.mock_getQueryUri());
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
        RestRequest request = buildRestRequest(RestOperation.GET);
        targetRestClient.perform(request);

        TestUtils.blockThread();

        assertEquals(true, targetDatabaseDelegate.mock_isQueryCalled());
        assertEquals(true, targetNetworkDelegate.mock_isGetCalled());
    }

    /**
     * Verifies that the expected Uri delegated to the
     * {@link MockNetworkClientDelegate}, while there is no Uri delegated at all
     * to the {@link MockDatabaseClientDelegate} when performing a post request.
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
        targetRestClient.perform(request);

        TestUtils.blockThread();

        assertNull(targetDatabaseDelegate.mock_getInsertUri());
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
        RestRequest request = buildRestRequest(RestOperation.POST);
        targetRestClient.perform(request);

        TestUtils.blockThread();

        assertEquals(false, targetDatabaseDelegate.mock_isInsertCalled());
        assertEquals(true, targetNetworkDelegate.mock_isPostCalled());
    }

    /**
     * Verifies that the expected Uri delegated to the
     * {@link MockNetworkClientDelegate}, while there is no Uri delegated at all
     * to the {@link MockDatabaseClientDelegate} when performing a put request.
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
        targetRestClient.perform(request);

        TestUtils.blockThread();

        assertNull(targetDatabaseDelegate.mock_getUpdateUri());
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
        RestRequest request = buildRestRequest(RestOperation.PUT);
        targetRestClient.perform(request);

        TestUtils.blockThread();

        assertEquals(false, targetDatabaseDelegate.mock_isUpdateCalled());
        assertEquals(true, targetNetworkDelegate.mock_isPutCalled());
    }

    private RestRequest buildRestRequest(RestOperation operation) {
        return buildRestRequest(operation, "test");
    }

    private RestRequest buildRestRequest(RestOperation operation, String path) {
        Filter filter = path == null ? new ItemFilter() : new ItemFilter(path);

        RestRequest request = new RestRequest() //
                .setContent(new Object()) //
                .setItemType(Object.class) //
                .setOperation(operation) //
                .setResultListener(null) //
                .setFilter(filter) //
                .setTicket(filter);

        return request;
    }

}
