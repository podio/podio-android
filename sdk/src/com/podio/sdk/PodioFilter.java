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

/**
 * Defines the filter interface that describes which data set any given rest
 * operation is to address. This interface is primarily used with the
 * {@link PodioProvider}.
 * 
 * @author László Urszuly
 * 
 */
public interface PodioFilter {

    /**
     * Adds a new query parameter with the given name and value.
     * 
     * @param key
     *            The name of the parameter.
     * @param value
     *            The corresponding parameter value.
     * @return This filter object for further chaining.
     */
    public PodioFilter addQueryParameter(String key, String value);

    /**
     * Adds a new path segment to the filter.
     * 
     * @param segment
     *            The new segment.
     * @return This filter object for further chaining.
     */
    public PodioFilter addPathSegment(String segment);

    /**
     * Builds a Uri based on the given parameters and the information contained
     * in the filter.
     * 
     * @param scheme
     *            The Uri scheme.
     * @param authority
     *            The Uri authority
     * @return The Uri.
     */
    public Uri buildUri(String scheme, String authority);

}
