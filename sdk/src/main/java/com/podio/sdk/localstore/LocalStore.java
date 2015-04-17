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

import android.content.Context;
import android.util.LruCache;

import com.podio.sdk.QueueClient;
import com.podio.sdk.Request;
import com.podio.sdk.Store;
import com.podio.sdk.internal.Utils;
import com.podio.sdk.json.JsonParser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

/**
 * A {@link Store} implementation modeling a memory-cache backed by persistent disk storage. The
 * memory cache heavily relies on the Android {@link LruCache} while the disk store is a basic
 * directory in the internal cache directory of the app. The actual contents are saved as JSON files
 * in sub-directories.
 * <p>
 * The {@link Store} interface enables means of adding, removing, and fetching content to and from
 * the store. Further more the caller can choose to close the store to free up memory. This will
 * clear the memory cache but leave the disk store intact. The user can also choose to erase the
 * store. This will wipe all data from both memory and disk store, but only for the given store.
 * Finally the user is offered the possibility to erase all disk stores which will wipe the entire
 * local store directory on the file system.
 * <p>
 * What is put in the store is completely up to the developer. There are no constraints nor
 * requirements on the data to have any association to Podio domain objects. The only general
 * requirement is for the key objects to have a constant string representation. Also bare in mind
 * that the disk store will convert the objects into JSON string notation and persist them as such.
 * This means that only those parts of your objects will be persisted to disk that can be expressed
 * as JSON.
 *
 * @author László Urszuly
 */
public class LocalStore extends QueueClient implements Store, LocalStoreRequest.RuntimeStoreEnabler {
    private static final String LOCAL_STORES_DIRECTORY = "stores";

    interface RuntimeStorePersister {

        void setMemoryStore(LruCache<Object, Object> memoryStore);

        void setDiskStore(File diskStore);

        Object getDiskStoreLock();
    }

    /**
     * Erases all local stores in the root store folder for this app.
     *
     * @param context
     *         The context used to find the cache directory for this app.
     */
    public static Request<Void> eraseAllDiskStores(Context context) {
        final String systemCachePath = context.getCacheDir().getPath();
        final File root = new File(systemCachePath + File.separator + LOCAL_STORES_DIRECTORY);

        EraseRequest request = LocalStoreRequest.newEraseRequest(new LocalStoreRequest.RuntimeStoreEnabler() {
            @Override
            public LruCache<Object, Object> getMemoryStore() {
                return null;
            }

            @Override
            public File getDiskStore() {
                return root;
            }

            @Override
            public Object getDiskStoreLock() {
                return new Object();
            }
        });

        LocalStore store = new LocalStore();
        store.execute(request);
        return request;
    }

    /**
     * Creates a new instance of this class and configures its initial state. This is the only way
     * to create and initialize a <code>LocalStore</code>.
     *
     * @param context
     *         Used to fetch the disk storage folder.
     * @param name
     *         The name of the store.
     * @param maxMemoryInKiloBytes
     *         The memory size constraint.
     */
    public static Store open(final Context context, final String name, int maxMemoryInKiloBytes) {
        String directoryName;

        try {
            directoryName = URLEncoder.encode(name, Charset.defaultCharset().name());
        } catch (UnsupportedEncodingException e) {
            return null;
        }

        String systemCachePath = context.getCacheDir().getPath();
        String storePath = systemCachePath + File.separator + LOCAL_STORES_DIRECTORY + File.separator + directoryName;

        final LocalStore store = new LocalStore();
        InitRequest request = LocalStoreRequest.newInitRequest(storePath, maxMemoryInKiloBytes,
                new RuntimeStorePersister() {
                    @Override
                    public void setMemoryStore(LruCache<Object, Object> memoryStore) {
                        // This callback is executed on the worker thread.
                        store.memoryStore = memoryStore;
                    }

                    @Override
                    public void setDiskStore(File diskStore) {
                        // This callback is executed on the worker thread.
                        store.diskStore = diskStore;
                        copyMemoryStoreToDiskStore(store);
                    }

                    @Override
                    public Object getDiskStoreLock() {
                        // This callback is executed on the worker thread.
                        return store.getDiskStoreLock();
                    }

                }

        );

        store.execute(request);
        return store;
    }

    /**
     * Writes the object to a {@code ByteArrayOutputStream} and counts the number of bytes in the
     * output. This value will then be converted to KB and returned as the size of the object.
     *
     * @param object
     *         The object to calculate the size of.
     *
     * @return The size of the object expressed in kilobytes.
     */

