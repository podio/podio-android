package com.podio.sdk.provider;

public final class ApplicationProvider extends PodioProvider {

    public Object fetchApplicationsForSpace(long spaceId) {
        ApplicationFilter filter = new ApplicationFilter() //
                .withSpaceId(spaceId) //
                .withInactivesIncluded(false);

        return fetchRequest(filter);
    }

    public Object fetchApplicationsForSpaceWithInactivesIncluded(long spaceId) {
        ApplicationFilter filter = new ApplicationFilter() //
                .withSpaceId(spaceId) //
                .withInactivesIncluded(true);

        return fetchRequest(filter);
    }

}
