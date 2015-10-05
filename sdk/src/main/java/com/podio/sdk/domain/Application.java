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

import com.podio.sdk.domain.data.Data;
import com.podio.sdk.domain.field.Field;
import com.podio.sdk.internal.Utils;

import java.util.List;

public class Application implements Data{

    public enum Status {
        active, inactive, deleted, undefined;

        public static Status fromString(String string) {
            try {
                return Status.valueOf(string);
            } catch (IllegalArgumentException e) {
                return Status.undefined;
            } catch (NullPointerException e) {
                return Status.undefined;
            }
        }
    }

    public enum Type {
        standard, meeting, undefined;

        public static Type fromString(String string) {
            try {
                return Type.valueOf(string);
            } catch (IllegalArgumentException e) {
                return Type.undefined;
            } catch (NullPointerException e) {
                return Type.undefined;
            }
        }
    }

    public enum View {
        badge, calendar, card, stream, table, undefined;

        public static View fromString(String string) {
            try {
                return View.valueOf(string);
            } catch (IllegalArgumentException e) {
                return View.undefined;
            } catch (NullPointerException e) {
                return View.undefined;
            }
        }
    }

    public static class Configuration {
        private final Boolean allow_attachments = null;
        private final Boolean allow_comments = null;
        private final Boolean allow_create = null;
        private final Boolean allow_edit = null;
        private final Boolean allow_tags = null;
        private final Boolean approved = null;
        private final Boolean disable_notifications = null;
        private final Boolean fivestar = null;
        private final Boolean rsvp = null;
        private final Boolean show_app_item_id = null;
        private final Boolean silent_creates = null;
        private final Boolean silent_edits = null;
        private final Boolean thumbs = null;
        private final Boolean yesno = null;
        private final Integer app_item_id_padding = null;
        private final String app_item_id_prefix = null;
        private final String description = null;
        private final String external_id = null;
        private final String fivestar_label = null;
        private final String icon = null;
        private final Integer icon_id = null;
        private final String item_name = null;
        private final String name = null;
        private final String rsvp_label = null;
        private final String thumbs_label = null;
        private final String usage = null;
        private final String yesno_label = null;
        private final String type = null;
        private final String default_view = null;

        private Configuration() {
        }

        public boolean allowsAttachments() {
            return Utils.getNative(allow_attachments, false);
        }

        public boolean allowsComments() {
            return Utils.getNative(allow_comments, false);
        }

        public boolean allowsCreate() {
            return Utils.getNative(allow_create, false);
        }

        public boolean allowsEdit() {
            return Utils.getNative(allow_edit, false);
        }

        public boolean allowsTags() {
            return Utils.getNative(allow_tags, false);
        }

        public String getAppItemIdPrefix() {
            return app_item_id_prefix;
        }

        public View getDefaultViewType() {
            return View.fromString(default_view);
        }

        public String getDescription() {
            return description;
        }

        public String getExternalId() {
            return external_id;
        }

        public String getFiveStarLabel() {
            return fivestar_label;
        }

        public String getIconName() {
            return icon;
        }

        public Integer getIconId(){
            return icon_id;
        }

        public String getItemName() {
            return item_name;
        }

        public int getAppItemIdPaddingCount() {
            return Utils.getNative(app_item_id_padding, 0);
        }

        public String getName() {
            return name;
        }

        public String getRsvpLabel() {
            return rsvp_label;
        }

        public String getThumbsLabel() {
            return thumbs_label;
        }

        public Type getType() {
            return Type.fromString(type);
        }

        public String getUsageInfo() {
            return usage;
        }

        public String getYesNoLabel() {
            return yesno_label;
        }

        public boolean hasDisabledNotifications() {
            return Utils.getNative(disable_notifications, false);
        }

        public boolean hasFiveStarRating() {
            return Utils.getNative(fivestar, false);
        }

        public boolean hasRsvpState() {
            return Utils.getNative(rsvp, false);
        }

        public boolean hasSilentCreates() {
            return Utils.getNative(silent_creates, false);
        }

        public boolean hasSilentEdits() {
            return Utils.getNative(silent_edits, false);
        }

        public boolean hasThumbsVoting() {
            return Utils.getNative(thumbs, false);
        }

        public boolean hasYesNoVoting() {
            return Utils.getNative(yesno, false);
        }

