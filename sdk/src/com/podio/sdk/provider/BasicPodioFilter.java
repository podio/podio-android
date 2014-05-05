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

import android.net.Uri;

import com.podio.sdk.PodioFilter;
import com.podio.sdk.internal.utils.Utils;

public class BasicPodioFilter implements PodioFilter {
    private final Uri.Builder uriBuilder;

    public BasicPodioFilter() {
        this(null);
    }

    public BasicPodioFilter(String path) {
        uriBuilder = new Uri.Builder();

        if (Utils.notEmpty(path)) {
            uriBuilder.appendEncodedPath(path);
        }
    }

    @Override
    public PodioFilter addQueryParameter(String key, String value) {
        if (Utils.notEmpty(key) && value != null) {
            uriBuilder.appendQueryParameter(key, value);
        }

        return this;
    }

    @Override
    public PodioFilter addPathSegment(String segment) {
        if (Utils.notEmpty(segment)) {
            uriBuilder.appendPath(segment);
        }

        return this;
    }

    @Override
    public Uri buildUri(String scheme, String authority) {
        Uri uri = null;

        if (Utils.notEmpty(scheme) && Utils.notEmpty(authority)) {
            uri = uriBuilder.scheme(scheme).authority(authority).build();
        }

        return uri;
    }
}
