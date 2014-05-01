package com.podio.sdk.domain;

public final class ApplicationFilter extends PodioFilter {

    public ApplicationFilter() {
        super("app");
    }

    public ApplicationFilter withInactivesIncluded(boolean doInclude) {
        addQueryParameter("include_inactive", doInclude ? "true" : "false");
        return this;
    }

    public ApplicationFilter withSpaceId(long spaceId) {
        addPathSegment("space");
        addPathSegment(Long.toString(spaceId, 10));
        return this;
    }

}
