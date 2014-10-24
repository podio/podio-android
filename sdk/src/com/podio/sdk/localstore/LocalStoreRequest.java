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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;

import com.podio.sdk.JsonParser;
import com.podio.sdk.Request;

public class LocalStoreRequest<T> extends FutureTask<T> implements Request<T> {

    public static LocalStoreRequest<Void> newCloseRequest(final LruCache<Object, Object> memoryStore,
            final File diskStore) {

        return new LocalStoreRequest<Void>(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                if (memoryStore != null) {
                    memoryStore.evictAll();
                }

                return null;
            }

        });
    }

    /**
     * Creates a new Request for destroying the local store. The request will
     * not deliver anything.
     * 
     * @param memoryStore
     *        The in-memory store.
     * @param diskStore
     *        The file handle to the disk store directory.
     * @return A request ready for being enqueued in a queue.
     */
    public static LocalStoreRequest<Void> newDestroyRequest(final LruCache<Object, Object> memoryStore,
            final File diskStore) {

        return new LocalStoreRequest<Void>(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                // Delete memory store.
                if (memoryStore != null) {
                    memoryStore.evictAll();
                }

                // Delete disk store.
                if (!isWritableDirectory(diskStore)) {
                    File[] files = diskStore.listFiles();

                    for (File file : files) {
                        file.delete();
                    }

                    diskStore.delete();
                }
                return null;
            }

        });
    }

    /**
     * Creates a new Request for retrieving a value from the local store. The
     * request will deliver the requested object, or a null-pointer if no object
     * is found by the given key.
     * 
     * @param memoryStore
     *        The in-memory store.
     * @param diskStore
     *        The file handle to the disk store directory.
     * @param key
     *        The key of the value.
     * @param classOfValue
     *        The type to parse the file into (if needed).
     * @return A request ready for being enqueued in a queue.
     */
    public static <E> LocalStoreRequest<E> newGetRequest(final LruCache<Object, Object> memoryStore,
            final File diskStore, final Object key, final Class<E> classOfValue) {

        return new LocalStoreRequest<E>(new Callable<E>() {

            @Override
            @SuppressWarnings("unchecked")
            public E call() throws Exception {
                E value = null;

                // Try to read from memory.
                if (memoryStore != null) {
                    value = (E) memoryStore.get(key);
                }

                // If failed try to read from disk.
                if (value == null && classOfValue != null && isReadableDirectory(diskStore)) {
                    String fileName = key.toString();
                    File file = new File(diskStore, fileName);
                    value = readObjectFromDisk(file, classOfValue);

                    // Update memory.
                    if (value != null) {
                        memoryStore.put(key, value);
                    }
                }

                return value;
            }

        });
    }

    /**
     * Creates a new initialization request targeting the disk store, enabling
     * the caller to initialize the store on a worker thread.
     * 
     * @param context
     *        The context from which the cache directory path will be extracted.
     * @param name
     *        The name of the store to initialize.
     * @return A request ready for being enqueued in a queue.
     */
    public static LocalStoreRequest<File> newInitDiskStoreRequest(final Context context, final String name) {
        return new LocalStoreRequest<File>(new Callable<File>() {

            @Override
            public File call() throws Exception {
                String externalStorageState = Environment.getExternalStorageState();
                String systemCachePath;

                // Get external cache directory, fallback to internal storage.
                if (Environment.MEDIA_MOUNTED.equals(externalStorageState)) {
                    systemCachePath = context.getExternalCacheDir().getPath();
                } else if (!Environment.isExternalStorageRemovable()) {
                    systemCachePath = context.getExternalCacheDir().getPath();
                } else {
                    systemCachePath = context.getCacheDir().getPath();
                }

                // Validate and create if necessary.
                File diskStore = new File(systemCachePath + File.separator + name);
                if (diskStore != null && !isWritableDirectory(diskStore)) {
                    diskStore.mkdir();
                }

                return diskStore;
            }

        });
    }

    /**
     * Creates a new initialization request targeting the in-memory store,
     * enabling the caller to initialize the store on a worker thread.
     * 
     * @return A request ready for being enqueued in a queue.
     */
    public static LocalStoreRequest<LruCache<Object, Object>> newInitMemoryStoreRequest(final int maxMemoryAsKiloBytes) {
        return new LocalStoreRequest<LruCache<Object, Object>>(new Callable<LruCache<Object, Object>>() {

            @Override
            public LruCache<Object, Object> call() throws Exception {
                return new LruCache<Object, Object>(maxMemoryAsKiloBytes) {

                    @Override
                    protected int sizeOf(Object key, Object value) {
                        return calculateSizeOfObject(value);
                    }

                };
            }

        });
    }

    /**
     * Creates a new Request for removing a value from the local store. The
     * request will deliver the removed value, or a null-pointer if no object is
     * found by the given key.
     * 
     * @param memoryStore
     *        The in-memory store.
     * @param diskStore
     *        The file handle to the disk store directory.
     * @param key
     *        The key of the value.
     * @param classOfValue
     *        The type to parse the file into (if needed).
     * @return A request ready for being enqueued in a queue.
     */
    public static <E> LocalStoreRequest<E> newRemoveRequest(final LruCache<Object, Object> memoryStore,
            final File diskStore, final Object key, final Class<E> classOfValue) {

        return new LocalStoreRequest<E>(new Callable<E>() {

            @Override
            @SuppressWarnings("unchecked")
            public E call() throws Exception {
                E value = null;

                // Remove from memory.
                if (memoryStore != null) {
                    value = (E) memoryStore.remove(key);
                }

                // Remove from disk.
                if (isReadableDirectory(diskStore)) {
                    String fileName = key.toString();
                    File file = new File(diskStore, fileName);

                    if (value == null && classOfValue != null) {
                        value = readObjectFromDisk(file, classOfValue);
                    }

                    file.delete();
                }

                return value;
            }

        });
    }

    /**
     * Creates a new request for storing a given value. The request will deliver
     * the previous value if an overwrite has occurred, or a null-pointer if no
     * object is previously stored by the given key.
     * 
     * @param memoryStore
     *        The in-memory store.
     * @param diskStore
     *        The file handle to the disk store directory.
     * @param key
     *        The key of the value.
     * @param value
     *        The value.
     * @return A request ready for being enqueued in a queue.
     */
    public static <E> LocalStoreRequest<E> newSetRequest(final LruCache<Object, Object> memoryStore,
            final File diskStore, final Object key, final Object value, final Class<E> classOfValue) {

        return new LocalStoreRequest<E>(new Callable<E>() {

            @Override
            @SuppressWarnings("unchecked")
            public E call() throws Exception {
                E previous = null;

                // Update memory
                if (memoryStore != null) {
                    previous = (E) memoryStore.put(key, value);
                }

                // Update disk.
                if (diskStore != null) {
                    String fileName = key.toString();
                    File file = new File(diskStore, fileName);

                    // ...but first, get any value being overwritten.
                    if (previous == null && classOfValue != null) {
                        previous = readObjectFromDisk(file, classOfValue);
                    }

                    writeObjectToDisk(file, value);
                }

                return previous;
            }

        });
    }

    /**
     * Estimates the size of an object expressed in kilobytes. This is done by
     * counting the number of bytes the object would occupy while serialized
     * through an object byte array output stream.
     * <p>
     * Up to date I'm not aware of any intelligent and reliable means to get
     * this figure from the platform. Java 7 (?) has the 'java.lang.instrument'
     * package which could have been of use, but the API is not available in
     * Android (as it operates on Java byte code executed on a Java VM, while
     * Android apps are dex byte code executed on the Dalvik VM)
     * 
     * @param object
     *        The object to estimate a size for.
     * @return The estimated size in KB.
     */
    private static int calculateSizeOfObject(Object object) {
        if (object == null) {
            return 0;
        }

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            objectOutputStream.close();

            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return (int) (byteArray.length / 1024);
        } catch (IOException e) {
            // We don't know anything about the size, assuming it's HUGE.
            return Integer.MAX_VALUE;
        }
    }

    /**
     * Verifies that the given file handle, 1) isn't a null pointer, 2) exists
     * on the file system, 3) is a directory and 4) is readable.
     * 
     * @param directory
     *        The directory to test.
     * @return Boolean <code>true</code> if the conditions are met, boolean
     *         <code>false</code> otherwise.
     */
    private static boolean isReadableDirectory(File directory) {
        return directory != null && directory.exists() && directory.isDirectory() && directory.canRead();
    }

    /**
     * Verifies that the given file handle, 1) isn't a null pointer, 2) exists
     * on the file system, 3) is a file and 4) is readable.
     * 
     * @param file
     *        The file to test.
     * @return Boolean <code>true</code> if the conditions are met, boolean
     *         <code>false</code> otherwise.
     */
    private static boolean isReadableFile(File file) {
        return file != null && file.exists() && file.isFile() && file.canRead();
    }

    /**
     * Verifies that the given file handle, 1) isn't a null pointer, 2) exists
     * on the file system, 3) is a directory and 4) is writable.
     * 
     * @param directory
     *        The directory to test.
     * @return Boolean <code>true</code> if the conditions are met, boolean
     *         <code>false</code> otherwise.
     */
    private static boolean isWritableDirectory(File directory) {
        return directory != null && directory.exists() && directory.isDirectory() && directory.canWrite();
    }

    /**
     * Reads the content of a file and tries to parse it as JSON into an object.
     * 
     * @param file
     *        The file on disk to read from.
     * @param classOfValue
     *        The class definition that the JSON should be parsed into.
     * @return The object stored in the file.
     * @throws IOException
     *         If anything went wrong during file access.
     */
    private static <E> E readObjectFromDisk(File file, Class<E> classOfValue) throws IOException {
        // Validate file.
        if (!isReadableFile(file)) {
            return null;
        }

        long length = file.length();
        if (length < 0 || length > Integer.MAX_VALUE) {
            return null;
        }

        // Read from disk.
        byte[] bytes = new byte[(int) length];
        FileInputStream fileInputStream = new FileInputStream(file);

        try {
            fileInputStream.read(bytes);
        } finally {
            fileInputStream.close();
        }

        // Parse JSON to object.
        String json = new String(bytes);
        E result = JsonParser.fromJson(json, classOfValue);
        return result;
    }

    /**
     * Tries to write the value to the disk store. An attempt will be made to
     * parse the value to JSON and on success it will be written to a file with
     * the same name as the key. The key will be transformed to the file name by
     * calling the <code>toString()</code> method on it (pick your keys with
     * great care).
     * 
     * @param file
     *        The {@link File} pointing at the desired destination file on disk.
     * @param value
     *        The value that will be serialized to JSON and saved as a file.
     * @throws IOException
     */
    private static void writeObjectToDisk(File file, Object value) throws IOException {
        String json = JsonParser.toJson(value);
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        try {
            fileOutputStream.write(json.getBytes());
        } finally {
            fileOutputStream.close();
        }
    }

    /**
     * The list to put any subscribing result listeners in.
     */
    private ArrayList<ResultListener<T>> resultListeners;

    /**
     * The list to put any subscribing error listeners in.
     */
    private ArrayList<ErrorListener> errorListeners;

    /**
     * The delivered result.
     */
    private T result;

    /**
     * The delivered error.
     */
    private Throwable error;

    /**
     * Initializes the listener containers.
     * 
     * @param callable
     *        The actual task to perform sometime in the future.
     */
    LocalStoreRequest(Callable<T> callable) {
        super(callable);
        resultListeners = new ArrayList<ResultListener<T>>();
        errorListeners = new ArrayList<ErrorListener>();
    }

    /**
     * Makes sure the result listeners are called properly when a result is
     * delivered.
     * 
     * @see java.util.concurrent.FutureTask#done()
     */
    @Override
    protected void done() {
        super.done();

        try {
            result = get();
            error = null;
            notifyResultListeners();
        } catch (ExecutionException e) {
            result = null;
            error = e.getCause();
            notifyErrorListeners();
        } catch (InterruptedException e) {
            result = null;
            error = e;
            notifyErrorListeners();
        }
    }

    /**
     * Registers a result listener for this request. If the result is already
     * delivered, then the result listener will be called immediately with the
     * result.
     * 
     * @see com.podio.sdk.Request#withResultListener(com.podio.sdk.Request.ResultListener)
     */
    @Override
    public Request<T> withResultListener(Request.ResultListener<T> contentListener) {
        if (contentListener != null && !resultListeners.contains(contentListener)) {
            resultListeners.add(contentListener);
        }

        if (isDone() && error == null) {
            contentListener.onRequestPerformed(result);
        }

        return this;
    }

    /**
     * Registers an error listener for this request. If an error is already
     * delivered, then the error listener will be called immediately with the
     * error.
     * 
     * @see com.podio.sdk.Request#withErrorListener(com.podio.sdk.Request.ErrorListener)
     */
    @Override
    public Request<T> withErrorListener(Request.ErrorListener errorListener) throws UnsupportedOperationException {
        if (errorListener != null && !errorListeners.contains(errorListener)) {
            errorListeners.add(errorListener);
        }

        if (isDone() && error != null) {
            errorListener.onErrorOccured(error);
        }

        return this;
    }

    /**
     * Throws an {@link UnsupportedOperationException} as this implementation
     * doesn't deal with sessions.
     * 
     * @see com.podio.sdk.Request#withSessionListener(com.podio.sdk.Request.SessionListener)
     */
    @Override
    public Request<T> withSessionListener(Request.SessionListener sessionListener) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This implementation doesn't handle sessions.");
    }

    /**
     * Reports a delivered error to all error listeners. The callback
     * implementations will be called on the Main thread.
     */
    private void notifyErrorListeners() {
        Handler handler = new Handler(Looper.getMainLooper());

        for (final ErrorListener listener : errorListeners) {
            if (listener != null) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        listener.onErrorOccured(error);
                    }

                });
            }
        }
    }

    /**
     * Reports a delivered result to all result listeners. The callback
     * implementations will be called on the Main thread.
     */
    private void notifyResultListeners() {
        Handler handler = new Handler(Looper.getMainLooper());

        for (final ResultListener<T> listener : resultListeners) {
            if (listener != null) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        listener.onRequestPerformed(result);
                    }

                });
            }
        }
    }

}
