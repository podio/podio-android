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

import java.util.ArrayList;
import java.util.List;

import android.app.Instrumentation;
import android.content.Context;
import android.net.Uri;

import com.podio.sdk.PodioParser;
import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.filter.BasicPodioFilter;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.test.TestUtils;
import com.podio.test.ThreadedTestCase;

public class HttpRestClientTest extends ThreadedTestCase {

    private HttpRestClient target;
    private List<RestOperation> calls;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Instrumentation instrumentation = getInstrumentation();
        Context context = instrumentation.getContext();

        calls = new ArrayList<RestOperation>();
        target = new HttpRestClient(context, "authority", new RestClientDelegate() {

            @Override
            public RestResult authorize(Uri uri, PodioParser<?> itemParser) {
            	calls.add(RestOperation.AUTHORIZE);
                TestUtils.completed();
                return RestResult.success();
            }

            @Override
            public RestResult delete(Uri uri, PodioParser<?> itemParser) {
            	calls.add(RestOperation.DELETE);
                TestUtils.completed();
                return RestResult.success();
            }

            @Override
            public RestResult get(Uri uri, PodioParser<?> itemParser) {
            	calls.add(RestOperation.GET);
                TestUtils.completed();
                return RestResult.success();
            }

            @Override
            public RestResult post(Uri uri, Object item, PodioParser<?> itemParser) {
            	calls.add(RestOperation.POST);
                TestUtils.completed();
                return RestResult.success();
            }

            @Override
            public RestResult put(Uri uri, Object item, PodioParser<?> itemParser) {
            	calls.add(RestOperation.PUT);
                TestUtils.completed();
                return RestResult.success();
            }

        }, 10);
    }
    
    private void assertCalled(RestOperation... operations) {
        assertEquals(calls.size(), operations.length);
        for (int i = 0; i < operations.length; i++) {
			assertEquals(operations[i], calls.get(i));
		}
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
                .setFilter(new BasicPodioFilter()) //
                .setOperation(RestOperation.AUTHORIZE);

        target.enqueue(restRequest);
        
        assertTrue(TestUtils.waitUntilCompletion());

        assertCalled(RestOperation.AUTHORIZE);
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
            fail("Should have thrown exception");
        } catch (NullPointerException e) {
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
                .setFilter(new BasicPodioFilter()) //
                .setOperation(RestOperation.DELETE);

        target.enqueue(restRequest);
        
        assertTrue(TestUtils.waitUntilCompletion());
        
        assertCalled(RestOperation.DELETE);
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
                .setFilter(new BasicPodioFilter()) //
                .setOperation(RestOperation.GET);

        target.enqueue(restRequest);
        
        assertTrue(TestUtils.waitUntilCompletion());
        
        assertCalled(RestOperation.GET);
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
                .setFilter(new BasicPodioFilter()) //
                .setOperation(RestOperation.POST);

        target.enqueue(restRequest);
        
        assertTrue(TestUtils.waitUntilCompletion());
        
        assertCalled(RestOperation.POST);
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
                .setFilter(new BasicPodioFilter()) //
                .setOperation(RestOperation.PUT);

        target.enqueue(restRequest);
        
        assertTrue(TestUtils.waitUntilCompletion());
        
        assertCalled(RestOperation.PUT);
    }
}
