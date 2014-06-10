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

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import android.os.Handler;
import android.os.HandlerThread;
import android.test.InstrumentationTestCase;

import com.podio.sdk.RestClient;
import com.podio.sdk.client.QueuedRestClient.State;
import com.podio.sdk.domain.Session;
import com.podio.sdk.filter.BasicPodioFilter;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.internal.request.ResultListener;
import com.podio.sdk.provider.mock.DummyRestClient;
import com.podio.test.ThreadCaptureResultListener;

public class QueuedRestClientTest extends InstrumentationTestCase {

	@Mock
	ResultListener<Object> listener;
	@Mock
	Session mockSession;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

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
		String expectedScheme = "test://";
		MockRestClient testTarget = new MockRestClient(expectedScheme, null, 1);
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
		MockRestClient testTarget = new MockRestClient(null, expectedAuthority,
				1);
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
		MockRestClient testTarget = new MockRestClient(invalidSize);
		RestRequest<Object> request = new RestRequest<Object>().setFilter(
				new BasicPodioFilter()).setOperation(RestOperation.AUTHORIZE);
		boolean didAcceptRequest = testTarget.enqueue(request);

		assertTrue(didAcceptRequest);
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
		MockRestClient testTarget = new MockRestClient();

		RestRequest<Object> firstRequest = new RestRequest<Object>()
				.setFilter(new BasicPodioFilter("first"))
				.setOperation(RestOperation.GET).setTicket(new Object())
				.setResultListener(listener);
		RestRequest<Object> secondRequest = new RestRequest<Object>()
				.setFilter(new BasicPodioFilter("second"))
				.setOperation(RestOperation.GET).setTicket(new Object())
				.setResultListener(listener);

		boolean enqueued = testTarget.enqueue(firstRequest);
		assertTrue(enqueued);

		enqueued = testTarget.enqueue(secondRequest);
		assertTrue(enqueued);

		Mockito.verify(listener, Mockito.timeout(2000)).onSuccess(
				firstRequest.getTicket(), null);
		Mockito.verify(listener, Mockito.timeout(2000)).onSuccess(
				secondRequest.getTicket(), null);
		Mockito.verifyNoMoreInteractions(listener);
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
		final String[] handlerThreadName = new String[1];

		MockRestClient testTarget = new MockRestClient() {
			@Override
			protected <T> RestResult<T> handleRequest(RestRequest<T> restRequest) {
				handlerThreadName[0] = Thread.currentThread().getName();

				return super.handleRequest(restRequest);
			}
		};

		RestRequest<Object> request = new RestRequest<Object>()
				.setFilter(new BasicPodioFilter()).setResultListener(listener)
				.setOperation(RestOperation.GET);
		testTarget.enqueue(request);

		Mockito.verify(listener, Mockito.timeout(2000)).onSuccess(
				request.getTicket(), null);

		assertFalse(Thread.currentThread().getName()
				.equals(handlerThreadName[0]));
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
	public void testRequestQueuePushFailureOnCapacityReached()
			throws InterruptedException {
		MockRestClient testTarget = new MockRestClient();

		RestRequest<Object> request = new RestRequest<Object>()
				.setFilter(new BasicPodioFilter()).setResultListener(listener)
				.setOperation(RestOperation.AUTHORIZE);

		assertEquals(testTarget.enqueue(request), true);
		// One is being processed, so room for one more in the queue
		assertEquals(testTarget.enqueue(request), true);
		// Now we are out of room
		assertEquals(testTarget.enqueue(request), false);

		Mockito.verify(listener, Mockito.timeout(2000).times(2)).onSuccess(
				request.getTicket(), null);

		// Now it can be processed
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
		MockRestClient testTarget = new MockRestClient();

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
		QueuedRestClient testTarget = Mockito.spy(new MockRestClient());

		RestRequest<Object> request = new RestRequest<Object>()
				.setFilter(new BasicPodioFilter("expected"))
				.setTicket(new Object()).setOperation(RestOperation.GET);

		testTarget.enqueue(request);

		Mockito.verify(testTarget, Mockito.timeout(2000))
				.handleRequest(request);
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
		QueuedRestClient testTarget = new MockRestClient();

		RestRequest<Object> firstRequest = new RestRequest<Object>()
				.setFilter(new BasicPodioFilter("first"))
				.setTicket(new Object()).setResultListener(listener)
				.setOperation(RestOperation.AUTHORIZE);

		boolean enqueued = testTarget.enqueue(firstRequest);
		assertTrue(enqueued);

		Mockito.verify(listener, Mockito.timeout(2000)).onSuccess(
				firstRequest.getTicket(), null);
		assertEquals(State.IDLE, testTarget.state());
		assertEquals(0, testTarget.size());

		RestRequest<Object> secondRequest = new RestRequest<Object>()
				.setFilter(new BasicPodioFilter("second"))
				.setTicket(new Object()).setResultListener(listener)
				.setOperation(RestOperation.AUTHORIZE);

		enqueued = testTarget.enqueue(secondRequest);
		assertTrue(enqueued);

		Mockito.verify(listener, Mockito.timeout(2000)).onSuccess(
				secondRequest.getTicket(), null);
		assertEquals(State.IDLE, testTarget.state());
		assertEquals(0, testTarget.size());
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
		ThreadCaptureResultListener threadListener = Mockito
				.spy(new ThreadCaptureResultListener());

		final RestRequest<Object> request = new RestRequest<Object>()
				.setResultListener(threadListener)
				.setFilter(new BasicPodioFilter())
				.setOperation(RestOperation.GET).setTicket(new Object());

		HandlerThread handlerThread = new HandlerThread("UIThread");
		handlerThread.start();

		Handler handler = new Handler(handlerThread.getLooper());
		handler.post(new Runnable() {
			@Override
			public void run() {
				MockRestClient testTarget = new MockRestClient();
				testTarget.setAsync(true);
				testTarget.enqueue(request);
			}
		});

		Mockito.verify(threadListener, Mockito.timeout(2000)).onSuccess(
				request.getTicket(), null);

		assertEquals(threadListener.getThreadName(), "UIThread");
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
		final String mockMessage = "error message";

		MockRestClient testTarget = new MockRestClient();
		testTarget.setResult(RestResult.failure(mockMessage));

		RestRequest<Object> request = new RestRequest<Object>()
				.setResultListener(listener).setFilter(new BasicPodioFilter())
				.setOperation(RestOperation.GET);
		testTarget.enqueue(request);

		Mockito.verify(listener, Mockito.timeout(2000)).onFailure(
				request.getTicket(), mockMessage);
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
		MockRestClient testTarget = new MockRestClient();
		testTarget.setResult(new RestResult<Object>(true, mockSession, null,
				null));

		RestRequest<Object> request = new RestRequest<Object>()
				.setResultListener(listener).setFilter(new BasicPodioFilter())
				.setOperation(RestOperation.AUTHORIZE);
		testTarget.enqueue(request);

		Mockito.verify(listener, Mockito.timeout(2000)).onSessionChange(
				request.getTicket(), mockSession);
		Mockito.verify(listener, Mockito.timeout(2000)).onSuccess(
				request.getTicket(), null);
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
		RestClient testTarget = new DummyRestClient(RestResult.success());

		RestRequest<Object> request = new RestRequest<Object>()
				.setResultListener(listener).setFilter(new BasicPodioFilter())
				.setOperation(RestOperation.AUTHORIZE);
		testTarget.enqueue(request);

		Mockito.verify(listener, Mockito.timeout(2000)).onSuccess(null, null);
		Mockito.verifyNoMoreInteractions(listener);
	}
}
