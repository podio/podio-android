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
import com.podio.sdk.VolleyProvider;
import com.podio.sdk.domain.Application;

public class ApplicationProvider extends VolleyProvider {

    static class Path extends Filter {

        private Path() {
            super("app");
        }

        private Path withApplicationId(long applicationId) {
            addPathSegment(Long.toString(applicationId, 10));
            return this;
        }

        private Path withInactivesIncluded(boolean doInclude) {
            addQueryParameter("include_inactive", doInclude ? "true" : "false");
            return this;
        }

        private Path withSpaceId(long spaceId) {
            addPathSegment("space");
            addPathSegment(Long.toString(spaceId, 10));
            return this;
        }

        private Path withType(String type) {
            addQueryParameter("type", type);
            return this;
        }
    }

    /**
     * Fetches the full content set of the application with the given id.
     * 
     * @param applicationId
     *        The id of the application to fetch.
     * @return A ticket which the caller can use to identify this request with.
     */
    public PodioRequest<Application> get(long applicationId) {
        Path filter = new Path()
                .withApplicationId(applicationId)
                .withType("full");

        return get(filter, Application.class);
    }

    /**
     * Fetches a short subset of the application with the given id.
     * 
     * @param applicationId
     *        The id of the application to fetch.
     * @return A ticket which the caller can use to identify this request with.
     */
    public PodioRequest<Application> getShort(long applicationId) {
        Path filter = new Path()
                .withApplicationId(applicationId)
                .withType("short");

        return get(filter, Application.class);
    }

    /**
     * Fetches a mini subset of the application with the given id.
     * 
     * @param applicationId
     *        The id of the application to fetch.
     * @return A ticket which the caller can use to identify this request with.
     */
    public PodioRequest<Application> getMini(long applicationId) {
        Path filter = new Path()
                .withApplicationId(applicationId)
                .withType("mini");

        return get(filter, Application.class);
    }

    /**
     * Fetches a micro subset of the application with the given id.
     * 
     * @param applicationId
     *        The id of the application to fetch.
     * @return A ticket which the caller can use to identify this request with.
     */
    public PodioRequest<Application> getMicro(long applicationId) {
        Path filter = new Path()
                .withApplicationId(applicationId)
                .withType("micro");

        return get(filter, Application.class);
    }

    /**
     * Fetches all active applications in the workspace with the given id.
     * 
     * @param spaceId
     *        The id of the parent workspace.
     * @return A ticket which the caller can use to identify this request with.
     */
    public PodioRequest<Application[]> getAllActive(long spaceId) {
        Path filter = new Path()
                .withSpaceId(spaceId)
                .withInactivesIncluded(false);

        return get(filter, Application[].class);
    }

    /**
     * Fetches all applications, including the inactive ones, in the workspace
     * with the given id.
     * 
     * @param spaceId
     *        The id of the parent workspace.
     * @return A ticket which the caller can use to identify this request with.
     */
    public PodioRequest<Application[]> getAll(long spaceId) {
        Path filter = new Path()
                .withSpaceId(spaceId)
                .withInactivesIncluded(true);

        return get(filter, Application[].class);
    }

}
