package com.podio.sdk.domain;

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
}
