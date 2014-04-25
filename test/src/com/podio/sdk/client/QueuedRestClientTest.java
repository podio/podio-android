package com.podio.sdk.client;

import android.test.InstrumentationTestCase;

import com.podio.sdk.Filter;
import com.podio.sdk.Session;
import com.podio.sdk.client.mock.MockRestClient;
import com.podio.sdk.domain.ItemFilter;
import com.podio.sdk.internal.request.ResultListener;
import com.podio.test.TestUtils;

public class QueuedRestClientTest extends InstrumentationTestCase {

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
        RestRequest request = new RestRequest();
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
        final Filter firstFilter = new ItemFilter("first");
        final Filter secondFilter = new ItemFilter("second");

        final ConcurrentResult firstResult = new ConcurrentResult();
        final ConcurrentResult secondResult = new ConcurrentResult();

        MockRestClient testTarget = new MockRestClient("test://", "podio.test", 2) {
            @Override
            protected RestResult handleRequest(RestRequest restRequest) {
                Filter filter = restRequest.getFilter();

                if (filter == firstFilter) {
                    firstResult.isRequestPopped = true;
                    firstResult.isTicketValid = true;
                } else if (filter == secondFilter) {
                    secondResult.isRequestPopped = true;
                    secondResult.isTicketValid = true;
                }

                if (firstResult.isTicketValid && secondResult.isTicketValid) {
                    TestUtils.releaseBlockedThread();
                }

                return new RestResult(true, null, null);
            }
        };

        RestRequest firstRequest = new RestRequest().setFilter(firstFilter);
        RestRequest secondRequest = new RestRequest().setFilter(secondFilter);

        firstResult.isRequestPushed = testTarget.enqueue(firstRequest);
        secondResult.isRequestPushed = testTarget.enqueue(secondRequest);

        TestUtils.blockThread(500);

