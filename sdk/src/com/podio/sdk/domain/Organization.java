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

import com.podio.sdk.internal.utils.Utils;

public class Organization {

    public static enum Role {
        admin, regular, light, undefined
    }

    public static enum Segment {
        education, undefined
    }

    public static enum Status {
        active, inactive, deleted, undefined
    }

    public static enum Type {
        free, sponsored, premium, undefined
    }

    private final Integer logo = null;
    private final Integer org_id = null;
    private final Integer rank = null;
    private final Integer sales_agent_id = null;
    private final Integer user_limit = null;
    private final List<String> domains = null;
    private final List<Right> rights = null;
    private final List<Space> spaces = null;
    private final Role role = null;
    private final Segment segment = null;
    private final Status status = null;
    private final String created_on = null;
    private final String name = null;
    private final String url = null;
    private final String url_label = null;
    private final Type type = null;
    private final User created_by = null;

    public User getCreatedByUser() {
        return created_by;
    }

    /**
     * Gets the end date of the calendar event as a Java Date object.
     * 
     * @return A date object, or null if the date couldn't be parsed.
     */
    public Date getCreatedDate() {
        return Utils.parseDateTime(created_on);
    }

    public String getCreatedDateString() {
        return created_on;
    }

    public List<String> getDomains() {
        return domains != null ?
                new ArrayList<String>(domains) :
                new ArrayList<String>();
    }

    public int getId() {
        return Utils.getNative(org_id, -1);
    }

    public int getLogoId() {
        return Utils.getNative(logo, -1);
    }

    public String getName() {
        return name;
    }

    public int getRank() {
        return Utils.getNative(rank, 0);
    }

    public Role getRole() {
        return role != null ? role : Role.undefined;
    }

    public int getSalesAgentId() {
        return Utils.getNative(sales_agent_id, -1);
    }

    public Segment getSegment() {
        return segment != null ? segment : Segment.undefined;
    }

    public List<Space> getSpaces() {
        return spaces != null ?
                new ArrayList<Space>(spaces) :
                new ArrayList<Space>();
    }

    public Status getStatus() {
        return status != null ? status : Status.undefined;
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

    public int getUserLimit() {
        return Utils.getNative(user_limit, 5);
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
