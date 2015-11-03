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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import android.test.InstrumentationTestCase;

import com.podio.sdk.PodioException;
import com.podio.sdk.RestClient;
import com.podio.sdk.ResultListener;
import com.podio.sdk.SessionListener;
import com.podio.sdk.ThreadCaptureResultListener;
import com.podio.sdk.domain.Session;
import com.podio.sdk.filter.BasicPodioFilter;
import com.podio.sdk.mock.MockRestClient;

public class QueuedRestClientTest extends InstrumentationTestCase {

    @Mock
    ResultListener<Object> resultListener;

    @Mock
    SessionListener sessionListener;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Required by Mockito.spy()
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());

        MockitoAnnotations.initMocks(this);
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
        assertEquals("test://", (new QueuedRestClient("test://", null, 1) {

            @Override
            protected <T> RestResult<T> handleRequest(RestRequest<T> restRequest) throws PodioException {
                return RestResult.success();
            }

        }).getScheme());
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
        assertEquals("a.b.c", (new QueuedRestClient(null, "a.b.c", 1) {

            @Override
            protected <T> RestResult<T> handleRequest(RestRequest<T> restRequest) throws PodioException {
                return RestResult.success();
            }

        }).getAuthority());
    }

    /**
     * Verifies that any internally thrown {@link PodioException}'s are bubbling
     * up to the future and re-thrown there.
     * 
     * <pre>
     * 
     *  1. Set up a mocked QueuedRestClient which will throw a RuntimeException
     *      on the worker thread.
     *  
     *  2. Enqueue an arbitrary request in the client.
     * 
     *  3. Verify that the RuntimeException is delivered to the calling thread.
     * 
     * </pre>
     * 
     * @throws TimeoutException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws NullPointerException
     */
    public void testExceptionsArePropagatedToMainThread() throws NullPointerException, InterruptedException, ExecutionException, TimeoutException {
        RuntimeException runtimeException = new RuntimeException("test-exception");

        try {
            new MockRestClient(runtimeException)
                    .enqueue(new RestRequest<Object>()
                            .setFilter(new BasicPodioFilter())
                            .setOperation(RestClient.Operation.GET))
                    .get(100, TimeUnit.MILLISECONDS);

            fail("Should have propaget exception by now");
        } catch (ExecutionException e) {
            Throwable t = e.getCause();
            assertEquals("test-exception", t.getMessage());
        }
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
     * 
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws NullPointerException
     * @throws TimeoutException
     */
    public void testRequestQueueCapacityValid() throws NullPointerException, InterruptedException, ExecutionException, TimeoutException {
        new MockRestClient(-1)
                .enqueue(new RestRequest<Object>()
                        .setFilter(new BasicPodioFilter())
                        .setOperation(RestClient.Operation.GET))
                .get(100, TimeUnit.MILLISECONDS);
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
     * 
     * @throws TimeoutException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws Exception
     * @throws NullPointerException
     */
    public void testRequestQueueDrainedEventually() throws NullPointerException, InterruptedException, ExecutionException, TimeoutException {
        final QueuedRestClient testTarget = new MockRestClient(2);

        testTarget
                .enqueue(new RestRequest<Object>()
                        .setFilter(new BasicPodioFilter("first"))
                        .setOperation(RestClient.Operation.GET))
                .withResultListener(resultListener);

        testTarget
                .enqueue(new RestRequest<Object>()
                        .setFilter(new BasicPodioFilter("second"))
                        .setOperation(RestClient.Operation.GET))
                .withResultListener(resultListener);

        Mockito.verify(resultListener, Mockito.timeout(300).times(2)).onRequestPerformed(null);
        Mockito.verifyNoMoreInteractions(resultListener);
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
     * 
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws NullPointerException
     * @throws TimeoutException
     */
    public void testRequestQueueProcessedOnWorkerThread() throws NullPointerException, InterruptedException, ExecutionException, TimeoutException {
        final String[] handlerThreadNames = { Thread.currentThread().getName(), null };

        (new QueuedRestClient(null, null, 1) {
            @Override
            protected <T> RestResult<T> handleRequest(RestRequest<T> restRequest) {
                handlerThreadNames[1] = Thread.currentThread().getName();

                return RestResult.success();
            }
        })
                .enqueue(new RestRequest<Object>()
                        .setFilter(new BasicPodioFilter())
                        .setOperation(RestClient.Operation.GET))
                .get(100, TimeUnit.MILLISECONDS);

        assertNotNull(handlerThreadNames[0]);
        assertNotNull(handlerThreadNames[1]);
        assertFalse(handlerThreadNames[0].equals(handlerThreadNames[1]));
    }

    /**
     * Verifies that no further requests are accepted once the queue capacity is
     * reached.
     * 
     * <pre>
     * 
     *  1. Set up a local QueuedRestClient implementation which has a capacity
     *      of 1 request.
     *  
     *  2. Push a request to the client.
     *  
     *  3. Push a second request to the client. This should be accepted as the
     *      first request is being processed, hence, doesn't occupy the only
     *      place in the queue.
     *  
     *  4. Push a third request to the client.
     *  
     *  5. Verify that the first and second requests were accepted.
     *  
     *  6. Verify that the third request was rejected.
     * 
     * </pre>
     * 
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws NullPointerException
     */
    public void testRequestQueuePushFailureOnCapacityReached() throws NullPointerException, InterruptedException, ExecutionException {
        QueuedRestClient testTarget = new QueuedRestClient(null, null, 1) {

            @Override
            protected <T> RestResult<T> handleRequest(RestRequest<T> restRequest) throws PodioException {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                }

                return RestResult.success();
            }

        };

        testTarget
                .enqueue(new RestRequest<Object>()
                        .setFilter(new BasicPodioFilter())
                        .setOperation(RestClient.Operation.GET))
                .withResultListener(resultListener);

        // One is being processed, so room for one more in the queue
        testTarget
                .enqueue(new RestRequest<Object>()
                        .setFilter(new BasicPodioFilter())
                        .setOperation(RestClient.Operation.GET))
                .withResultListener(resultListener);

        // Now we are out of room
        try {
            testTarget
                    .enqueue(new RestRequest<Object>()
                            .setFilter(new BasicPodioFilter())
                            .setOperation(RestClient.Operation.GET))
                    .withResultListener(resultListener);

            fail("Didn't reject request");
        } catch (RejectedExecutionException e) {
            // The request should be rejected.
        }
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
     * 
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public void testRequestQueuePushFailureOnNullRequest() throws InterruptedException, ExecutionException, TimeoutException {
        try {
            new MockRestClient()
                    .enqueue(null)
                    .get(100, TimeUnit.MILLISECONDS);
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
     * 
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws NullPointerException
     * @throws TimeoutException
     */
    public void testRequestQueuePushPopSuccess() throws NullPointerException, InterruptedException, ExecutionException, TimeoutException {
        QueuedRestClient testTarget = Mockito.spy(new MockRestClient());

        RestRequest<Object> request = new RestRequest<Object>()
                .setFilter(new BasicPodioFilter("expected"))
                .setOperation(RestClient.Operation.GET);

        testTarget
                .enqueue(request)
                .get(100, TimeUnit.MILLISECONDS);

        Mockito.verify(testTarget).handleRequest(request);
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
     * 
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws NullPointerException
     * @throws TimeoutException
     */
    public void testRequestQueueResumedOnNewRequest() throws NullPointerException, InterruptedException, ExecutionException, TimeoutException {
        QueuedRestClient testTarget = new MockRestClient(2);

        testTarget
                .enqueue(new RestRequest<Object>()
                        .setFilter(new BasicPodioFilter("first"))
                        .setOperation(RestClient.Operation.GET))
                .withResultListener(resultListener)
                .get(100, TimeUnit.MILLISECONDS);

        assertEquals(0, testTarget.size());

        testTarget
                .enqueue(new RestRequest<Object>()
                        .setFilter(new BasicPodioFilter("second"))
                        .setOperation(RestClient.Operation.GET))
                .withResultListener(resultListener)
                .get(100, TimeUnit.MILLISECONDS);

        Mockito.verify(resultListener, Mockito.timeout(200).times(2)).onRequestPerformed(null);
        assertEquals(0, testTarget.size());
    }

    /**
     * Verifies that the result of a request is reported on the main thread.
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
     * 
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws NullPointerException
     * @throws TimeoutException
     */
    public void testRequestResultReportedOnMainThread() throws InterruptedException, NullPointerException, ExecutionException, TimeoutException {
        final ThreadCaptureResultListener threadListener = Mockito.spy(new ThreadCaptureResultListener());

        new MockRestClient()
                .enqueue(new RestRequest<Object>()
                        .setFilter(new BasicPodioFilter())
                        .setOperation(RestClient.Operation.GET))
                .withResultListener(threadListener)
                .get(100, TimeUnit.MILLISECONDS);

        Mockito.verify(threadListener, Mockito.timeout(200)).onRequestPerformed(null);
        assertEquals("main", threadListener.getThreadName());
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
     * 
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws NullPointerException
     * @throws TimeoutException
     */
    public void testResultListenerReportsSessionChangeProperly() throws NullPointerException, InterruptedException, ExecutionException, TimeoutException {
        Session session = Mockito.mock(Session.class);

        new MockRestClient(1, RestResult.success(null, session))
                .enqueue(new RestRequest<Object>()
                        .setFilter(new BasicPodioFilter())
                        .setOperation(RestClient.Operation.GET))
                .withSessionListener(sessionListener)
                .withResultListener(resultListener)
                .get(100, TimeUnit.MILLISECONDS);

        Mockito.verify(sessionListener, Mockito.timeout(100)).onSessionChanged(session);
        Mockito.verify(resultListener, Mockito.timeout(100)).onRequestPerformed(Mockito.anyObject());
        Mockito.verifyNoMoreInteractions(sessionListener);
        Mockito.verifyNoMoreInteractions(resultListener);
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
     * 
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws NullPointerException
     * @throws TimeoutException
     */
    public void testResultListenerReportsSuccessProperly() throws NullPointerException, InterruptedException, ExecutionException, TimeoutException {
        new MockRestClient()
                .enqueue(new RestRequest<Object>()
                        .setFilter(new BasicPodioFilter())
                        .setOperation(RestClient.Operation.GET))
                .withResultListener(resultListener)
                .get(100, TimeUnit.MILLISECONDS);

        Mockito.verify(resultListener, Mockito.timeout(100)).onRequestPerformed(null);
        Mockito.verifyNoMoreInteractions(resultListener);
    }
}
