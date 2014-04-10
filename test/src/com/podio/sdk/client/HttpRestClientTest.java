package com.podio.sdk.client;

import android.app.Instrumentation;
import android.content.Context;
import android.net.Uri;
import android.test.InstrumentationTestCase;

import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.domain.ItemFilter;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.test.TestUtils;

public class HttpRestClientTest extends InstrumentationTestCase {

    private static final class ConcurrentResult {
        private boolean isDeleteCalled;
        private boolean isGetCalled;
        private boolean isPostCalled;
        private boolean isPutCalled;
    }

    private HttpRestClient target;
    private ConcurrentResult result;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Instrumentation instrumentation = getInstrumentation();
        Context context = instrumentation.getContext();

        result = new ConcurrentResult();

        target = new HttpRestClient(context, "authority");
        target.setNetworkDelegate(new RestClientDelegate() {

            @Override
            public RestResult delete(Uri uri) {
                result.isDeleteCalled = true;
                return null;
            }

            @Override
            public RestResult get(Uri uri, Class<?> classOfItem) {
                result.isGetCalled = true;
                return null;
            }

            @Override
            public RestResult post(Uri uri, Object item, Class<?> classOfItem) {
                result.isPostCalled = true;
                return null;
            }

            @Override
            public RestResult put(Uri uri, Object item, Class<?> classOfItem) {
                result.isPutCalled = true;
                return null;
            }

        });
    }

    /**
     * Verifies that a delete rest operation is delegated correctly to the
     * {@link NetworkClientDelegate}.
     * 
     * <pre>
     * 
     * 1. Create a new {@link HttpRestClient} and add a mock
     *      {@link NetworkClientDelegate} to it.
     * 
     * 2. Push a delete operation to the client.
     * 
     * 3. Verify that the delete method of the network helper
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
        assertEquals(false, result.isGetCalled);
        assertEquals(false, result.isPostCalled);
        assertEquals(false, result.isPutCalled);
    }

    /**
     * Verifies that a get rest operation is delegated correctly to the
     * {@link NetworkClientDelegate}.
     * 
     * <pre>
     * 
     * 1. Create a new {@link HttpRestClient} and add a mock
     *      {@link NetworkClientDelegate} to it.
     * 
     * 2. Push a get operation to the client.
     * 
     * 3. Verify that the get method of the network helper
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
        assertEquals(true, result.isGetCalled);
        assertEquals(false, result.isPostCalled);
        assertEquals(false, result.isPutCalled);
    }

    /**
     * Verifies that a post rest operation is delegated correctly to the
     * {@link NetworkClientDelegate}.
     * 
     * <pre>
     * 
     * 1. Create a new {@link HttpRestClient} and add a mock
     *      {@link NetworkClientDelegate} to it.
     * 
     * 2. Push a post operation to the client.
     * 
     * 3. Verify that the post method of the network helper
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
        assertEquals(false, result.isGetCalled);
        assertEquals(true, result.isPostCalled);
        assertEquals(false, result.isPutCalled);
    }

    /**
     * Verifies that a put rest operation is delegated correctly to the
     * {@link NetworkClientDelegate}.
     * 
     * <pre>
     * 
     * 1. Create a new {@link HttpRestClient} and add a mock
     *      {@link NetworkClientDelegate} to it.
     * 
     * 2. Push a put operation to the client.
     * 
     * 3. Verify that the put method of the network helper
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
        assertEquals(false, result.isGetCalled);
        assertEquals(false, result.isPostCalled);
        assertEquals(true, result.isPutCalled);
    }
}
