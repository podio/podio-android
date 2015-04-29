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

import com.podio.sdk.internal.Utils;

import java.util.Date;
import java.util.List;

public class Space {

    public static enum Privacy {
        open, closed, undefined
    }

    public static enum Role {
        admin, regular, light, undefined
    }

    public static enum Type {
        regular, emp_network, demo, undefined
    }

    public static Space newInstance() {
        return new Space();
    }

    private final Boolean archived = null;
    private final Boolean auto_join = null;
    private final Boolean post_on_new_app = null;
    private final Boolean post_on_new_member = null;
    private final Boolean premium = null;
    private final Boolean subscribed = null;
    private final Boolean top = null;
    private final Integer rank = null;
    private final Long space_id = null;
    private final List<String> rights = null;
    private final Organization org = null;
    private final String created_on = null;
    private final String description = null;
    private final String name = null;
    private final String privacy = null;
    private final String role = null;
    private final String type = null;
    private final String url = null;
    private final String url_label = null;
    private final String video = null;
    private final Profile created_by = null;

    // These attributes are defined in the API source code,
    // but not supported by the SDK right now.
    //private final Object owner = null;
    //private final Object tier = null;
    //private final Object is_overdue = null;
    //private final Object org_id = null;
    //private final Object push = null;
    //private final Object member_count = null;
    //private final Object last_activity_on = null;
    //private final Object top_members = null;
    //private final Object app_count = null;
    //private final Object top_apps = null;

    private Space() {
    }

    public boolean doAutoJoin() {
        return Utils.getNative(auto_join, false);
    }

    public boolean doPostOnNewApp() {
        return Utils.getNative(post_on_new_app, false);
    }

    public boolean doPostOnNewMember() {
        return Utils.getNative(post_on_new_member, false);
    }

    public Profile getCreatedBy() {
        return created_by;
    }

    public Date getCreatedDate() {
        return Utils.parseDateTime(created_on);
    }

    public String getCreatedDateString() {
        return created_on;
    }

    public String getDescription() {
        return description;
    }

    public long getSpaceId() {
        return Utils.getNative(space_id, -1L);
    }

    public String getName() {
        return name;
    }

    public Organization getOrganization() {
        return org;
    }

    public Privacy getPrivacy() {
        try {
            return Privacy.valueOf(privacy);
        } catch (NullPointerException e) {
            return Privacy.undefined;
        } catch (IllegalArgumentException e) {
            return Privacy.undefined;
        }
    }

    public int getRank() {
        return Utils.getNative(rank, -1);
    }

    public Role getRole() {
        try {
            return Role.valueOf(role);
        } catch (NullPointerException e) {
            return Role.undefined;
        } catch (IllegalArgumentException e) {
            return Role.undefined;
        }
    }

    public Type getType() {
        try {
            return Type.valueOf(type);
        } catch (NullPointerException e) {
            return Type.undefined;
        } catch (IllegalArgumentException e) {
            return Type.undefined;
        }
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
     * Checks whether the list of rights the user has for this Space contains <em>all</em> the given
     * permissions.
     *
     * @param rights
     *         The list of permissions to check for.
     *
     * @return Boolean true if all given permissions are granted for this Space. Boolean false
     * otherwise.
     */
    public boolean hasAllRights(Right... rights) {
        if (Utils.isEmpty(this.rights) && Utils.isEmpty(rights)) {
            // The user has no rights and wants to verify that.
            return true;
        }

        if (Utils.notEmpty(this.rights) && Utils.notEmpty(rights)) {
            for (Right right : rights) {
                if (!this.rights.contains(right.name())) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    /**
     * Checks whether the list of rights the user has for this Space contains <em>any</em> of the
     * given permissions.
     *
     * @param rights
     *         The list of permissions to check any single one for.
     *
     * @return Boolean true if any given permission is granted for this Space. Boolean false
     * otherwise.
     */
    public boolean hasAnyRights(Right... rights) {
        if (Utils.isEmpty(this.rights) && Utils.isEmpty(rights)) {
            // The user has no rights and wants to verify that.
            return true;
        }

        if (Utils.notEmpty(this.rights) && Utils.notEmpty(rights)) {
            for (Right right : rights) {
                if (this.rights.contains(right.name())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isArchived() {
        return Utils.getNative(archived, false);
    }

    public boolean isPremium() {
        return Utils.getNative(premium, false);
    }

    public boolean isSubscribed() {
        return Utils.getNative(subscribed, false);
    }

    public boolean isTop() {
        return Utils.getNative(top, false);
    }
}
