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

import com.podio.sdk.Request;
import com.podio.sdk.Store;

public class LocalStore implements Store {

    /**
     * Retrieves an object with the given key from the local store. If the
     * object isn't found in memory, it will be looked for on disk (spawning a
     * separate thread for the file system access). If it's not found there
     * either, a null-pointer will be returned.
     * 
     * @see com.podio.sdk.Store#get(java.lang.Object)
     */
    @Override
    public <T> Request<T> get(Object key) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Updates the in-memory cache immediately and spawns a separate thread to
     * update the persistent storage. There is no indication provided on the
     * result of neither the in-memory update nor the persistent storage
     * operation.
     * 
     * @see com.podio.sdk.Store#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public void put(Object key, Object value) {
        // TODO Auto-generated method stub

    }

    /**
     * Removes an object with the given key from the in memory cache as well as
     * the disk storage.
     * 
     * @see com.podio.sdk.Store#remove(java.lang.Object)
     */
    @Override
    public void remove(Object key) {
        // TODO Auto-generated method stub

    }

    /**
     * Destroys this instance of the local store. The in memory cache will be
     * cleared and all items in this store, as well as the store container
     * itself, on disk will be deleted.
     * 
     * @see com.podio.sdk.Store#destroy()
     */
    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}
