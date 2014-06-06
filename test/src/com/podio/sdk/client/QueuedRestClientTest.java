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

import java.util.concurrent.Semaphore;

import com.podio.sdk.PodioFilter;
import com.podio.sdk.client.mock.MockRestClient;
import com.podio.sdk.domain.Session;
import com.podio.sdk.filter.BasicPodioFilter;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.internal.request.ResultListener;
import com.podio.sdk.internal.request.ResultListenerAdapter;
import com.podio.test.TestUtils;
import com.podio.test.ThreadedTestCase;

public class QueuedRestClientTest extends ThreadedTestCase {

    private static final class ConcurrentResult {
        private boolean isRequestPushed;
        private boolean isRequestPopped;
        private boolean isTicketValid;
    }

    /**
     * Verifies that the correct scheme is returned, once asked for.
     * 
     * <pre>
     * 
     *  1. Set up a local QueuedRestClient implementation with a known scheme.
     *  
     *  2. Request the scheme from the client.
     *  
     *  3. Verify that the actual scheme of the client matches the expected one.
     * 
     * </pre>
     */
    public void testGetSchemeReturnsCorrectScheme() {
        String expectedScheme = "test://";
        MockRestClient testTarget = new MockRestClient(expectedScheme, null);
        String actualScheme = testTarget.getScheme();

        assertEquals(expectedScheme, actualScheme);
    }

    /**
     * Verifies that the correct authority is returned, once asked for.
     * 
     * <pre>
     * 
     *  1. Set up a local QueuedRestClient implementation with a known authority.
     *  
     *  2. Request the authority from the client.
     *  
     *  3. Verify that the actual authority of the client matches the expected one.
     * 
     * </pre>
     */
    public void testGetAuthorityReturnsCorrectAuthority() {
        String expectedAuthority = "a.b.c";
        MockRestClient testTarget = new MockRestClient(null, expectedAuthority);
        String actualAuthority = testTarget.getAuthority();

        assertEquals(expectedAuthority, actualAuthority);
    }

    /**
     * Verifies that the request queue can take at least one request even though
     * it's initialized with an invalid size (zero or less).
     * 
     * <pre>
     * 
     *  1. Set up a local QueuedRestClient implementation.
     *  
     *  2. Initialize it with an invalid queue size.
     *  
     *  3. Verify that the client still can take at least one request.
     * 
     * </pre>
     */
    public void testRequestQueueCapacityValid() {
        int invalidSize = -1;
        MockRestClient testTarget = new MockRestClient("test://", "podio.test", invalidSize);
        RestRequest request = new RestRequest().setFilter(new BasicPodioFilter()).setOperation(RestOperation.AUTHORIZE);
        boolean didAcceptRequest = testTarget.enqueue(request);

        assertEquals(true, didAcceptRequest);
    }

    /**
     * Verifies that the entire request queue is processed, given that the
     * in-flow ends or isn't as productive as the output.
     * 
     * <pre>
     * 
     *  1. Set up a local QueuedRestClient implementation.
     *  
     *  2. Push two requests to the client.
     *  
     *  3. Verify that both requests are processed.
     * 
     * </pre>
     */
    public void testRequestQueueDrainedEventually() {
        final PodioFilter firstFilter = new BasicPodioFilter("first");
        final PodioFilter secondFilter = new BasicPodioFilter("second");

        final ConcurrentResult firstResult = new ConcurrentResult();
        final ConcurrentResult secondResult = new ConcurrentResult();

        MockRestClient testTarget = new MockRestClient("test://", "podio.test", 2) {
            @Override
            protected RestResult handleRequest(RestRequest restRequest) {
                PodioFilter filter = restRequest.getFilter();

                if (filter == firstFilter) {
                    firstResult.isRequestPopped = true;
                    firstResult.isTicketValid = true;
                } else if (filter == secondFilter) {
                    secondResult.isRequestPopped = true;
                    secondResult.isTicketValid = true;
                }

                if (firstResult.isTicketValid && secondResult.isTicketValid) {
                    TestUtils.completed();
                }

                return RestResult.success();
            }
        };

        RestRequest firstRequest = new RestRequest().setFilter(firstFilter).setOperation(RestOperation.AUTHORIZE);
        RestRequest secondRequest = new RestRequest().setFilter(secondFilter).setOperation(RestOperation.AUTHORIZE);

        firstResult.isRequestPushed = testTarget.enqueue(firstRequest);
        secondResult.isRequestPushed = testTarget.enqueue(secondRequest);

        assertTrue(TestUtils.waitUntilCompletion());

        assertEquals(true, firstResult.isRequestPushed);
        assertEquals(true, firstResult.isRequestPopped);
        assertEquals(true, firstResult.isTicketValid);
        assertEquals(true, secondResult.isRequestPushed);
        assertEquals(true, secondResult.isRequestPopped);
        assertEquals(true, secondResult.isTicketValid);
    }