    private static int calculateKiloByteSizeOfObject(Object object) {
        try {
            // This will only calculate the size of the object itself. The size of any
            // referenced objects are not included in that figure.
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
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

    /**
     * Writes all entries currently in the memory cache to the disk cache. Note that this method
     * performs file system operations on the calling thread.
     *
     * @param store
     *         The local store object holding the memory and disk caches.
     */
    private static void copyMemoryStoreToDiskStore(LocalStore store) {
        if (store != null) {
            Map<Object, Object> snapshot = store.memoryStore.snapshot();
            Set<Map.Entry<Object, Object>> entries = snapshot.entrySet();

            for (Map.Entry<Object, Object> entry : entries) {
                FileOutputStream fileOutputStream = null;
                String fileName;

                try {
                    String key = entry.getKey().toString();
                    fileName = URLEncoder.encode(key, Charset.defaultCharset().name());
                    File file = new File(store.diskStore, fileName);
                    String json = JsonParser.toJson(entry.getValue());
                    fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(json.getBytes());
                } catch (UnsupportedEncodingException e) {
                    break;
                } catch (FileNotFoundException e) {
                    // Intentionally consume this exception.
                } catch (IOException e) {
                    // Intentionally consume this exception.
                } finally {
                    Utils.closeSilently(fileOutputStream);
                }
            }
        }

    }

    /**
     * Returns the {@code File} handle to a directory corresponding to the given name in the system
     * cache directory on this device. The {@code name} parameter will be URL encoded prior to any
     * usage. If no directory is found with the URL encoded {@code name}, then an attempt to create
     * it will be made. If anything goes wrong null is returned.
     *
     * @param context
     *         The context used to find the cache directory for this app.
     * @param name
     *         The name of the store.
     *
     * @return A {@code File} handle to the disk-store directory, or null on failure.
     */
    private static File getLocalStoreDirectory(Context context, String name) {
        if (context == null || Utils.isEmpty(name)) {
            return null;
        }

        String directoryName;

        try {
            directoryName = URLEncoder.encode(name, Charset.defaultCharset().name());
        } catch (UnsupportedEncodingException e) {
            return null;
        }

        String systemCachePath = context.getCacheDir().getPath();
        File storesRoot = new File(systemCachePath + File.separator + LOCAL_STORES_DIRECTORY);
        File diskStore = new File(storesRoot, directoryName);

        if (diskStore.exists()) {
            return diskStore.isDirectory() && diskStore.canWrite() ? diskStore : null;
        } else if (diskStore.mkdirs()) {
            return diskStore.canWrite() ? diskStore : null;
        } else {
            return null;
        }

    }

    private final Object diskStoreLock;

    private LruCache<Object, Object> memoryStore;
    private File diskStore;

    /**
     * Hidden constructor.
     */
    private LocalStore() {
        super(1, 1, 0L);
        diskStoreLock = new Object();
    }

    /**
     * Removes all objects in the memory cache. The disk store is left unaffected.
     *
     * @throws IllegalStateException
     *         If neither in-memory store, nor disk store has a valid handle.
     */
    @Override
    public Request<Void> free() {
        FreeRequest request = LocalStoreRequest.newFreeRequest(memoryStore);
        execute(request);
        return request;
    }

    /**
     * Destroys this instance of the local store. The in memory cache will be cleared and all files
     * in the disk store, as well as the store container itself, will be deleted.
     *
     * @throws IllegalStateException
     *         If neither in-memory store, nor disk store has a valid handle.
     */
    @Override
    public Request<Void> erase() {
        EraseRequest request = LocalStoreRequest.newEraseRequest(this);
        execute(request);
        return request;
    }

    /**
     * Retrieves an object with the given key from the local store. If the object isn't found in
     * memory, and a {@link Class} template is given, it will be looked for on disk. If it's not
     * found there either, a null pointer will be returned.
     *
     * @throws IllegalStateException
     *         If neither in-memory store, nor disk store has a valid handle.
     */
    @Override
    public <T> Request<T> get(Object key, Class<T> classOfValue) throws IllegalStateException {
        GetRequest<T> request = LocalStoreRequest.newGetRequest(this, key, classOfValue);
        execute(request);
        return request;
    }

    /**
     * Provides a disk store object.
     *
     * @return A reference to the current disk store object.
     */
    @Override
    public File getDiskStore() {
        return diskStore;
    }

    /**
     * Provides a lock to synchronize any disk operations on.
     *
     * @return The "Disk-store-ready" lock object.
     */
    @Override
    public Object getDiskStoreLock() {
        return diskStoreLock;
    }

    /**
     * Provides a memory store object.
     *
     * @return A reference to the current memory store object.
     */
    @Override
    public LruCache<Object, Object> getMemoryStore() {
        return memoryStore;
    }

    /**
     * Removes an object with the given key from the local store. If the removed value was found in
     * the memory store, then it will be returned, else if it exists in the disk store and a {@link
     * Class} template is given, the disk store version will be returned prior to deletion.
     *
     * @throws IllegalStateException
     *         If neither in-memory store, nor disk store has a valid handle.
     */
    @Override
    public Request<Void> remove(Object key) throws IllegalStateException {
        RemoveRequest request = LocalStoreRequest.newRemoveRequest(this, key);
        execute(request);
        return request;
    }

    /**
     * Adds or updates a value with the given key in the local store. If there already is a value
     * for the given key in the store, it will silently be overwritten.
     *
     * @throws IllegalStateException
     *         If neither in-memory store, nor disk store has a valid handle.
     */
    @Override
    public Request<Void> set(Object key, Object value) throws IllegalStateException {
        SetRequest request = LocalStoreRequest.newSetRequest(this, key, value);
        execute(request);
        return request;
    }

    /**
     * Returns whether the disk store is initialized and ready for use. If not, the memory store may
     * still cache and return any objects, even though the disk store won't.
     *
     * @return True if the disk store is ready, false otherwise.
     */
    public boolean isDiskStoreReady() {
        return diskStore != null && diskStore.exists() && diskStore.isDirectory() && diskStore.canWrite();
    }

    /**
     * Returns whether the memory store is initialized and ready for use.
     *
     * @return True if the memory cache is ready, false otherwise.
     */
    public boolean isMemoryStoreReady() {
        return memoryStore != null && memoryStore.size() < memoryStore.maxSize();
    }

}
