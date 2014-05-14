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

import com.podio.sdk.PodioParser;
import com.podio.sdk.RestClient;
import com.podio.sdk.domain.Application;
import com.podio.sdk.filter.ApplicationFilter;

public final class ApplicationProvider extends BasicPodioProvider {

    public ApplicationProvider(RestClient client) {
        super(client);
    }

    public Object fetchApplication(long applicationId) {
        PodioParser<Application> parser = new PodioParser<Application>(Application.class);

        ApplicationFilter filter = new ApplicationFilter() //
                .withApplicationId(applicationId) //
                .withType("full");

        return fetchRequest(filter, parser);
    }

    public Object fetchApplicationShort(long applicationId) {
        PodioParser<Application> parser = new PodioParser<Application>(Application.class);

        ApplicationFilter filter = new ApplicationFilter() //
                .withApplicationId(applicationId) //
                .withType("short");

        return fetchRequest(filter, parser);
    }

    public Object fetchApplicationMini(long applicationId) {
        PodioParser<Application> parser = new PodioParser<Application>(Application.class);

        ApplicationFilter filter = new ApplicationFilter() //
                .withApplicationId(applicationId) //
                .withType("mini");

        return fetchRequest(filter, parser);
    }

    public Object fetchApplicationMicro(long applicationId) {
        PodioParser<Application> parser = new PodioParser<Application>(Application.class);

        ApplicationFilter filter = new ApplicationFilter() //
                .withApplicationId(applicationId) //
                .withType("micro");

        return fetchRequest(filter, parser);
    }

    public Object fetchApplicationsForSpace(long spaceId) {
        PodioParser<Application[]> parser = new PodioParser<Application[]>(Application[].class);

        ApplicationFilter filter = new ApplicationFilter() //
                .withSpaceId(spaceId) //
                .withInactivesIncluded(false);

        return fetchRequest(filter, parser);
    }

    public Object fetchApplicationsForSpaceWithInactivesIncluded(long spaceId) {
        PodioParser<Application[]> parser = new PodioParser<Application[]>(Application[].class);

        ApplicationFilter filter = new ApplicationFilter() //
                .withSpaceId(spaceId) //
                .withInactivesIncluded(true);

        return fetchRequest(filter, parser);
    }

}