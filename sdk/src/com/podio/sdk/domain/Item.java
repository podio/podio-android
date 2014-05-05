/*
 *  Copyright (C) 2014 Copyright Citrix Systems, Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of 
 *  this software and associated documentation files (the "Software"), to deal in 
 *  the Software without restriction, including without limitation the rights to 
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
 *  of the Software, and to permit persons to whom the Software is furnished to 
 *  do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all 
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 *  SOFTWARE.
 */

package com.podio.sdk.domain;

import com.google.gson.annotations.SerializedName;
import com.podio.sdk.domain.helper.AccountInfo;
import com.podio.sdk.domain.helper.FileInfo;
import com.podio.sdk.domain.helper.ParticipantInfo;
import com.podio.sdk.domain.helper.PresenceInfo;
import com.podio.sdk.domain.helper.RatingInfo;
import com.podio.sdk.domain.helper.ReferenceInfo;
import com.podio.sdk.domain.helper.UserInfo;
import com.podio.sdk.domain.helper.ValueInfo;

public final class Item {

    public static final class Activity {
        private Activity() {
            // Hide the constructor.
        }
    }

    public static final class Client {
        private Client() {
            // Hide the constructor.
        }
    }

    public static final class Config {
        public final Value default_value = null;
        public final String description = null;
        public final Settings settings = null;
        public final Boolean required = null;
        // public final Mapping mapping = null;
        public final String label = null;
        public final Boolean visible = null;
        public final Integer delta = null;
        public final Boolean hidden = null;

        private Config() {
            // Hide the constructor.
        }
    }

    public static final class Excerpt {
        public final String label = null;
        public final String text = null;

        private Excerpt() {
            // Hide the constructor.
        }
    }

    public static final class Field {
        public final Config config = null;
        public final String external_id = null;
        public final Long field_id = null;
        public final String label = null;
        public final String status = null;
        public final String type = null;
        public final Value[] values = null;

        private Field() {
            // Hide the constructor.
        }
    }

    public static final class Options {
        public final String status = null;
        public final String text = null;
        public final Long id = null;
        public final String color = null;

        private Options() {
            // Hide the constructor.
        }
    }

    public final class Revision {
        public final UserInfo created_by = null;
        public final String created_on = null;
        public final Client created_via = null;
        public final Integer item_revision_id = null;
        public final Integer revision = null;
        public final String type = null;

        private Revision() {
            // Hide the constructor.
        }
    }

    public static final class Settings {
        public final String size = null;
        public final Boolean multiple = null;

        private Settings() {
            // Hide the constructor.
        }
    }

    public static final class Value {
        @SerializedName("value")
        public final String value_string = null;
        @SerializedName("value")
        public final ObjectValue value_object = null;

        private Value() {
            // Hide the constructor.
        }
    }

    public static final class ObjectValue {
        // Date/time fields.
        public final String end = null;
        public final String end_date = null;
        public final String end_time = null;
        public final String start = null;
        public final String start_date = null;
        public final String start_time = null;

        // Money fields.
        public final Float value = null;
        public final String currency = null;

        // Embed fields
        public final String embed = null;
        public final String file = null;

        private ObjectValue() {
            // Hide the constructor.
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
    public final Excerpt excerpt = null;
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
