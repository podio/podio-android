package com.podio.sdk.client;

import android.app.Instrumentation;
import android.content.Context;
import android.net.Uri;
import android.test.InstrumentationTestCase;

import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.domain.PodioFilter;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.test.TestUtils;

public class HttpRestClientTest extends InstrumentationTestCase {

    private static final class ConcurrentResult {
        private boolean isAuthorizeCalled;
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
        target = new HttpRestClient(context, "authority", new RestClientDelegate() {

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
                result.isGetCalled = true;
                return null;
            }

            @Override
            public RestResult post(Uri uri, Object item) {
                result.isPostCalled = true;
                return null;
            }

            @Override
            public RestResult put(Uri uri, Object item) {
                result.isPutCalled = true;
                return null;
            }

        }, 10);
    }

    /**
     * Verifies that a authorize rest operation is delegated correctly to the
     * {@link NetworkClientDelegate}.
     * 
     * <pre>
     * 
     * 1. Create a new {@link HttpRestClient} and add a mock
     *      {@link NetworkClientDelegate} to it.
     * 
     * 2. Push a authorize operation to the client.
     * 
     * 3. Verify that the authorize method of the network helper
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
        assertEquals(false, result.isGetCalled);
        assertEquals(false, result.isPostCalled);
        assertEquals(false, result.isPutCalled);
    }

    /**
     * Verifies that an IllegalArgumentException is thrown when trying to create
     * an HttpRestClient with a null pointer delegate.
     * 
     * <pre>
     * 
     * 1. Create a new HttpRestClient with a null pointer network delegate.
     * 
     * 2. Verify that an IllegalArgumentException was thrown.
     * 
     * </pre>
     */
    public void testConstructorThrowsIllegalArgumentExceptionOnInvalidDelegates() {
        try {
            new HttpRestClient(null, null, null, 0);
            boolean didReachThisPoint = true;
            assertFalse(didReachThisPoint);
        } catch (IllegalArgumentException e) {
            boolean didThrowException = true;
            assertTrue(didThrowException);
        }
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
                .setFilter(new PodioFilter()) //
                .setOperation(RestOperation.DELETE);

        target.enqueue(restRequest);
        TestUtils.blockThread(20);

        assertEquals(false, result.isAuthorizeCalled);
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
                .setFilter(new PodioFilter()) //
                .setOperation(RestOperation.GET);

        target.enqueue(restRequest);
        TestUtils.blockThread(20);

        assertEquals(false, result.isAuthorizeCalled);
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
                .setFilter(new PodioFilter()) //
                .setOperation(RestOperation.POST);

        target.enqueue(restRequest);
        TestUtils.blockThread(20);

        assertEquals(false, result.isAuthorizeCalled);
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
                .setFilter(new PodioFilter()) //
                .setOperation(RestOperation.PUT);

        target.enqueue(restRequest);
        TestUtils.blockThread(20);

        assertEquals(false, result.isAuthorizeCalled);
        assertEquals(false, result.isDeleteCalled);
        assertEquals(false, result.isGetCalled);
        assertEquals(false, result.isPostCalled);
        assertEquals(true, result.isPutCalled);
    }
}
