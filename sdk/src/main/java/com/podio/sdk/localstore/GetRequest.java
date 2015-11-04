
package com.podio.sdk.localstore;

import android.util.LruCache;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * A specific {@link com.podio.sdk.localstore.LocalStoreRequest LocalStoreRequest} implementation,
 * targeting the "get value" operation. This implementation tries to fetch a value from the memory
 * cache first and if nothing is found there it proceeds to look in the disk store. If the disk
 * store isn't prepared yet, the disk read request will block until the disk store is ready. If the
 * disk store is ready and contains a value, it tries to fetch it from there and put it in the
 * memory cache before returning it to the caller.
 *
 * @author László Urszuly
 */
final class GetRequest<T> extends LocalStoreRequest<T> {

    /**
     * Retrieves a value, associated with the given key, from the memory cache. If not found, an
     * attempt to fetch the value from the disk store is made. This method call will block until the
     * disk store is prepared. On success the value will be put in the memory cache. If no value is
     * found neither in the memory, nor on disk, null is returned.
     *
     * @param storeEnabler
     *         The callback that will provide the memory and disk stores.
     * @param key
     *         The key for the value to fetch.
     * @param classOfValue
     *         The {@link Class} template to parse the disk store JSON to.
     *
     * @return The value associated with the key or null if none found.
     *
     * @throws IOException
     *         If reading from disk store failed for some reason.
     * @throws ClassCastException
     *         If the value can't be cast to the requested template type.
     */
    @SuppressWarnings("unchecked")
    private static final <E> E getValue(RuntimeStoreEnabler storeEnabler, Object key, Class<E> classOfValue) throws IOException, ClassCastException {
        LruCache<Object, Object> memoryStore = storeEnabler.getMemoryStore();
        if (memoryStore == null) {
            throw new IllegalStateException("You're trying to fetch content from a closed store.");
        }

        E value = (E) memoryStore.get(key);

        // If nothing found in the memory cache, try to read from disk.
        if (value == null) {
            // Make sure we wait for the disk store to be ready before we start accessing it.
            synchronized (storeEnabler.getDiskStoreLock()) {
                File diskStore = storeEnabler.getDiskStore();
                if (isReadableDirectory(diskStore) && isValidTemplate(classOfValue)) {
                    // Read object from disk...
                    String fileName = getFileName(key);
                    File file = new File(diskStore, fileName);
                    value = readObjectFromDisk(file, classOfValue);

                    // ...and also update in memory.
                    if (value != null) {
                        memoryStore.put(key, value);
                    }
                }
            }
        }

        return value;

    }

    /**
     * Creates a new Request for retrieving a value from the local store. The request will deliver
     * the requested object, or a null-pointer if no object is found by the given key.
     *
     * @param storeEnabler
     *         The callback that will provide the memory and disk stores.
     * @param key
     *         The key of the value.
     * @param classOfValue
     *         The type to parse the file into (if needed).
     */
    GetRequest(final RuntimeStoreEnabler storeEnabler, final Object key, final Class<T> classOfValue) {
        super(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return getValue(storeEnabler, key, classOfValue);
            }
        });
    }
}
