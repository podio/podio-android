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
 * A specific {@link LocalStoreRequest}, targeting the "remove value" operation.
 * This implementation silently removes a value from both the memory cache and
 * the persistent disk storage.
 * 
 * @author László Urszuly
 */
final class RemoveRequest extends LocalStoreRequest<Void> {

    /**
     * Removes the value associated with the given key from the memory cache as
     * well as the disk store.
     * 
     * @param memoryStore
     *        The memory cache.
     * @param diskStore
     *        The disk store.
     * @param key
     *        The key of the value to remove.
     * @throws IOException
     *         If the file system access fails for some reason.
     */
    private static final void removeValue(LruCache<Object, Object> memoryStore, File diskStore, Object key) throws IOException {
        // Remove from memory.
        if (memoryStore != null) {
            memoryStore.remove(key);
        }

        // Remove from disk.
        if (isReadableDirectory(diskStore)) {
            String fileName = getFileName(key);
            File file = new File(diskStore, fileName);
            file.delete();
        }
    }

    /**
     * Creates a new Request for removing a value from the local store.
     * 
     * @param memoryStore
     *        The in-memory store.
     * @param diskStore
     *        The file handle to the disk store directory.
     * @param key
     *        The key of the value.
     * @return A request ready for being enqueued in a queue.
     */
    RemoveRequest(final LruCache<Object, Object> memoryStore, final File diskStore, final Object key) {
        super(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                removeValue(memoryStore, diskStore, key);
                return null;
            }

        });
    }
}
