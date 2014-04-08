package com.podio.sdk.domain;

import com.podio.sdk.Filter;

public class AppItemProvider extends ItemProvider<AppItem> {

    public Filter fetchAppItemsForSpace(long spaceId) {
        Filter filter = new AppItemFilter().withSpaceId(spaceId).withInactivesIncluded(false);
        fetchItems(filter);
        return filter;
    }

    public Filter fetchAppItemsForSpaceWithInactivesIncluded(long spaceId) {
        Filter filter = new AppItemFilter().withSpaceId(spaceId).withInactivesIncluded(true);
        fetchItems(filter);
        return filter;
    }

}
