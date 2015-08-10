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

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.reference.ReferenceGroup;

/**
 * Enables access to the Reference API end point.
 *
 * @author Tobias Lindberg
 */
public class ReferenceProvider extends Provider {

    static class Path extends Filter {

        protected Path() {
            super("reference");
        }

        Path withSearch() {
            addPathSegment("search");

            return this;
        }
    }

    /**
     * Performs a reference search based on the @referenceTarget.
     *
     * @param referenceTarget
     *         The reference search target.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<ReferenceGroup[]> referenceSearch(ReferenceGroup.ReferenceTarget referenceTarget) {
        Path filter = new Path().withSearch();

        return post(filter, referenceTarget, ReferenceGroup[].class);
    }

}
