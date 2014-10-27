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

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import android.util.LruCache;

/**
 * A specific {@link LocalStoreRequest}, targeting the "get value" operation.
 * This implementation tries to fetch a value from the memory cache first and if
 * nothing is found there it proceeds to look in the disk store. If the disk
 * store contains a value, it tries to fetch it from there and puts it in the
 * memory cache before returning it to the caller.
 * 
 * @author László Urszuly
 */
final class GetRequest<T> extends LocalStoreRequest<T> {

    /**
     * Retrieves a value, associated with the given key, from the memory cache.
     * If not found, an attempt to fetch the value from the disk store is made.
     * On success the value will be put in the memory cache. If no value is
     * found neither in the memory cache, nor in the disk store, null is
     * returned.
     * 
     * @param memoryStore
     *        The memory cache to fetch from.
     * @param diskStore
     *        The disk store to fetch from.
     * @param key
     *        The key for the value to fetch.
     * @param classOfValue
     *        The {@link Class} template to parse the disk store JSON to.
     * @return The value associated with the key or null if none found.
     * @throws IOException
     *         If reading from disk store failed for some reason.
     * @throws ClassCastException
     *         If the value can't be cast to the requested template type.
     */
    @SuppressWarnings("unchecked")
    private static final <E> E getValue(LruCache<Object, Object> memoryStore, File diskStore, Object key,
            Class<E> classOfValue) throws IOException, ClassCastException {

        E value = null;

        // Try to read from memory.
        if (memoryStore != null) {
            value = (E) memoryStore.get(key);
        }

        // If failed try to read from disk.
        if (value == null && isValidTemplate(classOfValue) && isReadableDirectory(diskStore)) {
            String fileName = getFileName(key);
            File file = new File(diskStore, fileName);
            value = readObjectFromDisk(file, classOfValue);

            // Update memory.
            if (value != null) {
                memoryStore.put(key, value);
            }
        }

        return value;

    }

    /**
     * Creates a new Request for retrieving a value from the local store. The
     * request will deliver the requested object, or a null-pointer if no object
     * is found by the given key.
     * 
     * @param memoryStore
     *        The in-memory store.
     * @param diskStore
     *        The file handle to the disk store directory.
     * @param key
     *        The key of the value.
     * @param classOfValue
     *        The type to parse the file into (if needed).
     */
    GetRequest(final LruCache<Object, Object> memoryStore, final File diskStore, final Object key,
            final Class<T> classOfValue) {

        super(new Callable<T>() {

            @Override
            public T call() throws Exception {
                validateState(memoryStore, diskStore);
                return getValue(memoryStore, diskStore, key, classOfValue);
            }

        });
    }
}
