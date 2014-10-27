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
import java.util.concurrent.Callable;

import android.util.LruCache;

/**
 * A specific {@link LocalStoreRequest}, targeting the "close store" operation.
 * This implementation simply clears the memory cache while leaving the
 * persistent disk store untouched.
 * 
 * @author László Urszuly
 */
final class CloseRequest extends LocalStoreRequest<Void> {

    /**
     * Creates a new Request for closing the local store, clearing the memory
     * store. The request will not deliver anything.
     * 
     * @param memoryStore
     * @param diskStore
     */
    CloseRequest(final LruCache<Object, Object> memoryStore, final File diskStore) {
        super(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                validateState(memoryStore, diskStore);

                if (memoryStore != null) {
                    memoryStore.evictAll();
                }

                return null;
            }

        });
    }

}
