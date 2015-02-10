/*
 * Copyright (C) 2015 Citrix Systems, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.podio.sdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.podio.sdk.internal.Utils;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocketFactory;

/**
 * This class is responsible for loading and caching images from the Internet. The loader can fetch
 * any image from any url (doesn't have to be one from a Podio CDN). This implementation relies
 * heavily on the Android Volley Image Loader implementation.
 *
 * @author László Urszuly
 */
public class ImageLoader {

    /**
     * The SDK provided image loader callback interface.
     */
    public static interface ImageListener {

        /**
         * Called when an image was successfully loaded, either from the cache or the network. This
         * method may be called with a null-pointer bitmap to signal that the image wasn't found in
         * the cache, hence, giving an opportunity to the caller to show either some kind of
         * progress indication or a default image.
         *
         * @param bitmap
         *         The requested bitmap or null if no cache-hit.
         * @param isFromCache
         *         True if the image was found in (and returned from) the cache, boolean false
         *         otherwise.
         */
        public void onImageReady(Bitmap bitmap, boolean isFromCache);

        /**
         * Called when loading the image failed due to any reason. The error object will hold more
         * details in the stack trace and could be tested for being an instance of {@link
         * com.podio.sdk.ConnectionError}, {@link com.podio.sdk.NoResponseError} or {@link
         * com.podio.sdk.ApiError}.
         *
         * @param podioError
         *         The cause of the failure.
         */
        public void onErrorOccurred(PodioError podioError);
    }

    /**
     * This enumeration offers any API approved default sizes of images. The caller should know what
     * type of image is fetched and use the appropriate size - if any - for it. The definitions will
     * give a hint on what type of images they apply to. Further details can be found at <a
     * href="https://developers.podio.com/doc/files">the documentation page</a>.
     */
    public static enum Size {
        DEFAULT("default"),
        UNSPECIFIED(""),
        AVATAR_TINY("tiny"),
        AVATAR_SMALL("small"),
        AVATAR_MEDIUM("medium"),
        AVATAR_LARGE("large"),
        ITEM_MEDIUM("medium"),
        ITEM_BADGE("badge"),
        ITEM_EXTRA_LARGE("extra_large"),
        LOGO_TINY("tiny"),
        LOGO_LARGE("large");

        private final String literal;

        private Size(String literal) {
            this.literal = literal;
        }

    }

    /**
     * This is the image cache implementation used by the under laying Volley ImageLoader class.
     */
    private static class ImageCache extends LruCache<String, Bitmap> implements com.android.volley.toolbox.ImageLoader.ImageCache {

        public ImageCache(int maxSize) {
            super(maxSize);
        }

