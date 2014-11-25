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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.podio.sdk.internal.Utils;

public class User {

    public static enum Flag {
        god,
        bulletin_author,
        app_store_manager,
        experiment_manager,
        org_viewer,
        org_manager,
        api_manager,
        extension_manager,
        tnol_manager,
        seo_manager,
        out_of_office,
        sales,
        sales_default,
        sales_large,
        sales_contract,
        undefined
    }

    public static enum Status {
        inactive,
        active,
        deleted,
        blocked,
        blacklisted,
        undefined
    }

    public static class Email {
        private final Boolean disabled = null;
        private final Boolean primary = null;
        private final Boolean verified = null;
        private final String mail = null;

        public String getAddress() {
            return mail;
        }

        public boolean isDisabled() {
            return Utils.getNative(disabled, false);
        }

        public boolean isPrimary() {
            return Utils.getNative(primary, false);
        }

        public boolean isVerified() {
            return Utils.getNative(verified, false);
        }
    }

    public static class Profile {
        private final File image = null;
        private final Long avatar = null;
        private final Long org_id = null;
        private final Long profile_id = null;
        private final Long space_id = null;
        private final Long user_id = null;
        private final List<String> location = null;
        private final List<String> mail = null;
        private final List<String> phone = null;
        private final List<Right> rights = null;
        private final List<String> title = null;
        private final String about = null;
        private final String external_id = null;
        private final String link = null;
        private final String last_seen_on = null;
        private final String name = null;
        // TODO consider offering an enum representation of the possible types
        // so the API becomes clearer for a user of the SDK
        private final String type = null;

        public String getAbout() {
            return about;
        }

        public String getType() {
            return type;
        }

        public long getAvatarId() {
            return Utils.getNative(avatar, -1L);
        }

        public List<String> getEmailAddresses() {
            return mail != null ?
                    new ArrayList<String>(mail) :
                    new ArrayList<String>();
        }

        public String getExternalId() {
            return external_id;
        }

        public long getId() {
            return Utils.getNative(profile_id, -1L);
        }

        public File getImage() {
            return image;
        }

        /**
         * Gets the date when the user was activated.
         * 
         * @return A date object, or null if the date couldn't be parsed.
         */
        public Date getLastSeenDate() {
            return Utils.parseDateTime(last_seen_on);
        }

        public String getLastSeenDateString() {
            return last_seen_on;
        }

        public String getLink() {
            return link;
        }

        public List<String> getLocations() {
            return location != null ?
                    new ArrayList<String>(location) :
                    new ArrayList<String>();
        }

        public String getName() {
            return name;
        }

        public long getOrganizationId() {
            return Utils.getNative(org_id, -1L);
        }

        public List<String> getPhoneNumbers() {
            return phone != null ?
                    new ArrayList<String>(phone) :
                    new ArrayList<String>();
        }

        public List<String> getTitles() {
            return title != null ?
                    new ArrayList<String>(title) :
                    new ArrayList<String>();
        }

        public long getUserId() {
            return Utils.getNative(user_id, -1L);
        }

        public long getWorkspaceId() {
            return Utils.getNative(space_id, -1L);
        }

        /**
         * Checks whether the list of rights the user has for this application
         * contains <em>all</em> the given permissions.
         * 
         * @param permissions
         *        The list of permissions to check for.
         * @return Boolean true if all given permissions are found or no
         *         permissions are given. Boolean false otherwise.
         */
        public boolean hasRights(Right... permissions) {
            if (rights != null) {
                for (Right permission : permissions) {
                    if (!rights.contains(permission)) {
                        return false;
                    }
                }

                return true;
            }

            return false;
        }
    }

    private final Long user_id = null;
    private final Long profile_id = null;
    private final Long org_id = null;
    private final Long space_id = null;
    private final String name = null;
    private final String link = null;
    private final String url = null;
    private final File image = null;
    private final Long avatar = null;
    private final List<String> flags = null;
    private final List<Email> mails = null;
    private final String type = null;
    private final String status = null;
    private final String last_seen_on = null;
    private final String activated_on = null;
    private final String created_on = null;
    private final String locale = null;
    private final String mail = null;
    private final String timezone = null;
    private final Integer inbox_new = null;
    private final Integer message_unread_count = null;
    private final Push push = null;
    private final Profile profile = null;
    private final Presence presence = null;

    private User() {
    }

    public Presence getPresence() {
        return presence;
    }

    public Push getPush() {
        return push;
    }

    public Profile getProfile() {
        return profile;
    }

    public long getProfileId() {
        return Utils.getNative(profile_id, -1L);
    }

    public long getOrganizationId() {
        return Utils.getNative(org_id, -1L);
    }

    public long getSpaceId() {
        return Utils.getNative(space_id, -1L);
    }

    public String getLink() {
        return link != null ? link : url;
    }

    public int getInboxNew() {
        return Utils.getNative(inbox_new, -1);
    }

    public File getImage() {
        return image;
    }

    public int getUnreadMessagesCount() {
        return Utils.getNative(message_unread_count, -1);
    }

    /**
     * Gets the date when the user was activated.
     * 
     * @return A date object, or null if the date couldn't be parsed.
     */
    public Date getActivatedDate() {
        return Utils.parseDateTime(activated_on);
    }

    public String getActivatedDateString() {
        return activated_on;
    }

    public Date getLastSeenDate() {
        return Utils.parseDateTime(last_seen_on);
    }

    public String getLastSeenDateString() {
        return last_seen_on;
    }

    /**
     * Gets the date when the user was created.
     * 
     * @return A date object, or null if the date couldn't be parsed.
     */
    public Date getCreatedDate() {
        return Utils.parseDateTime(created_on);
    }

    public String getCreatedDateString() {
        return created_on;
    }

    public String getEmailAddress() {
        return mail;
    }

    public List<Email> getEmails() {
        return mails != null ?
                new ArrayList<Email>(mails) :
                new ArrayList<Email>();
    }

    public long getId() {
        return Utils.getNative(user_id, -1L);
    }

    public String getName() {
        return name;
    }

    public long getAvatar() {
        return Utils.getNative(avatar, -1L);
    }

    public String getLocale() {
        return locale;
    }

    public Status getStatus() {
        try {
            return Status.valueOf(status);
        } catch (NullPointerException e) {
            return Status.undefined;
        } catch (IllegalArgumentException e) {
            return Status.undefined;
        }
    }

    public String getType() {
        return type;
    }

    public String getTimezone() {
        return timezone;
    }

    public boolean hasFlags(Flag... data) {
        if (Utils.notEmpty(flags)) {
            for (Flag flag : data) {
                if (!flags.contains(flag.name())) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }
}
