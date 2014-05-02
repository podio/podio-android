package com.podio.sdk.domain;

public final class ItemRequest {

    public static final class Filter {
        private Filter() {
            // Hide the constructor.
        }
    }

    public static final class Result {
        public final Integer total = null;
        public final Integer filtered = null;
        public final Item[] items = null;

        private Result() {
            // Hide the constructor.
        }
    }

    public final String sort_by;
    public final Boolean sort_desc;
    public final Filter filters;
    public final Integer limit;
    public final Integer offset;
    public final Boolean remember;

    public ItemRequest(String sortBy, Boolean doSortDescending, Filter filter, Integer limit,
            Integer offset, Boolean doRemember) {

        this.sort_by = sortBy;
        this.sort_desc = doSortDescending;
        this.filters = filter;
        this.limit = limit;
        this.offset = offset;
        this.remember = doRemember;
    }

}
