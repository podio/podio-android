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

import com.podio.sdk.client.RequestFuture;
import com.podio.sdk.domain.Application;
import com.podio.sdk.filter.ApplicationFilter;

public class ApplicationProvider extends BasicPodioProvider {

    /**
     * Fetches the full content set of the application with the given id.
     * 
     * @param applicationId
     *        The id of the application to fetch.
     * @return A ticket which the caller can use to identify this request with.
     */
    public RequestFuture<Application> get(long applicationId) {
        ApplicationFilter filter = new ApplicationFilter().withApplicationId(applicationId)
                .withType("full").withWorkspaceField();

        return get(filter, Application.class);
    }

    /**
     * Fetches a short subset of the application with the given id.
     * 
     * @param applicationId
     *        The id of the application to fetch.
     * @return A ticket which the caller can use to identify this request with.
     */
    public RequestFuture<Application> getShort(long applicationId) {
        ApplicationFilter filter = new ApplicationFilter().withApplicationId(applicationId)
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
    public RequestFuture<Application> getMini(long applicationId) {
        ApplicationFilter filter = new ApplicationFilter().withApplicationId(applicationId)
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
    public RequestFuture<Application> getMicro(long applicationId) {
        ApplicationFilter filter = new ApplicationFilter().withApplicationId(applicationId)
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
    public RequestFuture<Application[]> getAllActive(long spaceId) {
        ApplicationFilter filter = new ApplicationFilter().withSpaceId(spaceId)
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
    public RequestFuture<Application[]> getAll(long spaceId) {
        ApplicationFilter filter = new ApplicationFilter().withSpaceId(spaceId)
                .withInactivesIncluded(true);

        return get(filter, Application[].class);
    }

}
