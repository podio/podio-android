
package com.podio.sdk.localstore;

import android.util.LruCache;

import java.util.concurrent.Callable;

/**
 * A specific {@link com.podio.sdk.localstore.LocalStoreRequest LocalStoreRequest} implementation,
 * targeting the "close store" operation. This implementation simply clears the memory cache while
 * leaving the persistent disk store untouched.
 *
 */
final class FreeRequest extends LocalStoreRequest<Void> {

    /**
     * Creates a new Request for closing the local store, clearing the memory store. The disk store
     * is left intact. The request will not deliver anything.
     *
     * @param memoryStore
     *         The reference to the memory cache object.
     */
    FreeRequest(final LruCache<Object, Object> memoryStore) {
        super(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                if (memoryStore == null) {
                    throw new IllegalStateException("You're trying to free up a closed store.");
                }

                memoryStore.evictAll();
                return null;
            }
        });
    }

}
