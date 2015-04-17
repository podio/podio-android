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

import java.util.concurrent.Callable;

/**
 * A specific {@link com.podio.sdk.localstore.LocalStoreRequest LocalStoreRequest} implementation,
 * targeting the "close store" operation. This implementation simply clears the memory cache while
 * leaving the persistent disk store untouched.
 *
 * @author László Urszuly
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
