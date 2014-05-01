package com.podio.sdk.client;

import android.app.Instrumentation;
import android.content.Context;
import android.net.Uri;
import android.test.InstrumentationTestCase;

import com.podio.sdk.client.delegate.JsonClientDelegate;
import com.podio.sdk.domain.PodioFilter;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.test.TestUtils;

public class SQLiteRestClientTest extends InstrumentationTestCase {

    private static final class ConcurrentResult {
        private boolean isAuthorizeCalled;
        private boolean isDeleteCalled;
        private boolean isInsertCalled;
        private boolean isQueryCalled;
        private boolean isUpdateCalled;
    }

    private SQLiteRestClient target;
    private ConcurrentResult result;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Instrumentation instrumentation = getInstrumentation();
        Context context = instrumentation.getContext();

        result = new ConcurrentResult();
        target = new SQLiteRestClient(context, "authority", new JsonClientDelegate() {

            @Override
            public RestResult authorize(Uri uri) {
                result.isAuthorizeCalled = true;
                return null;
            }

            @Override
            public RestResult delete(Uri uri) {
                result.isDeleteCalled = true;
                return null;
            }

            @Override
            public RestResult get(Uri uri) {
                result.isQueryCalled = true;
                return null;
            }

            @Override
            public RestResult post(Uri uri, Object item) {
                result.isInsertCalled = true;
                return null;
            }

            @Override
            public RestResult put(Uri uri, Object item) {
                result.isUpdateCalled = true;
                return null;
            }

        }, 10);
    }

    /**
     * Verifies that a authorize rest operation is delegated properly to the
     * {@link DatabaseClientDelegate}.
     * 
     * <pre>
     * 
     * 1. Create a new {@link SQLiteRestClient} and add a mock
     *      {@link DatabaseClientDelegate} to it.
     * 
     * 2. Push a authorize operation to the client.
     * 
     * 3. Verify that the authorize method of the database helper
     *      is called.
     * 
     * </pre>
     */
    public void testAuthorizeOperationIsDelegatedCorrectly() {
        RestRequest restRequest = new RestRequest() //
                .setFilter(new PodioFilter()) //
                .setOperation(RestOperation.AUTHORIZE);

        target.enqueue(restRequest);
        TestUtils.blockThread(20);

        assertEquals(true, result.isAuthorizeCalled);
        assertEquals(false, result.isDeleteCalled);
        assertEquals(false, result.isInsertCalled);
        assertEquals(false, result.isQueryCalled);
        assertEquals(false, result.isUpdateCalled);
    }

    /**
     * Verifies that an IllegalArgumentException is thrown when trying to create
     * an SQLiteRestClient with a null pointer delegate.
     * 
     * <pre>
     * 
     * 1. Create a new SQLiteRestClient with a null pointer database delegate.
     * 
     * 2. Verify that an IllegalArgumentException was thrown.
     * 
     * </pre>
     */
    public void testConstructorThrowsIllegalArgumentExceptionOnInvalidDelegates() {
        try {
            new SQLiteRestClient(null, null, null, 0);
            boolean didReachThisPoint = true;
            assertFalse(didReachThisPoint);
        } catch (IllegalArgumentException e) {
            boolean didThrowException = true;
            assertTrue(didThrowException);
        }
    }

    /**
     * Verifies that a delete rest operation is delegated correctly to the
     * {@link DatabaseClientDelegate}.
     * 
     * <pre>
     * 
     * 1. Create a new {@link SQLiteRestClient} and add a mock
     *      {@link DatabaseClientDelegate} to it.
     * 
     * 2. Push a delete operation to the client.
     * 
     * 3. Verify that the delete method of the database helper
     *      is called.
     * 
     * </pre>
     */
    public void testDeleteOperationIsDelegatedCorrectly() {
        RestRequest restRequest = new RestRequest() //
                .setFilter(new PodioFilter()) //
                .setOperation(RestOperation.DELETE);

        target.enqueue(restRequest);
        TestUtils.blockThread(20);

        assertEquals(false, result.isAuthorizeCalled);
        assertEquals(true, result.isDeleteCalled);
        assertEquals(false, result.isInsertCalled);
        assertEquals(false, result.isQueryCalled);
        assertEquals(false, result.isUpdateCalled);
    }

    /**
     * Verifies that a get rest operation is delegated correctly to the
     * {@link DatabaseClientDelegate}.
     * 
     * <pre>
     * 
     * 1. Create a new {@link SQLiteRestClient} and add a mock
     *      {@link DatabaseClientDelegate} to it.
     * 
     * 2. Push a get operation to the client.
     * 
     * 3. Verify that the query method of the database helper
     *      is called.
     * 
     * </pre>
     */
    public void testGetOperationIsDelegatedCorrectly() {
        RestRequest restRequest = new RestRequest() //
                .setFilter(new PodioFilter()) //
                .setOperation(RestOperation.GET);

        target.enqueue(restRequest);
        TestUtils.blockThread(20);

        assertEquals(false, result.isAuthorizeCalled);
        assertEquals(false, result.isDeleteCalled);
        assertEquals(false, result.isInsertCalled);
        assertEquals(true, result.isQueryCalled);
        assertEquals(false, result.isUpdateCalled);
    }

    /**
     * Verifies that a post rest operation is delegated correctly to the
     * {@link DatabaseClientDelegate}.
     * 
     * <pre>
     * 
     * 1. Create a new {@link SQLiteRestClient} and add a mock
     *      {@link DatabaseClientDelegate} to it.
     * 
     * 2. Push a post operation to the client.
     * 
     * 3. Verify that the insert method of the database helper
     *      is called.
     * 
     * </pre>
     */
    public void testPostOperationIsDelegatedCorrectly() {
        RestRequest restRequest = new RestRequest() //
                .setFilter(new PodioFilter()) //
                .setOperation(RestOperation.POST);

        target.enqueue(restRequest);
        TestUtils.blockThread(20);

        assertEquals(false, result.isAuthorizeCalled);
        assertEquals(false, result.isDeleteCalled);
        assertEquals(true, result.isInsertCalled);
        assertEquals(false, result.isQueryCalled);
        assertEquals(false, result.isUpdateCalled);
    }

    /**
     * Verifies that a put rest operation is delegated correctly to the
     * {@link DatabaseClientDelegate}.
     * 
     * <pre>
     * 
     * 1. Create a new {@link SQLiteRestClient} and add a mock
     *      {@link DatabaseClientDelegate} to it.
     * 
     * 2. Push a put operation to the client.
     * 
     * 3. Verify that the update method of the database helper
     *      is called.
     * 
     * </pre>
     */
    public void testPutOperationIsDelegatedCorrectly() {
        RestRequest restRequest = new RestRequest() //
                .setFilter(new PodioFilter()) //
                .setOperation(RestOperation.PUT);

        target.enqueue(restRequest);
        TestUtils.blockThread(20);

        assertEquals(false, result.isAuthorizeCalled);
        assertEquals(false, result.isDeleteCalled);
        assertEquals(false, result.isInsertCalled);
        assertEquals(false, result.isQueryCalled);
        assertEquals(true, result.isUpdateCalled);
    }
}
