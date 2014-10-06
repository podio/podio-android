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
import com.podio.sdk.PodioRequest;
import com.podio.sdk.domain.View;

/**
 * Enables access to the view API end point.
 * 
 * @author Tobias Lindberg
 */
public class ViewProvider extends VolleyProvider {

    static class Path extends Filter {

        public Path() {
            super("view");
        }

        public Path withApplicationId(long applicationId) {
            addPathSegment("app");
            addPathSegment(Long.toString(applicationId, 10));

            return this;
        }

    }

    /**
     * Fetches views for a given application that can be used to filter items
     * for that application.
     * 
     * @param applicationId
     *        The id of the parent application.
     * @return A ticket which the caller can use to identify this request with.
     */
    public PodioRequest<View[]> getAllViews(long applicationId) {
        Path filter = new Path().withApplicationId(applicationId);
        return get(filter, View[].class);
    }

}
