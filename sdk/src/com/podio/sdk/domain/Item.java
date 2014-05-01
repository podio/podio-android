package com.podio.sdk.domain;

public final class Item {

    public static final class FilterResult {
        public final Integer total = 0;
        public final Integer filtered = 0;
        public final Item[] items = {};

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
        public final Long revision = 0L;
        public final Long app_revision = 0L;
        public final Creator created_by = new Creator();
        public final Client created_via = new Client();
        public final String created_on = "";

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
        public static final Long field_id = 0L;
        public static final String type = "";
        public static final String label = "";
        public static final Value[] values = {};

        private Field() {
            // Hide the constructor.
        }
    }

    public static final class Rating {
        private Rating() {
            // Hide the constructor.
        }
    }

    public final Long item_id = 0L;
    public final Revision initial_revision = new Revision();
    public final Revision current_revision = new Revision();
    public final String last_event_on = "";
    public final Long external_id = 0L;
    public final String title = "";
    public final String link = "";
    public final String[] rights = {};
    public final Field[] fiels = {};
    public final Integer comment_count = 0;
    public final Rating[] ratings = {};
    public final Integer file_count = 0;

    private Item() {
        // Hide the constructor.
    }

}
