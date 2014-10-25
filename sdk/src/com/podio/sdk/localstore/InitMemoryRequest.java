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
package com.podio.sdk.localstore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;

import android.util.LruCache;

/**
 * A specific {@link LocalStoreRequest}, targeting the "initialize memory cache"
 * operation. This implementation instantiates a {@link LruCache} which
 * calculates the size in kilobytes rather than number of contained items.
 * 
 * @author László Urszuly
 */
final class InitMemoryRequest extends LocalStoreRequest<LruCache<Object, Object>> {

    /**
     * Makes a rough estimate of the size of the object in KB's. There doesn't
     * seem to be any mechanism in the Android platform to get this figure
     * easily and reliably. Java 7 has a 'java.lang.instrument' class that could
     * have helped, but those features aren't valid on Android (as Android
     * doesn't run in a Java VM, but rather in a Dalvik VM).
     * 
     * @param object
     *        The object to calculate the size of.
     * @return The size in kilobytes of the object serialized into a byte array,
     *         or zero if the object is null or couldn't be serialized into a
     *         byte array.
     */
    private static int calculateSizeOfByteStream(Object object) {
        if (object == null) {
            return 0;
        }

        try {
            // TODO: Does this take referenced objects into consideration? Most
            // likely not. Would it make sense to cache the JSON representation
            // of the value instead? Which one has the smallest memory
            // footprint?
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            objectOutputStream.close();

            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return (int) (byteArray.length / 1024);
        } catch (IOException e) {
            // We don't know anything about the size.
            return Integer.MAX_VALUE;
        }
    }

    /**
     * Creates a new local store request that initializes the memory cache on a
     * worker thread.
     * 
     * @param maxMemoryAsKiloBytes
     *        The maximum amount of memory, in KB, the memory store will ever
     *        request from the system.
     */
    InitMemoryRequest(final int maxMemoryAsKiloBytes) {
        super(new Callable<LruCache<Object, Object>>() {

            @Override
            public LruCache<Object, Object> call() throws Exception {
                return new LruCache<Object, Object>(maxMemoryAsKiloBytes) {

                    @Override
                    protected int sizeOf(Object key, Object value) {
                        return calculateSizeOfByteStream(value);
                    }

                };
            }

        });
    }

}
