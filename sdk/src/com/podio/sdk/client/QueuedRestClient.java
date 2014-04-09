package com.podio.sdk.client;

import java.util.concurrent.LinkedBlockingQueue;

import android.os.Handler;

import com.podio.sdk.RestClient;
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

    /**
     * Consumes request from the request queue and executes them (according to
     * abstract implementations) on a worker thread.
     * 
     * @author László Urszuly
     */
    private final class QueuedRestConsumer implements Runnable {
        @Override
        public void run() {
            while (true) {
                state = State.IDLE;
                try {
                    RestRequest request = queue.take();

                    if (request != null) {
                        state = State.PROCESSING;

                        // Let the extending class define how to process the
                        // request and analyze the result.
                        Object ticket = request.getTicket();
                        ResultListener resultListener = request.getResultListener();
                        RestResult result = handleRequest(request);
                        reportResult(ticket, resultListener, result);
                    }
                } catch (InterruptedException e) {
                }
            }
        }
    }

    protected final String scheme;
    protected final String authority;

    private final Handler callerHandler;
    private final LinkedBlockingQueue<RestRequest> queue;
    private final Thread consumerThread;

    private State state;

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
        this.callerHandler = new Handler();
        this.queue = new LinkedBlockingQueue<RestRequest>(capacity);
        this.consumerThread = new Thread(new QueuedRestConsumer(), "consumer");
        this.consumerThread.start();
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
    public boolean perform(RestRequest request) {
        boolean isPushed = request != null && queue.offer(request);
        return isPushed;
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
    protected abstract RestResult handleRequest(RestRequest restRequest);

    /**
     * Reports a result back to any callback implementation.
     * 
     * @param ticket
     *            The request ticket to pass back to the caller.
     * @param resultListener
     *            The callback implementation to call through.
     * @param result
     *            The result of the request.
     */
    protected void reportResult(final Object ticket, final ResultListener resultListener,
            final RestResult result) {

        if (resultListener != null) {
            // Make sure to post to the caller thread.
            callerHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (result == null) {
                        resultListener.onFailure(ticket, null);
                    } else if (result.isSuccess()) {
                        resultListener.onSuccess(ticket, result.items());
                    } else {
                        resultListener.onFailure(ticket, result.message());
                    }
                }
            });
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
}
