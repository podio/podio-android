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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;

import com.podio.sdk.JsonParser;
import com.podio.sdk.Request;

/**
 * A base class for operations targeting a local store. The
 * <code>LocalStoreRequest</code> offers means of hooking in callback interfaces
 * which will be called once the operation has delivered it's result (or an
 * error).
 * 
 * @author László Urszuly
 * @param <T>
 *        The type of data handled by a given request. This only applies to the
 *        "get" operation.
 */
class LocalStoreRequest<T> extends FutureTask<T> implements Request<T> {

    /**
     * Creates a new Request for clearing the memory store. The disk store is
     * not affected by this. The request will not deliver anything.
     * 
     * @param memoryStore
     *        The memory store to clear and close.
     * @return A request ready for being enqueued in a queue.
     * @see com.podio.sdk.localstore.LocalStoreRequest#newEraseRequest(LruCache,
     *      File)
     */
    static FreeRequest newFreeRequest(LruCache<Object, Object> memoryStore) {
        return new FreeRequest(memoryStore);
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
    static EraseRequest newEraseRequest(LruCache<Object, Object> memoryStore, File diskStore) {
        return new EraseRequest(memoryStore, diskStore);
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
    static <E> GetRequest<E> newGetRequest(LruCache<Object, Object> memoryStore,
            File diskStore, Object key, Class<E> classOfValue) {
        return new GetRequest<E>(memoryStore, diskStore, key, classOfValue);
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
    static InitDiskRequest newInitDiskStoreRequest(Context context, String name) {
        return new InitDiskRequest(context, name);
    }

    /**
     * Creates a new initialization request targeting the in-memory store,
     * enabling the caller to initialize the store on a worker thread.
     * 
     * @return A request ready for being enqueued in a queue.
     */
    static InitMemoryRequest newInitMemoryStoreRequest(int maxMemoryAsKiloBytes) {
        return new InitMemoryRequest(maxMemoryAsKiloBytes);
    }

    /**
     * Creates a new Request for removing a value from the local store.
     * 
     * @param memoryStore
     *        The in-memory store.
     * @param diskStore
     *        The file handle to the disk store directory.
     * @param key
     *        The key of the value.
     * @return A request ready for being enqueued in a queue.
     */
    static RemoveRequest newRemoveRequest(LruCache<Object, Object> memoryStore, File diskStore, Object key) {
        return new RemoveRequest(memoryStore, diskStore, key);
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
    static SetRequest newSetRequest(LruCache<Object, Object> memoryStore, File diskStore,
            Object key, Object value) {
        return new SetRequest(memoryStore, diskStore, key, value);
    }

    /**
     * URL encodes the string format of the given key, so it can be used as a
     * file name.
     * 
     * @param key
     *        The key to build a file name on.
     * @return The URL encoded string notation of the given key.
     * @throws UnsupportedEncodingException
     *         If using an invalid charset name. This should never happen as we
     *         call for the default charset of the system.
     */
    protected static String getFileName(Object key) throws UnsupportedEncodingException {
        return URLEncoder.encode(key.toString(), Charset.defaultCharset().name());
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
    protected static boolean isReadableDirectory(File directory) {
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
    protected static boolean isReadableFile(File file) {
        return file != null && file.exists() && file.isFile() && file.canRead();
    }

    /**
     * Verifies that the given class definition isn't a null pointer or a
     * {@link Void} class.
     * 
     * @param template
     *        The class definition to test.
     * @return Boolean <code>true</code> if the conditions are met, boolean
     *         <code>false</code> otherwise.
     */
    protected static boolean isValidTemplate(Class<?> template) {
        return template != null && !template.equals(Void.class);
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
    protected static boolean isWritableDirectory(File directory) {
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
    protected static <E> E readObjectFromDisk(File file, Class<E> classOfValue) throws IOException {
        // Validate file.
        if (!isReadableFile(file)) {
            return null;
        }

        if (classOfValue == null || classOfValue.equals(Void.class)) {
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
    protected static void writeObjectToDisk(File file, Object value) throws IOException {
        String json = JsonParser.toJson(value);
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        try {
            fileOutputStream.write(json.getBytes());
        } finally {
            fileOutputStream.close();
        }
    }

    /**
     * Validates the memory cache and the disk store handles. If none of them
     * are ready for use, an {@link IllegalStateException} is thrown, otherwise
     * we're cool.
     * 
     * @throws IllegalStateException
     *         If neither in-memory store, nor disk store has a valid handle.
     */
    protected static void validateState(LruCache<Object, Object> memoryStore, File diskStore) throws IllegalStateException {
        if (memoryStore == null && diskStore == null) {
            throw new IllegalStateException("You're trying to interact with a closed store.");
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
        handler.post(new Runnable() {

            @Override
            public void run() {
                for (final ErrorListener listener : errorListeners) {
                    if (listener != null) {
                        if (listener.onErrorOccured(error)) {
                            return;
                        }
                    }
                }
            }

        });
    }

    /**
     * Reports a delivered result to all result listeners. The callback
     * implementations will be called on the Main thread.
     */
    private void notifyResultListeners() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                for (final ResultListener<T> listener : resultListeners) {
                    if (listener != null) {
                        if (listener.onRequestPerformed(result)) {
                            return;
                        }
                    }
                }
            }

        });
    }

}
