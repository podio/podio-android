
package com.podio.sdk.localstore;

import android.util.LruCache;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * A specific {@link com.podio.sdk.localstore.LocalStoreRequest LocalStoreRequest} implementation,
 * targeting the "store value" operation. This implementation adds a value to both the memory cache
 * as well as the disk cache, silently overwriting any previous values with the same key. If the
 * disk store isn't prepared yet, the disk write request will block until the disk store is ready.
 *
 */
final class SetRequest extends LocalStoreRequest<Void> {

    /**
     * Puts the given value in the memory cache and persists it in the disk store. If the disk store
     * isn't initialized yet, the disk write operation will block until it's prepared. Any previous
     * values are silently overwritten.
     *
     * @param storeEnabler
     *         The callback that will provide the memory and disk stores.
     * @param key
     *         The key of the value to store.
     * @param value
     *         The value to store.
     *
     * @throws IOException
     *         If the file system operation fails for some reason.
     */
    private static void setValue(final RuntimeStoreEnabler storeEnabler, Object key, Object value) throws IOException {
        LruCache<Object, Object> memoryStore = storeEnabler.getMemoryStore();
        if (memoryStore == null) {
            throw new IllegalStateException("You're trying to write content to a closed store.");
        }

        memoryStore.put(key, value);

        // Update disk. Make sure we wait for the disk store to be ready before we start accessing
        // it.
        synchronized (storeEnabler.getDiskStoreLock()) {
            File diskStore = storeEnabler.getDiskStore();
            if (isWritableDirectory(diskStore)) {
                String fileName = getFileName(key);
                File file = new File(diskStore, fileName);
                writeObjectToDisk(file, value);
            }
        }
    }

    /**
     * Creates a new request for storing a given value.
     *
     * @param storeEnabler
     *         The callback that will provide the memory and disk stores.
     * @param key
     *         The key of the value.
     * @param value
     *         The value to store.
     */
    SetRequest(final RuntimeStoreEnabler storeEnabler, final Object key, final Object value) {
        super(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                setValue(storeEnabler, key, value);
                return null;
            }
        });
    }
}