        public boolean isApproved() {
            return Utils.getNative(approved, false);
        }

        public boolean showAppItemId() {
            return Utils.getNative(show_app_item_id, false);
        }
    }

    public static Application newInstance() {
        return new Application();
    }

    private final Boolean pinned = null;
    private final Boolean subscribed = null;
    private final Configuration config = null;
    private final Field[] fields = null;
    private final List<String> rights = null;
    private final Long current_revision = null;
    private final Long original_revision = null;
    private final Long app_id = null;
    private final Long default_view_id = null;
    private final Long original = null;
    private final Long space_id = null;
    private final Profile owner = null;
    private final Space space = null;
    private final String status = null;
    private final String link = null;
    private final String link_add = null;
    private final String mailbox = null;
    private final String token = null;
    private final String url = null;
    private final String url_add = null;
    private final String url_label = null;

    private Application() {
    }

    public boolean isPinned() {
        return Utils.getNative(pinned, false);
    }

    public boolean isSubscribed() {
        return Utils.getNative(subscribed, false);
    }

    public String getAddLink() {
        return link_add;
    }

    public String getAddUrl() {
        return url_add;
    }

    public long getAppId() {
        return Utils.getNative(app_id, -1L);
    }

    /**
     * Returns the configuration settings for this application. If the API didn't provide a
     * configuration object then a default configuration will be returned.
     *
     * @return A configuration object. Never null.
     */
    public Configuration getConfiguration() {
        return config != null ? config : new Configuration();
    }

    public long getCurrentRevisionId() {
        return Utils.getNative(current_revision, -1L);
    }

    public long getDefaultViewId() {
        return Utils.getNative(default_view_id, -1L);
    }

    /**
     * Returns the singular name of this app (e.g. when creating a new item) as defined in the
     * configuration object. This is a convenience method.
     *
     * @return The singular name of this app.
     */
    public String getItemName() {
        return config != null ? config.getItemName() : null;
    }

    public String getLink() {
        return link;
    }

    public String getMailbox() {
        return mailbox;
    }

    /**
     * Returns the name of this App as defined in the configuration object. This is a convenience
     * method.
     *
     * @return The name of this app.
     */
    public String getName() {
        return config != null ? config.getName() : null;
    }

    public long getOriginAppId() {
        return Utils.getNative(original, -1L);
    }

    public long getOriginAppRevisionId() {
        return Utils.getNative(original_revision, -1L);
    }

    public Profile getOwner() {
        return owner;
    }

    public long getSpaceId() {
        return Utils.getNative(space_id, -1L);
    }

    public Space getSpace() {
        return space;
    }

    public Status getStatus() {
        return Status.fromString(status);
    }

    /**
     * Gets a copy of the immutable array of fields for this application.
     *
     * @return A list of fields. Never null.
     */
    public Field[] getTemplate() {
        return fields != null ? fields : new Field[0];
    }

    public String getToken() {
        return token;
    }

    /**
     * Returns the {@link com.podio.sdk.domain.Application.Type Type} of this app as defined in the
     * configuration object. This is a convenience method.
     *
     * @return The type of this app.
     */
    public Type getType() {
        return config != null ? config.getType() : Type.undefined;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlLabel() {
        return url_label;
    }

    /**
     * Checks whether the list of rights the user has for this application contains <em>all</em> the
     * given permissions.
     *
     * @param permissions
     *         The list of permissions to check for.
     *
     * @return Boolean true if all given permissions are found or no permissions are given. Boolean
     * false otherwise.
     */
    public boolean hasAllRights(Right... permissions) {
        if (rights != null) {
            for (Right permission : permissions) {
                if (!rights.contains(permission.name())) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    /**
     * Checks whether the list of rights the user has for this application contains <em>any</em> of
     * the given permissions. The method doesn't say anything about which of the given permissions
     * match the test.
     *
     * @param permissions
     *         The list of any single permission to check for.
     *
     * @return Boolean true if any of the given permissions match user rights. Boolean false
     * otherwise.
     */
    public boolean hasAnyRights(Right... permissions) {
        if (rights != null) {
            for (Right permission : permissions) {
                if (rights.contains(permission.name())) {
                    return true;
                }
            }
        }

        return false;
    }
}
