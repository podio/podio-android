package com.podio.sdk.domain;

public final class Organization {
    public final Boolean premium = Boolean.FALSE;
    public final Integer grants_count = 0;
    public final Integer rank = 0;
    public final Long org_id = 0L;
    public final Long logo = 0L;
    public final String name = null;
    public final String url = null;
    public final String url_label = null;
    public final String type = null;
    public final String role = null;
    public final String status = null;

    public final String[] rights = {};
    public final Space[] spaces = {};

    private Organization() {
        // Hide the constructor.
    }
}
