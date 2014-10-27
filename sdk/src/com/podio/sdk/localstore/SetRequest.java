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
 * A specific {@link LocalStoreRequest}, targeting the "store value" operation.
 * This implementation adds a value to both the memory cache as well as the
 * persistent disk storage, silently overwriting any previous values with the
 * same key.
 * 
 * @author László Urszuly
 */
final class SetRequest extends LocalStoreRequest<Void> {

    /**
     * Puts the given value in the memory cache and persists it in the disk
     * store. Any previous values are silently overwritten.
     * 
     * @param memoryStore
     *        The memory cache.
     * @param diskStore
     *        The disk store.
     * @param key
     *        The key of the value to store.
     * @param value
     *        The value to store.
     * @return The currently store object (may be null if the value couldn't be
     *         properly stored).
     * @throws IOException
     *         If the file system operation fails for some reason.
     */
    private static final void setValue(LruCache<Object, Object> memoryStore, File diskStore, Object key, Object value) throws IOException {
        // Update memory
        if (memoryStore != null) {
            memoryStore.put(key, value);
        }

        // Update disk.
        if (diskStore != null) {
            String fileName = getFileName(key);
            File file = new File(diskStore, fileName);
            writeObjectToDisk(file, value);
        }
    }

    /**
     * Creates a new request for storing a given value.
     * 
     * @param memoryStore
     *        The in-memory store.
     * @param diskStore
     *        The file handle to the disk store directory.
     * @param key
     *        The key of the value.
     * @param value
     *        The value to store.
     */
    SetRequest(final LruCache<Object, Object> memoryStore, final File diskStore, final Object key, final Object value) {
        super(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                validateState(memoryStore, diskStore);
                setValue(memoryStore, diskStore, key, value);
                return null;
            }

        });
    }
}
