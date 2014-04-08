package com.podio.sdk.domain;

public final class AppItemFilter extends ItemFilter {

    public AppItemFilter() {
        super("app");
    }

    public AppItemFilter withInactivesIncluded(boolean doInclude) {
        addQueryParameter("include_inactive", doInclude ? "true" : "false");
        return this;
    }

    public AppItemFilter withSpaceId(long spaceId) {
        addPathSegment("space");
        addPathSegment(Long.toString(spaceId, 10));
        return this;
    }

    public AppItemFilter withToken(String token) {
        addQueryParameter("oauth_token", token);
        return this;
    }

}
