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
import java.util.concurrent.TimeUnit;

public class TestUtils {

    private static final long DELAY_DEFAULT = 2000;

    private static Semaphore semaphore;
    
    private TestUtils() {
    }
    
    public static void reset() {
    	semaphore = new Semaphore(0);
    }

    /**
     * Alerts the watch dog and blocks the current thread by acquiring the only
     * semaphore immediately. The watch dog will force-release the blockade
     * after two seconds.
     */
    public static boolean waitUntilCompletion(long delayMillis) {
        try {
			return semaphore.tryAcquire(delayMillis, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			return false;
		}
    }

    public static boolean waitUntilCompletion() {
        return waitUntilCompletion(DELAY_DEFAULT);
    }

    /**
     * Release the semaphore now and remove any pending force releases from the
     * watch dog.
     */
    public static void completed() {
    	semaphore.release();
    }
}
