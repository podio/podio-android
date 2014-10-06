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

package com.podio.sdk;

import android.net.Uri;

import com.podio.sdk.internal.Utils;

public class Filter {
    private final Uri.Builder uriBuilder;

    protected Filter() {
        this(null);
    }

    protected Filter(String path) {
        uriBuilder = new Uri.Builder();

        if (Utils.notEmpty(path)) {
            uriBuilder.appendEncodedPath(path);
        }
    }

    protected Filter addQueryParameter(String key, String value) {
        uriBuilder.appendQueryParameter(key, value);
        return this;
    }

    protected Filter addPathSegment(String segment) {
        uriBuilder.appendPath(segment);
        return this;
    }

    protected Uri buildUri(String scheme, String authority) {
        return uriBuilder
                .scheme(scheme)
                .authority(authority)
                .build();
    }
}
