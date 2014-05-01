package com.podio.sdk.domain;

public class ItemProvider extends PodioProvider {

    public Object fetchItemsForApplication(long applicationId) {
        ItemFilter filter = new ItemFilter() //
                .withApplicationId(applicationId);

        Item.FilterRequest filterRequest = new Item.FilterRequest(null, null, null, null, null,
                null);

        return pushRequest(filter, filterRequest);
    }

}
