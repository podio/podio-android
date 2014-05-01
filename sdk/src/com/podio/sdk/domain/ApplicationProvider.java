package com.podio.sdk.domain;


public final class ApplicationProvider extends PodioProvider<Application> {

    public Object fetchApplicationsForSpace(long spaceId) {
        ApplicationFilter filter = new ApplicationFilter() //
                .withSpaceId(spaceId) //
                .withInactivesIncluded(false);

        return fetchItems(filter);
    }

    public Object fetchApplicationsForSpaceWithInactivesIncluded(long spaceId) {
        ApplicationFilter filter = new ApplicationFilter() //
                .withSpaceId(spaceId) //
                .withInactivesIncluded(true);

        return fetchItems(filter);
    }

}
