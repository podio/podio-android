package com.podio.test;

import java.util.concurrent.Semaphore;

public class TestUtils {

    private static final long DELAY_DEFAULT = 2000;

    private static Semaphore semaphore;

    private static void alertWatchDog(long delayMillis) {
        final long delay = delayMillis > 0 ? delayMillis : DELAY_DEFAULT;

        new Thread(new Runnable() {
            @Override
            public void run() {
                calmDown(delay);
                releaseBlockedThread();
            }
        }).start();
    }

    /**
     * Alerts the watch dog and blocks the current thread by acquiring the only
     * semaphore immediately. The watch dog will force-release the blockade
     * after two seconds.
     */
    public static void blockThread(long delayMillis) {
        if (semaphore == null) {
            semaphore = new Semaphore(0);
        }

        alertWatchDog(delayMillis);
        semaphore.acquireUninterruptibly();
    }

    public static void blockThread() {
        blockThread(DELAY_DEFAULT);
    }

    /**
     * Release the semaphore now and remove any pending force releases from the
     * watch dog.
     */
    public static void releaseBlockedThread() {
        if (semaphore != null) {
            semaphore.release();
        }
    }

    /**
     * Just calm down a bit, and wait the time it takes to count to ten (in
     * milliseconds).
     */
    public static void calmDown(long timeToTen) {
        try {
            Thread.sleep(timeToTen);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Just calm down a bit, and wait for 200 ms.
     */
    public static void calmDown() {
        calmDown(100);
    }
}
