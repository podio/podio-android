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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.os.Handler;

import com.podio.sdk.ErrorListener;
import com.podio.sdk.PodioException;
import com.podio.sdk.RestClient;
import com.podio.sdk.ResultListener;
import com.podio.sdk.SessionListener;
import com.podio.sdk.domain.Session;

/**
 * Facilitates default means of queuing up requests, flirting with the classical
 * Producer Consumer design pattern. This implementation of the Rest client acts
 * as a producer to its own queue and holds a private consumer as well.
 * <p>
 * The consumer runs on a worker thread which means that the actual execution of
 * a request will not interfere with with the main thread execution.
 * <p>
 * The responsibility of which thread the result is delivered on is delegated to
 * the actual request implementation.
 * <p>
 * The responsibility of analyzing the request state is delegated to extending
 * classes.
 * 
 * @author László Urszuly
 */
public abstract class QueuedRestClient implements RestClient {
    private final String scheme;
    private final String authority;

    private final BlockingQueue<Runnable> queue;
    private final ExecutorService executorService;
    private final Handler callerHandler;

    /**
     * @author László Urszuly
     * @param <T>
     */
    private final class RequestCallable<T> implements Callable<RestResult<T>> {

        private final RestRequest<T> request;

        private RequestCallable(RestRequest<T> request) {
            this.request = request;
        }

        @Override
        public RestResult<T> call() throws Exception {
            RestResult<T> result = null;

            try {
                result = handleRequest(request);

                if (result.hasSession() && !result.getSession().isAuthorized()) {
                    // The user is no longer authorized. Remove any pending
                    // requests (allowing any running requests to finish).
                    queue.clear();
                }
            } catch (PodioException e) {
                result = RestResult.failure(e);
            } finally {
                reportResult(request, result);
            }

            return result;
        }
    }

    /**
     * Initializes the request executor service with a {@link Integer#MAX_VALUE}
     * capacity request queue.
     * 
     * @param scheme
     *        The scheme of this {@link RestClient}.
     * @param authority
     *        The authority of this {@link RestClient}.
     */
    public QueuedRestClient(String scheme, String authority) {
        this(scheme, authority, Integer.MAX_VALUE);
    }

    /**
     * Initializes the request executor service.
     * 
     * @param scheme
     *        The scheme of this {@link RestClient}.
     * @param authority
     *        The authority of this {@link RestClient}.
     * @param queueCapacity
     *        The desired capacity of the request queue. Defaults to
     *        {@link Integer#MAX_VALUE} if less than zero.
     */
    public QueuedRestClient(String scheme, String authority, int queueCapacity) {
        this.scheme = scheme;
        this.authority = authority;

        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        int capacity = queueCapacity > 0 ? queueCapacity : Integer.MAX_VALUE;

        this.queue = new LinkedBlockingQueue<Runnable>(capacity);
        this.executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, queue, threadFactory);
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
    public <T> Future<RestResult<T>> enqueue(RestRequest<T> request) throws NullPointerException {
        if (request == null) {
            throw new NullPointerException("request cannot be null");
        }

        RequestCallable<T> callable = new RequestCallable<T>(request);
        return executorService.submit(callable);
    }

    /**
     * Processes the given result and analyzes the result of it, returning a
     * generic success/failure bundle.
     * 
     * @param restRequest
     *        The request to process.
     * @return A simplified result object which reflects the final processing
     *         state of the request.
     */
    protected abstract <T> RestResult<T> handleRequest(RestRequest<T> restRequest) throws PodioException;

    /**
     * Reports a result using the callerHandler.
     * 
     * @param request
     *        The request that we are reporting the result of.
     * @param result
     *        The result of the request.
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
     * Returns the number of requests in the executor queue.
     * 
     * @return
     */
    public int size() {
        return queue.size();
    }

    /**
     * Reports a result back to any listeners implementation.
     * 
     * @param request
     *        The request that we are reporting the result of.
     * @param result
     *        The result of the request.
     */
    protected <T> void callListener(final RestRequest<T> request, final RestResult<T> result) {
        if (result != null) {

            if (result.hasSession()) {
                SessionListener sessionListener = request.getSessionListener();

                if (sessionListener != null) {
                    Session session = result.getSession();
                    sessionListener.onSessionChanged(session);
                }
            }

            if (result.hasException()) {
                ErrorListener errorListener = request.getErrorListener();

                if (errorListener != null) {
                    PodioException error = result.getException();
                    errorListener.onExceptionOccurred(error);
                }
            } else {
                ResultListener<? super T> resultListener = request.getResultListener();

                if (resultListener != null) {
                    T item = result.getItem();
                    resultListener.onRequestPerformed(item);
                }
            }
        }
    }

}
