package com.podio.sdk.domain;

public class ItemProvider extends PodioProvider<Item> {

    public Object fetchItemsForApplication(long applicationId) {
        ItemFilter filter = new ItemFilter() //
                .withApplicationId(applicationId);

        return fetchItems(filter);
    }

}