    /**
     * Verifies that the request isn't processed from the calling thread.
     * 
     * <pre>
     * 
     *  1. Temporarily store the name of the current thread.
     * 
     *  2. Set up a local QueuedRestClient implementation.
     *  
     *  3. Push a request to the client.
     *  
     *  4. Once the request is processed, take note of the processing threads name.
     *  
     *  5. Verify that the two process names are not the same.
     * 
     * </pre>
     */
    public void testRequestQueueProcessedOnWorkerThread() {
        final String[] threadNames = new String[] { Thread.currentThread().getName(),
                Thread.currentThread().getName() };

        MockRestClient testTarget = new MockRestClient("test://", "podio.test") {
            @Override
            protected RestResult handleRequest(RestRequest restRequest) {
                threadNames[1] = Thread.currentThread().getName();
                TestUtils.completed();
                return RestResult.success();
            }
        };

        RestRequest request = new RestRequest().setFilter(new BasicPodioFilter()).setOperation(RestOperation.AUTHORIZE);
        testTarget.enqueue(request);
        assertTrue(TestUtils.waitUntilCompletion());

        assertFalse(threadNames[0] + " vs. " + threadNames[1],
                threadNames[0].equals(threadNames[1]));
    }

    /**
     * Verifies that no further requests are accepted once the queue capacity is
     * reached.
     * 
     * <pre>
     * 
     *  1. Set up a local QueuedRestClient implementation which has a capacity
     *      of two requests.
     *  
     *  2. Push a request to the client.
     *  
     *  3. Push a second request to the client.
     *  
     *  4. Push a third request to the client.
     *  
     *  5. Verify that the first request was accepted.
     *  
     *  6. Verify that the second or third request was rejected.
     * 
     * </pre>
     */
    public void testRequestQueuePushFailureOnCapacityReached() throws InterruptedException {
    	final Semaphore waiter = new Semaphore(0);
    	
        MockRestClient testTarget = new MockRestClient("test://", "podio.test", 1) {
			@Override
			protected RestResult handleRequest(RestRequest restRequest) {
				try {
					waiter.acquire();
				} catch (InterruptedException e) {
				}
				
				return super.handleRequest(restRequest);
			}        	
        };
        
        ResultListener resultListener = new ResultListenerAdapter() {
			@Override
			public void onSuccess(Object ticket, Object content) {
				TestUtils.completed();
			}
		};

        RestRequest request = new RestRequest().setFilter(new BasicPodioFilter()).setOperation(RestOperation.AUTHORIZE).setResultListener(resultListener);
        
        assertEquals(testTarget.enqueue(request), true);
        //One is being processed, so room for one more in the queue
        assertEquals(testTarget.enqueue(request), true);

        assertEquals(testTarget.enqueue(request), false);
        
        //Complete the request, so we can add another
        waiter.release();
        
        TestUtils.waitUntilCompletion();

        //Now it can be processed
        assertEquals(testTarget.enqueue(request), true);
    }

    /**
     * Verifies that a null pointer request isn't accepted when pushed to the
     * queue.
     * 
     * <pre>
     * 
     *  1. Set up a local QueuedRestClient implementation.
     *  
     *  2. Push a null pointer request to the client.
     *  
     *  3. Verify that the request is not accepted.
     * 
     * </pre>
     */
    public void testRequestQueuePushFailureOnNullRequest() {
        MockRestClient testTarget = new MockRestClient("test://", "podio.test", 1);

		try {
			testTarget.enqueue(null);
			fail("Should have thrown exception");
		} catch (NullPointerException e) {
		}
    }

    /**
     * Verifies that a request is pushed to the queue and also successfully
     * popped from it.
     * 
     * <pre>
     * 
     *  1. Set up a local QueuedRestClient implementation.
     *  
     *  2. Push a request to the client.
     * 
     *  3. Wait for the client to start handling the request.
     * 
     *  4. Verify the ticket of the processing request.
     * 
     * </pre>
     */
    public void testRequestQueuePushPopSuccess() {
        final Object expectedTicket = new Object();
        final PodioFilter expectedFilter = new BasicPodioFilter("expected");
        final ConcurrentResult result = new ConcurrentResult();

        MockRestClient testTarget = new MockRestClient("test://", "podio.test") {
            @Override
            protected RestResult handleRequest(RestRequest restRequest) {
                result.isRequestPopped = true;
                result.isTicketValid = (expectedTicket == restRequest.getTicket());
                TestUtils.completed();
                return RestResult.success();
            }
        };

        RestRequest request = new RestRequest() //
                .setFilter(expectedFilter) //
                .setTicket(expectedTicket).setOperation(RestOperation.AUTHORIZE);

        result.isRequestPushed = testTarget.enqueue(request);

        // This line will block execution until the blocking semaphore is
        // released either by the above result listener or the test global
        // watch-dog.
        assertTrue(TestUtils.waitUntilCompletion());

        assertEquals(true, result.isRequestPushed);
        assertEquals(true, result.isRequestPopped);
        assertEquals(true, result.isTicketValid);
    }

