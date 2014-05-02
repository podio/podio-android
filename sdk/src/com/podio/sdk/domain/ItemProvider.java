package com.podio.sdk.domain;

public class ItemProvider extends PodioProvider {

    public Object fetchItemsForApplication(long applicationId) {
        ItemFilter filter = new ItemFilter() //
                .withApplicationId(applicationId);

        ItemRequest filterRequest = new ItemRequest(null, null, null, null, null, null);

        return pushRequest(filter, filterRequest);
    }

}
