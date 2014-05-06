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
    private static void calmDown(long timeToTen) {
        try {
            Thread.sleep(timeToTen);
        } catch (InterruptedException e) {
        }
    }
}
