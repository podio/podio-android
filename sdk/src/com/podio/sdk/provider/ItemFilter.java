package com.podio.sdk.provider;

public final class ItemFilter extends PodioFilter {

    public ItemFilter() {
        super("item");
    }

    public ItemFilter withApplicationId(long applicationId) {
        addPathSegment("app");
        addPathSegment(Long.toString(applicationId, 10));
        addPathSegment("filter");
        return this;
    }

    public ItemFilter withItemId(long itemId) {
        addPathSegment(Long.toString(itemId, 10));
        return this;
    }
}
