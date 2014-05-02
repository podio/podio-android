package com.podio.sdk.domain;

public final class Item {

    public static final class Activity {
        private Activity() {
            // Hide the constructor.
        }
    }

    public static final class AccountInfo {
        private AccountInfo() {
            // Hide the constructor.
        }
    }

    public static final class Client {
        private Client() {
            // Hide the constructor.
        }
    }

    public static final class Comment {
        private Comment() {
            // Hide the constructor.
        }
    }

    public static final class Config {
        public static final Value default_value = null;
        public static final String description = null;
        public static final Settings settings = null;
        public static final Boolean required = null;
        // public static final Mapping mapping = null;
        public static final String label = null;
        public static final Boolean visible = null;
        public static final Integer delta = null;
        public static final Boolean hidden = null;

        private Config() {
            // Hide the constructor.
        }
    }

    public static final class ExcerptInfo {
        private ExcerptInfo() {
            // Hide the constructor.
        }
    }

    public static final class Field {
        public static final Long field_id = null;
        public static final String external_id = null;
        public static final String status = null;
        public static final String type = null;
        public static final String label = null;
        public static final Value[] values = null;

        private Field() {
            // Hide the constructor.
        }
    }

    public static final class FileInfo {
        public final Long file_id = null;
        public final String hosted_by = null;
        public final String hosted_by_humanized_name = null;
        public final String thumbnail_link = null;
        public final String link = null;
        public final String link_target = null;

        private FileInfo() {
            // Hide the constructor.
        }
    }

    public static final class Options {
        public static final String status = null;
        public static final String text = null;
        public static final Long id = null;
        public static final String color = null;

        private Options() {
            // Hide the constructor.
        }
    }

    public static final class ParticipantInfo {
        private ParticipantInfo() {
            // Hide the constructor.
        }
    }

    public static final class PresenceInfo {
        private PresenceInfo() {
            // Hide the constructor.
        }
    }

    public static final class PushInfo {
        private PushInfo() {
            // Hide the constructor.
        }
    }

    public static final class RatingInfo {
        private RatingInfo() {
            // Hide the constructor.
        }
    }

    public static final class ReferenceInfo {
        private ReferenceInfo() {
            // Hide the constructor.
        }
    }

    public static final class Revision {
        public final Long revision = null;
        public final Long app_revision = null;
        public final UserInfo created_by = null;
        public final Client created_via = null;
        public final String created_on = null;

        private Revision() {
            // Hide the constructor.
        }
    }

    public static final class Settings {
        public static final String size = null;
        public static final Boolean multiple = null;

        private Settings() {
            // Hide the constructor.
        }
    }

    public static final class UserInfo {
        public final Long user_id = null;
        public final String name = null;
        public final String url = null;
        public final String type = null;
        public final FileInfo image = null;
        public final String avatar_type = null;
        public final Long avatar = null;
        public final Long id = null;
        public final Long avatar_id = null;
        public final String last_seen_on = null;

        private UserInfo() {
            // Hide the constructor.
        }
    }

    public static final class Value {
        private Value() {
            // Hide the constructor.
        }
    }

    public static final class ValueInfo {
        private ValueInfo() {
            // Hide the constructor.
        }
    }

    public static final class RequestFilter {
        private RequestFilter() {
            // Hide the constructor.
        }
    }

    public static final class RequestResult {
        public final Integer total = null;
        public final Integer filtered = null;
        public final Item[] items = null;

        private RequestResult() {
            // Hide the constructor.
        }
    }

    public static final class RequestDescription {
        public final String sort_by;
        public final Boolean sort_desc;
        public final RequestFilter filters;
        public final Integer limit;
        public final Integer offset;
        public final Boolean remember;

        public RequestDescription(String sortBy, Boolean doSortDescending, RequestFilter filter,
                Integer limit, Integer offset, Boolean doRemember) {

            this.sort_by = sortBy;
            this.sort_desc = doSortDescending;
            this.filters = filter;
            this.limit = limit;
            this.offset = offset;
            this.remember = doRemember;
        }

    }

    public final Activity[] activity = null;
    public final Application app = null;
    public final Integer comment_count = null;
    public final Comment[] comments = null;
    public final UserInfo created_by = null;
    public final String created_on = null;
    public final Client created_via = null;
    public final Revision current_revision = null;
    public final ExcerptInfo excerpt = null;
    public final String external_id = null;
    public final Field[] fields = null;
    public final Integer file_count = null;
    public final FileInfo[] files = null;
    public final String[] grant = null; // message, action, created_by
    public final Integer grant_count = null;
    public final Revision initial_revision = null;
    public final Boolean is_liked = null;
    public final Long item_id = null;
    public final String last_event_on = null;
    public final Integer like_count = null;
    public final String link = null;
    public final AccountInfo linked_account_data = null;
    public final Long linked_account_id = null;
    public final ParticipantInfo participants = null;
    public final Boolean pinned = null;
    public final PresenceInfo presence = null;
    public final Integer priority = null;
    public final PushInfo push = null;
    public final RatingInfo ratings = null;
    public final ReferenceInfo ref = null;
    public final ReferenceInfo[] refs = null;
    public final Integer revision = null;
    public final Revision[] revisions = null;
    public final String[] rights = null;
    public final Space space = null;
    public final Boolean subscribed = null;
    public final Integer subscribed_count = null;
    public final String[] tags = null;
    public final String title = null;
    public final RatingInfo user_ratings = null;
    public final ValueInfo values = null;

    private Item() {
        // Hide the constructor.
    }

}
