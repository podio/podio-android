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
package com.podio.sdk.provider;

import android.content.Context;

import com.podio.sdk.Request;
import com.podio.sdk.Store;
import com.podio.sdk.localstore.LocalStore;
import com.podio.sdk.volley.VolleyProvider;

/**
 * A Provider implementation that offers means of locally storing your data. The
 * data is kept in memory, to the extent it fits within the given constraints,
 * and is also persisted to disk.
 * <p>
 * When adding a new item and the in-memory cache can't fit any more items, then
 * the new item will be added to the head of the stack and the last item is
 * evicted. When an item is fetched from the store, the in-memory cache will
 * move it to the head of the stack.
 * <p>
 * This provider gives access to a separate instance of the local store. You
 * will then interact with that instance. You can't interact with the store
 * through the Podio SDK.
 * 
 * @author László Urszuly
 */
public class StoreProvider extends VolleyProvider {

    /**
     * Opens an existing store or creates a new one if none is found by the
     * given name.
     * 
     * @param context
     *        The context used to get access to the file system.
     * @param name
     *        The name of the store to open or create.
     * @param maxSizeMemory
     *        The maximum amount of memory in kilobytes that this memory store
     *        will ever occupy.
     * @return A request future, enabling the caller to hook in optional
     *         callback implementations.
     */
    public Request<Store> open(Context context, String name, int maxSizeMemory) {
        return LocalStore.open(context, name, maxSizeMemory);
    }

}
