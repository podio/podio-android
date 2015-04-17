/*
 * Copyright (C) 2015 Citrix Systems, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.podio.sdk.localstore;

import android.util.LruCache;

import com.podio.sdk.internal.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;

/**
 * A specific {@link LocalStoreRequest LocalStoreRequest} implementation, targeting the "get value"
 * operation. This implementation tries to fetch a value from the memory cache first and if nothing
 * is found there it proceeds to look in the disk store. If the disk store isn't prepared yet, the
 * request will block until the disk store is ready. If the disk store is ready and contains a
 * value, it tries to fetch it from there and put it in the memory cache before returning it to the
 * caller.
 *
 * @author László Urszuly
 */
final class InitRequest extends LocalStoreRequest<Void> {

    /**
     * Returns a {@link java.io.File File} handle to the provided file path string. If no directory
     * is found with the given name, then an attempt to create it will be made. If anything goes
     * wrong null is returned.
     *
     * @param storePath
     *         The absolute path to the store.
     *
     * @return The disk store.
     */
    private static File createNewDiskStore(String storePath) {
        if (Utils.isEmpty(storePath)) {
            return null;
        }

        File diskStore = new File(storePath);

        if (diskStore.exists()) {
            return diskStore.isDirectory() && diskStore.canWrite() ? diskStore : null;
        } else if (diskStore.mkdirs()) {
            return diskStore.canWrite() ? diskStore : null;
        } else {
            return null;
        }
    }

    /**
     * Returns a {@link android.util.LruCache LruCache} providing the in-memory store.
     *
     * @param maxMemoryInKiloBytes
     *         The maximum allowed size of the memory cache.
     *
     * @return The memory store.
     */
    private static LruCache<Object, Object> createNewMemoryStore(int maxMemoryInKiloBytes) {
        return new LruCache<Object, Object>(maxMemoryInKiloBytes) {
            @Override
            protected int sizeOf(Object key, Object value) {
                try {
                    // This will only calculate the size of the object itself. The size of any
                    // referenced objects are not included in that figure.
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    objectOutputStream.writeObject(value);
                    objectOutputStream.flush();

                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    objectOutputStream.close();

                    return byteArray.length / 1024;
                } catch (NullPointerException e) {
                    return 0;
                } catch (IOException e) {
                    // We don't know anything about the size, assume worst case scenario.
                    return Integer.MAX_VALUE;
                }
            }
        };
    }

    /**
     * Creates a new Request for retrieving a value from the local store. The request will deliver
     * the requested object, or a null-pointer if no object is found by the given key.
     *
     * @param storePath
     *         The absolute path to the disk cache.
     * @param maxMemoryInKiloBytes
     *         The maximum allowed size of the memory cache.
     * @param storePersister
     *         The callback interface to deliver created stores through.
     */
    InitRequest(final String storePath, final int maxMemoryInKiloBytes, final LocalStore.RuntimeStorePersister storePersister) {
        super(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                storePersister.setMemoryStore(createNewMemoryStore(maxMemoryInKiloBytes));
                synchronized (storePersister.getDiskStoreLock()) {
                    storePersister.setDiskStore(createNewDiskStore(storePath));
                }
                return null;
            }

        });
    }
}
