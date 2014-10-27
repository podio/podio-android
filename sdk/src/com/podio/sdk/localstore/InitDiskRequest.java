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
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Callable;

import android.content.Context;

import com.podio.sdk.internal.Utils;

/**
 * A specific {@link LocalStoreRequest}, targeting the "initialize disk store"
 * operation. This implementation gets a handle to the internal cache directory
 * and looks for a sub directory therein with the given store name. If no such
 * sub directory is found, one is created.
 * 
 * @see com.podio.sdk.localstore.EraseRequest
 * @author László Urszuly
 */
final class InitDiskRequest extends LocalStoreRequest<File> {

    /**
     * Returns the handle to the internal cache directory on this device.
     * 
     * @param context
     *        The context to request the directory handle from.
     * @param name
     *        The name of the store, i.e. the name of the sub-folder in the
     *        cache directory.
     * @return A File object pointing at the disk store directory.
     * @throws UnsupportedEncodingException
     *         If trying to URL encode the name with an invalid charset. This
     *         should never happen as we request the platform default charset.
     */
    private static final File getDiskStoreDirectory(Context context, String name) throws UnsupportedEncodingException {
        File diskStore = null;

        if (context != null && Utils.notEmpty(name)) {
            String systemCachePath = context.getCacheDir().getPath();
            String directoryName = getFileName(name);
            diskStore = new File(systemCachePath + File.separator + directoryName);

            if (diskStore != null && !isWritableDirectory(diskStore)) {
                diskStore.mkdir();
            }
        }

        return diskStore;
    }

    /**
     * Creates a new local store request that initializes the disk cache on a
     * worker thread.
     * 
     * @param context
     *        The context from which the cache directory path will be extracted.
     * @param name
     *        The name of the store to initialize.
     */
    InitDiskRequest(final Context context, final String name) {
        super(new Callable<File>() {

            @Override
            public File call() throws Exception {
                return getDiskStoreDirectory(context, name);
            }

        });
    }

}