        @Override
        public Bitmap getBitmap(String url) {
            return get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            if (Utils.notEmpty(url) && bitmap != null) {
                put(url, bitmap);
            }
        }

        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            return bitmap.getByteCount() / 1024;
        }
    }

    /**
     * The shared Volley request queue on which the images will be downloaded.
     */
    private static RequestQueue volleyImageRequestQueue;

    /**
     * The shared cache that is injected in the Volley ImageLoader class.
     */
    private static ImageCache imageCache;

    /**
     * The shared, under laying Volley ImageLoader class that will perform the actual work.
     */
    private static com.android.volley.toolbox.ImageLoader imageLoader;

    /**
     * Attempts to load the requested image with the given size. If it already exists in the cache,
     * it will be loaded from there, otherwise a network request will be performed.
     * <p/>
     * The given callback interface will be invoked with a null pointer bitmap if the cache doesn't
     * hold the requested image. The caller can then decide to show some sort of default image or a
     * progress indication or so. The same callback interface will be called a second time when the
     * corresponding network request has completed.
     * <p/>
     * If the cache already holds the requested image, the callback will only be called once.
     *
     * @param url
     *         The url to fetch the bitmap from if it doesn't exist in the cache. The url will also
     *         serve as a cache key once the bitmap is stored locally.
     * @param size
     *         An API defined size notation that can optionally be given. This size will ask for a
     *         specific, server side pre-scaled bitmap from the API. The image loader will not
     *         resize any bitmaps.
     * @param listener
     *         The callback implementation that will be invoked on bitmap or error delivery.
     */
    public synchronized void loadImage(String url, Size size, final ImageListener listener) {
        Uri uri = Uri.parse(url);
        Uri requestUri = (size != null && size != Size.UNSPECIFIED) ? Uri.withAppendedPath(uri, size.literal) : uri;

        imageLoader.get(requestUri.toString(), new com.android.volley.toolbox.ImageLoader.ImageListener() {
            @Override
            public void onResponse(com.android.volley.toolbox.ImageLoader.ImageContainer response, boolean isImmediate) {
                listener.onImageReady(response.getBitmap(), isImmediate);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onErrorOccurred(parseVolleyError(error));
            }
        });
    }

    /**
     * Initializes the image loader to its default state. This method MUST be called prior to any
     * further interaction with the image loader.
     *
     * @param context
     *         The context used to create and initialize the network request queue.
     * @param sslSocketFactory
     *         An optional SSL socket factory to use for the network requests.
     */
    public synchronized void setup(Context context, SSLSocketFactory sslSocketFactory) {
        // Ensure the expected request queues exists.
        if (volleyImageRequestQueue == null) {
            volleyImageRequestQueue = (sslSocketFactory != null) ?
                    Volley.newRequestQueue(context, new HurlStack(null, sslSocketFactory)) :
                    Volley.newRequestQueue(context);
            volleyImageRequestQueue.start();
        }

        // Clear out any and all queued requests.
        volleyImageRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(com.android.volley.Request<?> request) {
                return true;
            }
        });

        // Clear out any cached content in the request queues.
        Cache requestCache = volleyImageRequestQueue.getCache();
        if (requestCache != null) {
            requestCache.clear();
        }

        // Ensure the expected image cache exists.
        if (imageCache == null) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;
            int maxSizeKb = width * height * 4 * 3 / 1024; // * 4 = magic unicorn,  * 3 = 3 full screens worth of memory
            imageCache = new ImageCache(maxSizeKb);
        }

        // Clear out any and all cached images.
        imageCache.evictAll();
        imageLoader = new com.android.volley.toolbox.ImageLoader(volleyImageRequestQueue, imageCache);
    }

    /**
     * Parses any given errors from the underlying Volley mechanism into SDK defined error types.
     *
     * @param volleyError
     *         The Volley error to parse.
     *
     * @return The corresponding SDK defined error object.
     */
    private PodioError parseVolleyError(VolleyError volleyError) {
        PodioError error;

        if (volleyError instanceof NoConnectionError && volleyError.getCause() instanceof UnknownHostException) {
            error = new ConnectionError(volleyError);
        } else if (volleyError instanceof TimeoutError) {
            error = new NoResponseError(volleyError);
        } else {
            String errorJson = getResponseBody(volleyError.networkResponse);
            int responseCode = getResponseCode(volleyError.networkResponse);

            if (Utils.notEmpty(errorJson) && responseCode > 0) {
                error = new ApiError(errorJson, responseCode, volleyError);
            } else {
                error = new PodioError(volleyError);
            }
        }

        return error;
    }

    /**
     * Reads the API provided error response body from the given Volley network response object.
     *
     * @param networkResponse
     *         The Volley network response that holds the error description.
     *
     * @return The JSON error description.
     */
    private String getResponseBody(NetworkResponse networkResponse) {
        try {
            String charSet = HttpHeaderParser.parseCharset(networkResponse.headers);
            return new String(networkResponse.data, charSet);
        } catch (UnsupportedEncodingException e) {
            // The provided error JSON is provided with an unknown char-set.
            return null;
        } catch (NullPointerException e) {
            // For some reason the VolleyError didn't provide a networkResponse.
            return null;
        }
    }

    /**
     * Reads the API provided HTTP error status code from the given Volley network response object.
     *
     * @param networkResponse
     *         The Volley network response object that holds the HTTP status code.
     *
     * @return The numeric HTTP status code.
     */
    private int getResponseCode(NetworkResponse networkResponse) {
        return networkResponse != null ? networkResponse.statusCode : 0;
    }

}
