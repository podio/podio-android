package com.podio.sdk.provider;

import com.podio.sdk.domain.ItemRequest;

public class ItemProvider extends PodioProvider {

    public Object fetchItem(long itemId) {
        ItemFilter filter = new ItemFilter() //
                .withItemId(itemId);

        return fetchRequest(filter);
    }

    public Object fetchItemsForApplication(long applicationId) {
        ItemFilter filter = new ItemFilter() //
                .withApplicationId(applicationId);

        ItemRequest filterRequest = new ItemRequest(null, null, null, null, null, null);

        return pushRequest(filter, filterRequest);
    }

}