        assertEquals(true, firstResult.isRequestPushed);
        assertEquals(true, firstResult.isRequestPopped);
        assertEquals(true, firstResult.isTicketValid);
        assertEquals(true, secondResult.isRequestPushed);
        assertEquals(true, secondResult.isRequestPopped);
        assertEquals(true, secondResult.isTicketValid);
    }

    /**
     * Verifies that a null pointer result, returned by the request processing,
     * triggers a onFailure callback report.
     * 
     * <pre>
     * 
     *  1. Set up a local QueuedRestClient implementation.
     *  
     *  2. Push a request to the client.
     *  
     *  3. Return null from the processing method.
     *  
     *  4. Verify that the onFailure callback is called on the result listener.
     * 
     * </pre>
     */
    public void testRequestQueueHandleRequestNullResultGivesOnFailureCallback() {
        ResultListener listener = new ResultListener() {
            @Override
            public void onFailure(Object ticket, String message) {
                boolean isCalled = true;
                assertTrue(isCalled);
            }

            @Override
            public void onSessionChange(Object ticket, Session session) {
                boolean isCalled = true;
                assertFalse(isCalled);
            }

            @Override
            public void onSuccess(Object ticket, Object item) {
                boolean isCalled = true;
                assertFalse(isCalled);
            }
        };

        MockRestClient testTarget = new MockRestClient("test://", "podio.test") {
            @Override
            protected RestResult handleRequest(RestRequest restRequest) {
                return null;
            }
        };

        RestRequest request = new RestRequest().setResultListener(listener);
        testTarget.enqueue(request);
        TestUtils.blockThread(100);

        // The code should return in the above defined listener once the
        // blockade is released.
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
                TestUtils.releaseBlockedThread();
                return new RestResult(true, null, null);
            }
        };

        RestRequest request = new RestRequest();
        testTarget.enqueue(request);
        TestUtils.blockThread();

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
    public void testRequestQueuePushFailureOnCapacityReached() {
        MockRestClient testTarget = new MockRestClient("test://", "podio.test", 1) {
            @Override
            protected RestResult handleRequest(RestRequest restRequest) {
                return new RestResult(true, null, null);
            }
        };

        RestRequest request = new RestRequest();
        boolean isFirstRequestAccepted = testTarget.enqueue(request);
        boolean isSecondRequestAccepted = testTarget.enqueue(request);

        // Sometimes the Java thread scheduler allows the worker thread of the
        // QueuedRestClient to start working right after the first request is
        // made, resulting in the queue being empty when the second request is
        // posted, hence it being accepted as well. Since the 'handleRequest'
        // method is forced to sleep in two seconds according to the above
        // mock implementation, the worker thread is therefore guaranteed not to
        // pop the second request as well from the queue before the third
        // request is requested. The grand total is that either request 2 or 3
        // (or both) should be guaranteed to be rejected for this test to pass.
        boolean isThirdRequestAccepted = testTarget.enqueue(request);

        assertEquals(true, isFirstRequestAccepted);
        assertEquals(false, isSecondRequestAccepted);
        assertEquals(false, isThirdRequestAccepted);
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
        MockRestClient testTarget = new MockRestClient("test://", "podio.test", 1) {
            @Override
            protected RestResult handleRequest(RestRequest restRequest) {
                assertFalse("Unexpected request processing", true);
                return new RestResult(false, null, null);
            }
        };

        boolean isRequestAccepted = testTarget.enqueue(null);
        TestUtils.blockThread();

        assertEquals(false, isRequestAccepted);
        assertEquals(0, testTarget.size());
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
        final Filter expectedFilter = new ItemFilter("expected");
        final ConcurrentResult result = new ConcurrentResult();

        MockRestClient testTarget = new MockRestClient("test://", "podio.test") {
            @Override
            protected RestResult handleRequest(RestRequest restRequest) {
                result.isRequestPopped = true;
                result.isTicketValid = (expectedTicket == restRequest.getTicket());
                TestUtils.releaseBlockedThread();
                return new RestResult(true, "", null);
            }
        };

        RestRequest request = new RestRequest() //
                .setFilter(expectedFilter) //
                .setTicket(expectedTicket);

        result.isRequestPushed = testTarget.enqueue(request);

        // This line will block execution until the blocking semaphore is
        // released either by the above result listener or the test global
        // watch-dog.
        TestUtils.blockThread();

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

        final Filter firstFilter = new ItemFilter("first");
        final Filter secondFilter = new ItemFilter("second");

        ResultListener listener = new ResultListener() {
            @Override
            public void onFailure(Object ticket, String message) {
                boolean isCalled = true;
                assertFalse(isCalled);
            }

            @Override
            public void onSessionChange(Object ticket, Session session) {
                boolean isCalled = true;
                assertFalse(isCalled);
            }

            @Override
            public void onSuccess(Object ticket, Object item) {
                boolean isSuccess = true;

                if (firstFilter == ticket) {
                    assertTrue(isSuccess);
                } else if (secondFilter == ticket) {
                    assertTrue(isSuccess);
                } else {
                    assertFalse(isSuccess);
                }
            }
        };

        MockRestClient testTarget = new MockRestClient("test://", "podio.test") {
            @Override
            protected RestResult handleRequest(RestRequest restRequest) {
                Filter filter = restRequest.getFilter();

                if (firstFilter == filter) {
                    firstResult.isRequestPopped = true;
                    firstResult.isTicketValid = true;
                } else if (secondFilter == filter) {
                    secondResult.isRequestPopped = true;
                    secondResult.isTicketValid = true;
                }

                return new RestResult(true, null, null);
            }
        };

        RestRequest firstRequest = new RestRequest() //
                .setFilter(firstFilter) //
                .setResultListener(listener);

        firstResult.isRequestPushed = testTarget.enqueue(firstRequest);
        TestUtils.blockThread(100);

        assertEquals(true, firstResult.isRequestPushed);
        assertEquals(true, firstResult.isRequestPopped);
        assertEquals(QueuedRestClient.State.IDLE, testTarget.state());
        assertEquals(0, testTarget.size());

        TestUtils.blockThread(100);

        RestRequest secondRequest = new RestRequest() //
                .setFilter(secondFilter) //
                .setResultListener(listener);

        secondResult.isRequestPushed = testTarget.enqueue(secondRequest);
        TestUtils.blockThread(100);

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
                boolean isCalled = true;
                assertFalse(isCalled);
            }

            @Override
            public void onSessionChange(Object object, Session session) {
                boolean isCalled = true;
                assertFalse(isCalled);
            }

            @Override
            public void onSuccess(Object ticket, Object item) {
                threadNames[1] = Thread.currentThread().getName();
                assertTrue("The result of the RestRequest was reported from an unexpected thread."
                        + " Calling thread: " + threadNames[0] + ", other thread: "
                        + threadNames[1], threadNames[0].equals(threadNames[1]));
            }
        };

        MockRestClient testTarget = new MockRestClient("test://", "podio.test") {
            @Override
            protected RestResult handleRequest(RestRequest restRequest) {
                return new RestResult(true, null, null);
            }
        };

        RestRequest request = new RestRequest().setResultListener(listener);
        testTarget.enqueue(request);
        TestUtils.blockThread(100);

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
                boolean isCalled = true;
                assertTrue(isCalled);
            }

            @Override
            public void onSessionChange(Object ticket, Session session) {
                boolean isCalled = true;
                assertFalse(isCalled);
            }

            @Override
            public void onSuccess(Object ticket, Object item) {
                boolean isCalled = true;
                assertFalse(isCalled);
            }
        };

        MockRestClient testTarget = new MockRestClient("test://", "podio.test") {
            @Override
            protected RestResult handleRequest(RestRequest restRequest) {
                return new RestResult(false, null, null);
            }
        };

        RestRequest request = new RestRequest().setResultListener(listener);
        testTarget.enqueue(request);
        TestUtils.blockThread(100);

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
                boolean isCalled = true;
                assertFalse(isCalled);
            }

            @Override
            public void onSessionChange(Object ticket, Session session) {
                boolean isCalled = true;
                assertTrue(isCalled);
            }

            @Override
            public void onSuccess(Object ticket, Object item) {
                boolean isCalled = true;
                assertTrue(isCalled);
            }
        };

        MockRestClient testTarget = new MockRestClient("test://", "podio.test") {
            @Override
            protected RestResult handleRequest(RestRequest restRequest) {
                Session session = new Session("a", "b", 1L);
                return new RestResult(true, session, null, null);
            }
        };

        RestRequest request = new RestRequest().setResultListener(listener);
        testTarget.enqueue(request);
        TestUtils.blockThread(100);

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
                boolean isCalled = true;
                assertFalse(isCalled);
            }

            @Override
            public void onSessionChange(Object ticket, Session session) {
                boolean isCalled = true;
                assertFalse(isCalled);
            }

            @Override
            public void onSuccess(Object ticket, Object item) {
                boolean isCalled = true;
                assertTrue(isCalled);
            }
        };

        MockRestClient testTarget = new MockRestClient("test://", "podio.test") {
            @Override
            protected RestResult handleRequest(RestRequest restRequest) {
                return new RestResult(true, null, null);
            }
        };

        RestRequest request = new RestRequest().setResultListener(listener);
        testTarget.enqueue(request);
        TestUtils.blockThread(100);

        // The code should return in the above defined listener once the
        // blockade is released.
    }
}
