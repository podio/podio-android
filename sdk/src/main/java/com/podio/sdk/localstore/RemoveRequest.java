
package com.podio.sdk.localstore;

import android.util.LruCache;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * A specific {@link LocalStoreRequest}, targeting the "remove value" operation. This implementation
 * silently removes a value from both the memory cache as well as the disk cache. If the disk store
 * isn't prepared yet, the disk write request will block until the disk store is ready.
 *
 */
final class RemoveRequest extends LocalStoreRequest<Void> {

    /**
     * Removes the value associated with the given key from the memory cache as well as the disk
     * store. If the disk store isn't initialized yet, the disk write operation will block until
     * it's prepared.
     *
     * @param storeEnabler
     *         The callback that will provide the memory and disk stores.
     * @param key
     *         The key of the value to remove.
     *
     * @throws IOException
     *         If the file system access fails for some reason.
     */
    private static final void removeValue(RuntimeStoreEnabler storeEnabler, Object key) throws IOException {
        LruCache<Object, Object> memoryStore = storeEnabler.getMemoryStore();
        if (memoryStore == null) {
            throw new IllegalStateException("You're trying to remove content from a closed store.");
        }

        memoryStore.remove(key);

        // Remove from disk. Make sure we wait for the disk store to be ready before we start
        // accessing it.
        synchronized (storeEnabler.getDiskStoreLock()) {
            File diskStore = storeEnabler.getDiskStore();
            if (isReadableDirectory(diskStore)) {
                String fileName = getFileName(key);
                File file = new File(diskStore, fileName);
                file.delete();
            }
        }
    }

    /**
     * Creates a new Request for removing a value from the local store.
     *
     * @param storeEnabler
     *         The callback that will provide the memory and disk stores.
     * @param key
     *         The key of the value.
     *
     * @return A request ready for being enqueued in a queue.
     */
    RemoveRequest(final RuntimeStoreEnabler storeEnabler, final Object key) {
        super(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                removeValue(storeEnabler, key);
                return null;
            }
        });
    }
}