    /**
     * Verifies that the processing of the request queue, after it's been
     * completely processed, is automatically resumed when a new request is
     * pushed to it.
     * 
     * <pre>
     * 
     *  1. Set up a local QueuedRestClient implementation.
     *  
     *  2. Push a request to the client.
     *  
     *  3. Verify that the request is processed and that the queue is empty and idle.
     *  
     *  4. Push a second request to the client.
     *  
     *  5. Verify that the second request is automatically processed as well.
     * 
     * </pre>
     */
    public void testRequestQueueResumedOnNewRequest() {
        final ConcurrentResult firstResult = new ConcurrentResult();
        final ConcurrentResult secondResult = new ConcurrentResult();

        final PodioFilter firstFilter = new BasicPodioFilter("first");
        final PodioFilter secondFilter = new BasicPodioFilter("second");

        ResultListener listener = new ResultListener() {
            @Override
            public void onFailure(Object ticket, String message) {
            	fail();
            }

            @Override
            public void onSessionChange(Object ticket, Session session) {
            	fail();
            }

            @Override
            public void onSuccess(Object ticket, Object item) {
                if (firstFilter != ticket && secondFilter != ticket) {
                	fail();
                }
            }
        };

        MockRestClient testTarget = new MockRestClient("test://", "podio.test") {
            @Override
            protected RestResult handleRequest(RestRequest restRequest) {
                PodioFilter filter = restRequest.getFilter();

                if (firstFilter == filter) {
                    firstResult.isRequestPopped = true;
                    firstResult.isTicketValid = true;
                    
                    TestUtils.completed();
                } else if (secondFilter == filter) {
                    secondResult.isRequestPopped = true;
                    secondResult.isTicketValid = true;
                    
                    TestUtils.completed();
                }
                
                return super.handleRequest(restRequest);
            }
        };

        RestRequest firstRequest = new RestRequest() //
                .setFilter(firstFilter) //
                .setResultListener(listener)
                .setOperation(RestOperation.AUTHORIZE);

        firstResult.isRequestPushed = testTarget.enqueue(firstRequest);
        
        assertTrue(TestUtils.waitUntilCompletion());
        
        //FIXME: Temporary to get the request handled also
        try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}

        assertEquals(true, firstResult.isRequestPushed);
        assertEquals(true, firstResult.isRequestPopped);
        assertEquals(QueuedRestClient.State.IDLE, testTarget.state());
        assertEquals(0, testTarget.size());
        
        TestUtils.reset();

        RestRequest secondRequest = new RestRequest() //
                .setFilter(secondFilter) //
                .setResultListener(listener)
                .setOperation(RestOperation.AUTHORIZE);

        secondResult.isRequestPushed = testTarget.enqueue(secondRequest);
        
        assertTrue(TestUtils.waitUntilCompletion());
        
