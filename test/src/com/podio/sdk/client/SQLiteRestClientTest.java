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

import com.podio.sdk.PodioParser;
import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.filter.BasicPodioFilter;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.test.TestUtils;
import com.podio.test.ThreadedTestCase;

public class SQLiteRestClientTest extends ThreadedTestCase {

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
        target = new SQLiteRestClient(context, "authority", new RestClientDelegate() {

            @Override
            public <T> RestResult<T> authorize(Uri uri, PodioParser<? extends T> itemParser) {
                result.isAuthorizeCalled = true;
                TestUtils.completed();
                return RestResult.success();
            }

            @Override
            public <T> RestResult<T> delete(Uri uri, PodioParser<? extends T> itemParser) {
                result.isDeleteCalled = true;
                TestUtils.completed();
                return RestResult.success();
            }

            @Override
            public <T> RestResult<T> get(Uri uri, PodioParser<? extends T> itemParser) {
                result.isQueryCalled = true;
                TestUtils.completed();
                return RestResult.success();
            }

            @Override
            public <T> RestResult<T> post(Uri uri, Object item, PodioParser<? extends T> itemParser) {
                result.isInsertCalled = true;
                TestUtils.completed();
                return RestResult.success();
            }

            @Override
            public <T> RestResult<T> put(Uri uri, Object item, PodioParser<? extends T> itemParser) {
                result.isUpdateCalled = true;
                TestUtils.completed();
                return RestResult.success();
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
        RestRequest<Object> restRequest = new RestRequest<Object>() //
                .setFilter(new BasicPodioFilter()) //
                .setOperation(RestOperation.AUTHORIZE);

        target.enqueue(restRequest);
        
        assertTrue(TestUtils.waitUntilCompletion());

        assertTrue(result.isAuthorizeCalled);
        assertFalse(result.isDeleteCalled);
        assertFalse(result.isInsertCalled);
        assertFalse(result.isQueryCalled);
        assertFalse(result.isUpdateCalled);
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
            fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
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
        RestRequest<Object> restRequest = new RestRequest<Object>() //
                .setFilter(new BasicPodioFilter()) //
                .setOperation(RestOperation.DELETE);

        target.enqueue(restRequest);
        
        assertTrue(TestUtils.waitUntilCompletion());

        assertFalse(result.isAuthorizeCalled);
        assertTrue(result.isDeleteCalled);
        assertFalse(result.isInsertCalled);
        assertFalse(result.isQueryCalled);
        assertFalse(result.isUpdateCalled);
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
        RestRequest<Object> restRequest = new RestRequest<Object>() //
                .setFilter(new BasicPodioFilter()) //
                .setOperation(RestOperation.GET);

        target.enqueue(restRequest);
        
        assertTrue(TestUtils.waitUntilCompletion());

        assertFalse(result.isAuthorizeCalled);
        assertFalse(result.isDeleteCalled);
        assertFalse(result.isInsertCalled);
        assertTrue(result.isQueryCalled);
        assertFalse(result.isUpdateCalled);
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
        RestRequest<Object> restRequest = new RestRequest<Object>() //
                .setFilter(new BasicPodioFilter()) //
                .setOperation(RestOperation.POST);

        target.enqueue(restRequest);
        
        assertTrue(TestUtils.waitUntilCompletion());

        assertFalse(result.isAuthorizeCalled);
        assertFalse(result.isDeleteCalled);
        assertTrue(result.isInsertCalled);
        assertFalse(result.isQueryCalled);
        assertFalse(result.isUpdateCalled);
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
        RestRequest<Object> restRequest = new RestRequest<Object>() //
                .setFilter(new BasicPodioFilter()) //
                .setOperation(RestOperation.PUT);

        target.enqueue(restRequest);
        
        assertTrue(TestUtils.waitUntilCompletion());

        assertFalse(result.isAuthorizeCalled);
        assertFalse(result.isDeleteCalled);
        assertFalse(result.isInsertCalled);
        assertFalse(result.isQueryCalled);
        assertTrue(result.isUpdateCalled);
    }
}
