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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.os.Handler;

import com.podio.sdk.RestClient;
import com.podio.sdk.domain.Session;
import com.podio.sdk.internal.request.ResultListener;

/**
 * Facilitates default means of queuing up requests, flirting with the classical
 * Producer Consumer design pattern. This implementation of the Rest client acts
 * as a producer to its own queue and holds a private consumer as well.
 * 
 * The consumer runs on a worker thread which means that the actual execution of
 * a request will not interfere with with the main thread execution.
 * 
 * The responsibility of which thread the result is delivered on is delegated to
 * the actual request implementation.
 * 
 * The responsibility of analyzing the request state is delegated to extending
 * classes.
 * 
 * @author László Urszuly
 */
public abstract class QueuedRestClient implements RestClient {

	public static final int CAPACITY_DEFAULT = 1;

	public static enum State {
		IDLE, PROCESSING
	}
	
	private final String scheme;
	private final String authority;

	private final BlockingQueue<Runnable> queue;

	private final Executor executor;

	private final Handler callerHandler;

	private State state = State.IDLE;

	/**
	 * Initializes the request queue with the
	 * {@link QueuedRestClient#CAPACITY_DEFAULT} capacity. Any requests that are
	 * passed on to a full queue will be rejected.
	 * 
	 * @param scheme
	 *            The scheme of this {@link RestClient}.
	 * @param authority
	 *            The authority of this {@link RestClient}.
	 */
	public QueuedRestClient(String scheme, String authority) {
		this(scheme, authority, CAPACITY_DEFAULT);
	}

	/**
	 * Initializes the request queue with the given capacity. Any requests that
	 * are passed on to a full queue will be rejected.
	 * 
	 * @param scheme
	 *            The scheme of this {@link RestClient}.
	 * @param authority
	 *            The authority of this {@link RestClient}.
	 * @param queueCapacity
	 *            The capacity of the request queue. If the provided capacity is
	 *            less than or equal to zero, then the
	 *            {@link QueuedRestClient#CAPACITY_DEFAULT} will be used
	 *            instead.
	 */
	public QueuedRestClient(String scheme, String authority, int queueCapacity) {
		int capacity = queueCapacity > 0 ? queueCapacity : CAPACITY_DEFAULT;

		this.scheme = scheme;
		this.authority = authority;

		this.queue = new LinkedBlockingQueue<Runnable>(capacity);
		this.executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, queue);
		this.callerHandler = new Handler();
	}

	/**
	 * {@inheritDoc RestClient#getScheme()}
	 */
	@Override
	public String getScheme() {
		return scheme;
	}

	/**
	 * {@inheritDoc RestClient#getAuthority()}
	 */
	@Override
	public String getAuthority() {
		return authority;
	}

	/**
	 * {@inheritDoc RestClient#perform(RestRequest)}
	 */
	@Override
	public <T> boolean enqueue(RestRequest<T> request) {
		if (request == null) {
			throw new NullPointerException("request cannot be null");
		}
		request.validate();

		try {
			this.executor.execute(new RequestRunner<T>(request));

			return true;
		} catch (RejectedExecutionException e) {
			// TODO: Consider just propagating this exception
			return false;
		}
	}

	/**
	 * Processes the given result and analyzes the result of it, returning a
	 * generic success/failure bundle.
	 * 
	 * @param restRequest
	 *            The request to process.
	 * @return A simplified result object which reflects the final processing
	 *         state of the request.
	 */
	protected abstract <T> RestResult<T> handleRequest(RestRequest<T> restRequest);
	
	/**
	 * Reports a result using the callerHandler.
	 * 
	 * @param request
	 *            The request that we are reporting the result of.
	 * @param result
	 *            The result of the request.
	 */
	protected <T> void reportResult(final RestRequest<T> request, final RestResult<T> result) {
		callerHandler.post(new Runnable() {
			@Override
			public void run() {
				callListener(request, result);
			}
		});
	}
	
	
	/**
	 * Reports a result back to any listeners implementation.
	 * 
	 * @param request
	 *            The request that we are reporting the result of.
	 * @param result
	 *            The result of the request.
	 */
	protected <T> void callListener(final RestRequest<T> request, final RestResult<T> result) {
		ResultListener<? super T> resultListener = request.getResultListener();
		Session session = result.session();
		if (session != null) {
			resultListener.onSessionChange(request.getTicket(), session);
		}

		if (result.isSuccess()) {
			resultListener.onSuccess(request.getTicket(), result.item());
		} else {
			resultListener.onFailure(request.getTicket(), result.message());
		}
	}

	/**
	 * Gives information on the current occupied size of the request queue.
	 * 
	 * @return The number of requests in the queue.
	 */
	public int size() {
		return queue.size();
	}

	/**
	 * Gives information on the current state of the consumer thread.
	 * 
	 * @return The current state.
	 */
	public State state() {
		return state;
	}

	private class RequestRunner<T> implements Runnable {

		private final RestRequest<T> request;

		private RequestRunner(RestRequest<T> request) {
			this.request = request;
		}

		@Override
		public void run() {
			state = State.PROCESSING;
			try {			
				final RestResult<T> result = handleRequest(request);
	
				// The user is no longer authorized. Remove any pending
				// requests before proceeding.
				Session session = result.session();
				if (session != null && !session.isAuthorized()) {
					queue.clear();
				}
	
				reportResult(request, result);
			} finally {			
				state = State.IDLE;
			}
		}
	}
}
