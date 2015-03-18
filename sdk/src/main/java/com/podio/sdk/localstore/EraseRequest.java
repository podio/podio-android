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

import android.util.LruCache;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * A specific {@link LocalStoreRequest}, targeting the "destroy store" operation. This
 * implementation clears the memory cache and wipes all files from the persistent disk store that
 * belongs to this very store, leaving other stores intact. This class also removes the actual store
 * sub directory from the file system.
 *
 * @author László Urszuly
 * @see .com.podio.sdk.localstore.InitDiskRequest
 */
final class EraseRequest extends LocalStoreRequest<Void> {

    /**
     * Evicts all entries in the given memory cache.
     *
     * @param memoryStore
     *         The in-memory cache to clear.
     */
    private static void destroyMemoryStore(LruCache<Object, Object> memoryStore) {
        if (memoryStore != null) {
            memoryStore.evictAll();
        }
    }

    /**
     * Removes all files in the given directory and tries to remove the directory as well.
     *
     * @param diskStore
     *         The disk cache to clear.
     */
    private static void destroyDiskStore(File diskStore) {
        if (isWritableDirectory(diskStore)) {
            File[] files = diskStore.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        destroyDiskStore(file);
                    } else if (file.isFile()) {
                        file.delete();
                    }
                }
            }
        }
    }

    /**
     * Creates a new Request for destroying the local store. The request will not deliver anything.
     *
     * @param memoryStore
     *         The in-memory store.
     * @param diskStore
     *         The file handle to the disk store directory.
     */
    EraseRequest(final LruCache<Object, Object> memoryStore, final File diskStore) {
        super(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                destroyMemoryStore(memoryStore);
                destroyDiskStore(diskStore);
                return null;
            }

        });
    }

}
