/*
 * Copyright (C) 2014 Copyright Citrix Systems, Inc. Permission is hereby
 * granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions: The above copyright notice and this
 * permission notice shall be included in all copies or substantial portions of
 * the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package com.podio.sdk.localstore;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.util.LruCache;

import com.podio.sdk.Request;
import com.podio.sdk.Request.ResultListener;
import com.podio.sdk.Store;

public class LocalStore implements Store {

    /**
     * Creates a new instance of this class and configures its initial state.
     * This is the only way to create and initialize a store.
     * 
     * @param context
     *        Used to fetch the disk storage folder.
     * @param name
     *        The name of the store.
     * @param maxMemoryInKiloBytes
     *        The memory size constraint.
     * @param listener
     *        The callback implementation through which the store will be
     *        delivered through.
     */
    public static Request<Store> open(Context context, String name, int maxMemoryInKiloBytes) {
        final int corePoolSize = 1;
        final int maxPoolSize = 1;
        final long waitTime = 0L;

        final LocalStore store = new LocalStore();
        store.executorService = new ThreadPoolExecutor(corePoolSize, maxPoolSize, waitTime, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE));

        LocalStoreRequest<LruCache<Object, Object>> initMemoryStoreRequest =
                (LocalStoreRequest<LruCache<Object, Object>>) LocalStoreRequest
                        .newInitMemoryStoreRequest(maxMemoryInKiloBytes)
                        .withResultListener(new ResultListener<LruCache<Object, Object>>() {

                            @Override
                            public boolean onRequestPerformed(LruCache<Object, Object> result) {
                                store.memoryStore = result;
                                return false;
                            }

                        });

        LocalStoreRequest<File> initDiskStoreRequest =
                (LocalStoreRequest<File>) LocalStoreRequest
                        .newInitDiskStoreRequest(context, name)
                        .withResultListener(new ResultListener<File>() {

                            @Override
                            public boolean onRequestPerformed(File result) {
                                store.diskStore = result;
                                return false;
                            }

                        });

        LocalStoreRequest<Store> deliverStoreRequest =
                new LocalStoreRequest<Store>(new Callable<Store>() {

                    @Override
                    public Store call() throws Exception {
                        return store;
                    }

                });

        store.executorService.execute(initMemoryStoreRequest);
        store.executorService.execute(initDiskStoreRequest);
        store.executorService.execute(deliverStoreRequest);

        return deliverStoreRequest;
    }

    /**
     * The queue executor service that manages the request queue.
     */
    private ExecutorService executorService;

    /**
     * The in-memory store.
     */
    private LruCache<Object, Object> memoryStore;

    /**
     * The disk store directory.
     */
    private File diskStore;

    /**
     * Hidden constructor.
     */
    private LocalStore() {
    }

    /**
     * Destroys this instance of the local store. The in memory cache will be
     * cleared and all files in the disk store, as well as the store container
     * itself, will be deleted.
     * 
     * @see com.podio.sdk.Store#destroy()
     */
    @Override
    public Request<Void> destroy() {
        LocalStoreRequest<Void> request = LocalStoreRequest.newDestroyRequest(memoryStore, diskStore);
        executorService.execute(request);
        return request;
    }

    /**
     * Retrieves an object with the given key from the local store. If the
     * object isn't found in memory, and a {@link Class} template is given, it
     * will be looked for on disk. If it's not found there either, a null
     * pointer will be returned.
     * 
     * @see com.podio.sdk.Store#get(java.lang.Object, java.lang.Class)
     */
    @Override
    public <T> Request<T> get(Object key, Class<T> classOfValue) {
        LocalStoreRequest<T> request = LocalStoreRequest.newGetRequest(memoryStore, diskStore, key, classOfValue);
        executorService.execute(request);
        return request;
    }

    /**
     * Removes an object with the given key from the local store. If the removed
     * value was found in the memory store, then it will be returned, else if it
     * exists in the disk store and a {@link Class} template is given, the disk
     * store version will be returned prior to deletion.
     * 
     * @see com.podio.sdk.Store#remove(java.lang.Object, java.lang.Class)
     */
    @Override
    public <T> Request<T> remove(Object key, Class<T> classOfValue) {
        LocalStoreRequest<T> request = LocalStoreRequest.newRemoveRequest(memoryStore, diskStore, key, classOfValue);
        executorService.execute(request);
        return request;
    }

    /**
     * Adds or updates a value with the given key in the local store. If there
     * already is a value for the given key in the memory store it will be
     * returned, else if a corresponding value exists in the disk store and a
     * {@link Class} template is given, the disk store version will be returned.
     * 
     * @see com.podio.sdk.Store#put(java.lang.Object, java.lang.Object,
     *      java.lang.Class)
     */
    @Override
    public <T> Request<T> set(Object key, Object value, Class<T> classOfValue) {
        LocalStoreRequest<T> request = LocalStoreRequest.newSetRequest(memoryStore, diskStore, key, value, classOfValue);
        executorService.execute(request);
        return request;
    }

}
