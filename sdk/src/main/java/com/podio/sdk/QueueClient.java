
package com.podio.sdk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class QueueClient {

    /**
     * The queue executor service that manages the request queue.
     */
    private ExecutorService executorService;

    protected QueueClient(int corePoolSize, int maxPoolSize, long waitTimeSeconds) {
        executorService = new ThreadPoolExecutor(corePoolSize, maxPoolSize, waitTimeSeconds, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE));
    }

    protected void execute(FutureTask<?> request) {
        executorService.execute(request);
    }

}
