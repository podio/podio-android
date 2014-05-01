package com.podio.sdk.domain;

public final class Item {

    public static final class Filter {

    }

    public static final class FilterRequest {
        public final String sort_by;
        public final Boolean sort_desc;
        public final Filter filters;
        public final Integer limit;
        public final Integer offset;
        public final Boolean remember;

        public FilterRequest(String sortBy, Boolean doSortDescending, Filter filter, Integer limit,
                Integer offset, Boolean doRemember) {

            this.sort_by = sortBy;
            this.sort_desc = doSortDescending;
            this.filters = filter;
            this.limit = limit;
            this.offset = offset;
            this.remember = doRemember;
        }
    }

    public static final class FilterResult {
        public final Integer total = null;
        public final Integer filtered = null;
        public final Item[] items = null;

        private FilterResult() {
            // Hide the constructor.
        }
    }

    public static final class Creator {
        private Creator() {
            // Hide the constructor.
        }
    }

    public static final class Client {
        private Client() {
            // Hide the constructor.
        }
    }

    public static final class Revision {
        public final Long revision = null;
        public final Long app_revision = null;
        public final Creator created_by = null;
        public final Client created_via = null;
        public final String created_on = null;

        private Revision() {
            // Hide the constructor.
        }
    }

    public static final class Value {
        private Value() {
            // Hide the constructor.
        }
    }

    public static final class Field {
        public static final Long field_id = null;
        public static final String type = null;
        public static final String label = null;
        public static final Value[] values = null;

        private Field() {
            // Hide the constructor.
        }
    }

    public static final class Rating {
        private Rating() {
            // Hide the constructor.
        }
    }

    public final Long item_id = null;
    public final Revision initial_revision = null;
    public final Revision current_revision = null;
    public final String last_event_on = null;
    public final Long external_id = null;
    public final String title = null;
    public final String link = null;
    public final String[] rights = null;
    public final Field[] fiels = null;
    public final Integer comment_count = null;
    public final Rating ratings = null;
    public final Integer file_count = null;

    private Item() {
        // Hide the constructor.
    }

}
