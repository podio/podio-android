package com.podio.sdk.domain;

import com.podio.sdk.Filter;

/**
 * An AppItemProvider is a custom extension to the ItemProvider class. It
 * targets AppItems and enables very AppItem-specific helper methods to off-load
 * the caller from the boiler plate it takes to create and post a Podio request.
 * 
 * @author László Urszuly
 */
public final class AppItemProvider extends ItemProvider<AppItem> {

    public Object fetchAppItemsForSpace(long spaceId) {
        Filter filter = new AppItemFilter() //
                .withSpaceId(spaceId) //
                .withInactivesIncluded(false);

        return fetchItems(filter);
    }

    public Object fetchAppItemsForSpaceWithInactivesIncluded(long spaceId) {
        Filter filter = new AppItemFilter() //
                .withSpaceId(spaceId) //
                .withInactivesIncluded(true);
        return fetchItems(filter);
    }

}
