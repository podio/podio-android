package com.podio.sdk.client;

import android.app.Instrumentation;
import android.content.Context;
import android.net.Uri;
import android.test.InstrumentationTestCase;

import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.domain.ItemFilter;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.test.TestUtils;

public class SQLiteRestClientTest extends InstrumentationTestCase {

    private static final class ConcurrentResult {
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

        target = new SQLiteRestClient(context, "authority");
        target.setDatabaseDelegate(new RestClientDelegate() {

            @Override
            public RestResult delete(Uri uri) {
                result.isDeleteCalled = true;
                return null;
            }

            @Override
            public RestResult get(Uri uri, Class<?> classOfResult) {
                result.isQueryCalled = true;
                return null;
            }

            @Override
            public RestResult post(Uri uri, Object item, Class<?> classOfItem) {
                result.isInsertCalled = true;
                return null;
            }

            @Override
            public RestResult put(Uri uri, Object item, Class<?> classOfItem) {
                result.isUpdateCalled = true;
                return null;
            }

        });
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
                .setFilter(new ItemFilter()) //
                .setOperation(RestOperation.DELETE);

        target.perform(restRequest);
        TestUtils.blockThread(20);

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
                .setFilter(new ItemFilter()) //
                .setOperation(RestOperation.GET);

        target.perform(restRequest);
        TestUtils.blockThread(20);

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
                .setFilter(new ItemFilter()) //
                .setOperation(RestOperation.POST);

        target.perform(restRequest);
        TestUtils.blockThread(20);

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
                .setFilter(new ItemFilter()) //
                .setOperation(RestOperation.PUT);

        target.perform(restRequest);
        TestUtils.blockThread(20);

        assertEquals(false, result.isDeleteCalled);
        assertEquals(false, result.isInsertCalled);
        assertEquals(false, result.isQueryCalled);
        assertEquals(true, result.isUpdateCalled);
    }
}