        //FIXME: Temporary to get the request handled also
        try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}

        assertEquals(true, secondResult.isRequestPushed);
        assertEquals(true, secondResult.isRequestPopped);
        assertEquals(QueuedRestClient.State.IDLE, testTarget.state());
        assertEquals(0, testTarget.size());

        // The code should return in the above defined listener once the
        // blockade is released.
    }

    /**
     * Verifies that the result of a request is reported on the calling thread.
     * 
     * <pre>
     * 
     *  1. Temporarily store the name of the current thread.
     * 
     *  2. Set up a local QueuedRestClient implementation.
     *  
     *  3. Push a request to the client.
     *  
     *  4. Once the result is reported, take note of the reporting threads name.
     *  
     *  5. Verify that the two process names are the same.
     * 
     * </pre>
     */
    public void testRequestResultReportedOnCallingThread() {
        final String[] threadNames = new String[] { Thread.currentThread().getName(), "" };

        ResultListener listener = new ResultListener() {
            @Override
            public void onFailure(Object ticket, String message) {
            	fail();
            }

            @Override
            public void onSessionChange(Object object, Session session) {
            	fail();
            }

            @Override
            public void onSuccess(Object ticket, Object item) {
                assertEquals(threadNames[0], threadNames[1]);
            }
        };

        MockRestClient testTarget = new MockRestClient("test://", "podio.test") {
            @Override
            protected RestResult handleRequest(RestRequest restRequest) {
            	TestUtils.completed();
            	
                return RestResult.success();
            }
        };

        RestRequest request = new RestRequest().setResultListener(listener).setFilter(new BasicPodioFilter()).setOperation(RestOperation.AUTHORIZE);
        testTarget.enqueue(request);
        
        assertTrue(TestUtils.waitUntilCompletion());

        // The code should return in the above defined listener once the
        // blockade is released.
    }

    /**
     * Verifies that the onFailure callback is called when the request couldn't
     * be processed properly.
     * 
     * <pre>
     * 
     *  1. Set up a local QueuedRestClient implementation.
     *  
     *  2. Push a request to the client.
     *  
     *  3. Simulate a failure during processing of the request.
     *  
     *  4. Verify that the correct callback method is called.
     * 
     * </pre>
     */
    public void testResultListenerReportsFailureProperly() {
        ResultListener listener = new ResultListener() {
            @Override
            public void onFailure(Object ticket, String message) {
            }

            @Override
            public void onSessionChange(Object ticket, Session session) {
            	fail();
            }

            @Override
            public void onSuccess(Object ticket, Object item) {
            	fail();
            }
        };

        MockRestClient testTarget = new MockRestClient("test://", "podio.test") {
            @Override
            protected RestResult handleRequest(RestRequest restRequest) {
            	TestUtils.completed();
            	
                return RestResult.success();
            }
        };

        RestRequest request = new RestRequest().setResultListener(listener).setFilter(new BasicPodioFilter()).setOperation(RestOperation.AUTHORIZE);
        testTarget.enqueue(request);
        
        assertTrue(TestUtils.waitUntilCompletion());

        // The code should return in the above defined listener once the
        // blockade is released.
    }

    /**
     * Verifies that the onSessionChange callback is called when the request was
     * processed properly with a non-null session parameter.
     * 
     * <pre>
     * 
     *  1. Set up a local QueuedRestClient implementation.
     * 
     *  2. Push a request to the client.
     * 
     *  3. Simulate a success and session update during processing of the
     *      request.
     * 
     *  4. Verify that the correct callback methods are called.
     * 
     * </pre>
     */
    public void testResultListenerReportsSessionChangeProperly() {
        ResultListener listener = new ResultListener() {
            @Override
            public void onFailure(Object ticket, String message) {
            	fail();
            }

            @Override
            public void onSessionChange(Object ticket, Session session) {
            }

            @Override
            public void onSuccess(Object ticket, Object item) {
            }
        };

        MockRestClient testTarget = new MockRestClient("test://", "podio.test") {
            @Override
            protected RestResult handleRequest(RestRequest restRequest) {
            	TestUtils.completed();
            	
                Session session = new Session("a", "b", 1L);
                
                return new RestResult(true, session, null, null);
            }
        };

        RestRequest request = new RestRequest().setResultListener(listener).setFilter(new BasicPodioFilter()).setOperation(RestOperation.AUTHORIZE);
        testTarget.enqueue(request);
        
        assertTrue(TestUtils.waitUntilCompletion());

        // The code should return in the above defined listener once the
        // blockade is released.
    }

    /**
     * Verifies that the onSuccess callback is called when the request was
     * processed properly.
     * 
     * <pre>
     * 
     *  1. Set up a local QueuedRestClient implementation.
     *  
     *  2. Push a request to the client.
     *  
     *  3. Simulate a success during processing of the request.
     *  
     *  4. Verify that the correct callback method is called.
     * 
     * </pre>
     */
    public void testResultListenerReportsSuccessProperly() {
        ResultListener listener = new ResultListener() {
            @Override
            public void onFailure(Object ticket, String message) {
            	fail();
            }

            @Override
            public void onSessionChange(Object ticket, Session session) {
            	fail();
            }

            @Override
            public void onSuccess(Object ticket, Object item) {
            }
        };

        MockRestClient testTarget = new MockRestClient("test://", "podio.test") {
            @Override
            protected RestResult handleRequest(RestRequest restRequest) {
            	TestUtils.completed();
            	
                return RestResult.success();
            }
        };

        RestRequest request = new RestRequest().setResultListener(listener).setFilter(new BasicPodioFilter()).setOperation(RestOperation.AUTHORIZE);
        testTarget.enqueue(request);
        
        assertTrue(TestUtils.waitUntilCompletion());

        // The code should return in the above defined listener once the
        // blockade is released.
    }
}
