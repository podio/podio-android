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

import android.renderscript.Sampler.Value;

import com.podio.sdk.domain.helper.AccountInfo;
import com.podio.sdk.domain.helper.ClientInfo;
import com.podio.sdk.domain.helper.FileInfo;
import com.podio.sdk.domain.helper.ParticipantInfo;
import com.podio.sdk.domain.helper.PresenceInfo;
import com.podio.sdk.domain.helper.RatingInfo;
import com.podio.sdk.domain.helper.ReferenceInfo;
import com.podio.sdk.domain.helper.UserInfo;
import com.podio.sdk.domain.helper.ValueInfo;

public final class Item {

    public static final class Activity {
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
    }

    public static final class Excerpt {
        public final String label = null;
        public final String text = null;
    }

    public static final class ApplicationField extends Field {
        public static final class ApplicationValue {
            public static final class Value {
                public final Application app = null;
                public final Long app_item_id = null;
                public final UserInfo created_by = null;
                public final String created_on = null;
                public final ClientInfo created_via = null;
                public final File[] files = null;
                public final Revision initial_revision = null;
                public final Long item_id = null;
                public final String link = null;
                public final Integer revision = null;
                public final Space space = null;
                public final String title = null;
            }

            public final Value value = null;
        }

        public final ApplicationValue[] values = null;
    }

    public static final class CalculationField extends Field {
        public static final class CalculationValue {
            public final String value = null;
        }

        public final CalculationField[] values = null;
    }

    public static final class CategoryField extends Field {
        public static final class CategoryValue {
            public static final class Value {
                public final String status = null;
                public final String text = null;
                public final Long id = null;
                public final String color = null;
            }

            public final Value value = null;
        }

        public final CategoryValue[] values = null;
    }

    public static final class ContactField extends Field {
        public static final class ContactValue {
            public static final class Value {
                public final Long user_id = null;
                public final Long space_id = null;
                public final String type = null;
                public final FileInfo image = null;
                public final Long profile_id = null;
                public final Long org_id = null;
                public final String link = null;
                public final String[] mail = null;
                public final String external_id = null;
                public final String last_seen_on = null;
                public final String name = null;
            }

            public final Value value = null;
        }

        public final ContactValue[] values = null;
    }

    public static final class DateField extends Field {
        public static final class DateValue {
            public final String end = null;
            public final String end_date = null;
            public final String end_date_utc = null;
            public final String end_time = null;
            public final String end_time_utc = null;
            public final String end_utc = null;
            public final String start = null;
            public final String start_date = null;
            public final String start_date_utc = null;
            public final String start_time = null;
            public final String start_time_utc = null;
            public final String start_utc = null;
        }

        public final DateValue[] values = null;
    }

    public static final class DurationField extends Field {
        public static final class DurationValue {
            public final Integer value = null;
        }

        public final DurationValue[] values = null;
    }

    public static final class EmbedField extends Field {
        public static final class EmbedValue {
            public static final class Value {
                public final String description = null;
                public final Integer embed_height = null;
                public final String embed_html = null;
                public final Long embed_id = null;
                public final Integer embed_width = null;
                public final String hostname = null;
                public final String original_url = null;
                public final String resolved_url = null;
                public final String title = null;
                public final String type = null;
                public final String url = null;
            }

            public final Value embed = null;
            public final FileInfo file = null;
        }

        public final EmbedValue[] values = null;
    }

    public static class Field {
        public static enum Type {
            app, calculation, category, contact, date, duration, embed, image, location, money, number, progress, text, title
        }

        public final Config config = null;
        public final String external_id = null;
        public final Long field_id = null;
        public final String label = null;
        public final String status = null;
        public final Type type = null;
    }

    public static final class ImageField extends Field {
        public static final class ImageValue {
            public static final class Value {
                public final String mimetype = null;
                public final String perma_link = null;
                public final String hosted_by = null;
                public final String description = null;
                public final String hosted_by_humanized_name = null;
                public final Integer size = null;
                public final String thumbnail_link = null;
                public final String link = null;
                public final Long file_id = null;
                public final String link_target = null;
                public final String name = null;
            }

            public final Value value = null;
        }

        public final ImageValue[] values = null;
    }

    public static final class LocationField extends Field {
        public static final class LocationValue {
            public final String city = null;
            public final String street_number = null;
            public final String country = null;
            public final String street_name = null;
            public final String formatted = null;
            public final String value = null;
            public final String state = null;
            public final String postal_code = null;
            public final Double lat = null;
            public final Double lng = null;
        }

        public final LocationValue[] values = null;
    }

    public static final class NumberField extends Field {
        public static final class NumberValue {
            public final String value = null;
        }

        public final NumberValue[] values = null;
    }

    public static final class MoneyField extends Field {
        public static final class MoneyValue {
            public final String currency = null;
            public final String value = null;
        }

        public final MoneyValue[] values = null;
    }

    public static final class ProgressField extends Field {
        public static final class ProgressValue {
            public final Integer value = null;
        }

        public final ProgressValue[] values = null;
    }

    public static final class TextField extends Field {
        public static final class TextValue {
            public final String value = null;
        }

        public final TextValue[] values = null;
    }

    public static final class TitleField extends Field {
        public static final class TitleValue {
            public final String value = null;
        }

        public final TitleValue[] values = null;
    }

    public static final class Options {
        public final String status = null;
        public final String text = null;
        public final Long id = null;
        public final String color = null;
    }

    public final class Revision {
        public final UserInfo created_by = null;
        public final String created_on = null;
        public final ClientInfo created_via = null;
        public final Integer item_revision_id = null;
        public final Integer revision = null;
        public final String type = null;
        public final UserInfo user = null;
    }

    public static final class Settings {
        public final String size = null;
        public final Boolean multiple = null;
    }

    public final Activity[] activity = null;
    public final Application app = null;
    public final Integer comment_count = null;
    public final Comment[] comments = null;
    public final UserInfo created_by = null;
    public final String created_on = null;
    public final ClientInfo created_via = null;
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
}
