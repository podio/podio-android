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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.podio.sdk.internal.utils.Utils;

public class Space {
    private static final transient SimpleDateFormat FORMATTER_DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static enum Privacy {
        open, closed, undefined
    }

    public static enum Role {
        admin, regular, light, undefined
    }

    public static enum Type {
        regular, emp_network, demo, undefined
    }

    public static class Owner {
        private final Reference.Type type = null;
        private final Integer id = null;

        public Reference.Type getType() {
            return type != null ? type : Reference.Type.undefined;
        }

        public int getId() {
            return Utils.getNative(id, -1);
        }
    }

    private final Boolean auto_join = null;
    private final Boolean post_on_new_app = null;
    private final Boolean post_on_new_member = null;
    private final Boolean subscribed = null;
    private final Boolean premium = null;
    private final Integer rank = null;
    private final Integer space_id = null;
    private final List<String> rights = null;
    private final Organization org = null;
    private final Privacy privacy = null;
    private final Role role = null;
    private final String created_on = null;
    private final String description = null;
    private final String name = null;
    private final String url = null;
    private final String url_label = null;
    private final String video = null;
    private final Type type = null;
    private final User created_by = null;

    public boolean doAutoJoin() {
        return Utils.getNative(auto_join, false);
    }

    public boolean doPostOnNewApp() {
        return Utils.getNative(post_on_new_app, true);
    }

    public boolean doPostOnNewMember() {
        return Utils.getNative(post_on_new_member, true);
    }

    public User getCreatedByUser() {
        return created_by;
    }

    /**
     * Gets the end date of the calendar event as a Java Date object.
     * 
     * @return A date object, or null if the date couldn't be parsed.
     */
    public Date getCreatedDate() {
        Date date;

        try {
            date = FORMATTER_DATETIME.parse(created_on);
        } catch (ParseException e) {
            date = null;
        } catch (NullPointerException e) {
            date = null;
        }

        return date;
    }

    public String getCreatedDateString() {
        return created_on;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return Utils.getNative(space_id, -1);
    }

    public String getName() {
        return name;
    }

    public Organization getOrganization() {
        return org;
    }

    public Privacy getPrivacy() {
        return privacy != null ? privacy : Privacy.undefined;
    }

    public int getRank() {
        return Utils.getNative(rank, 0);
    }

    public Role getRole() {
        return role != null ? role : Role.undefined;
    }

    public Type getType() {
        return type != null ? type : Type.undefined;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlLabel() {
        return url_label;
    }

    public String getVideoId() {
        return video;
    }

    /**
     * Checks whether the list of rights the user has for this application
     * contains <em>all</em> the given permissions.
     * 
     * @param permissions
     *        The list of permissions to check for.
     * @return Boolean true if all given permissions are found or no permissions
     *         are given. Boolean false otherwise.
     */
    public boolean hasPermissions(String... permissions) {
        if (rights != null) {
            for (String permission : permissions) {
                if (!rights.contains(permission)) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public boolean isSubscribed() {
        return Utils.getNative(subscribed, false);
    }

    public boolean isPremium() {
        return Utils.getNative(premium, false);
    }
}
