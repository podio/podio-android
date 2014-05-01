package com.podio.sdk.domain;

public class ItemProvider extends PodioProvider {

    public Object fetchItemsForApplication(long applicationId) {
        ItemFilter filter = new ItemFilter() //
                .withApplicationId(applicationId);

        return pushRequest(filter, null);
    }

}
