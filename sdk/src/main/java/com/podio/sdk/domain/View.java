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

import java.util.Date;
import java.util.Set;

import com.google.gson.annotations.SerializedName;
import com.podio.sdk.internal.Utils;

/**
 * Domain object representing saved app views that you can use to filter your
 * app item lists.
 * 
 * @author Tobias Lindberg
 */
public class View {

    public static enum Type {
        standard, saved, undefined
    }

    private final Long view_id = null;
    private final String name = null;
    private final String created_on = null;
    private final User created_by = null;
    private final Set<Right> rights = null;
    @SerializedName("private")
    private final Boolean is_private = null;
    private final Integer items = null;
    private final Type type = null;

    private View() {
    }

    public long getId() {
        return Utils.getNative(view_id, -1L);
    }

    public String getName() {
        return name;
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

    public User getCreatedByUser() {
        return created_by;
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

    public boolean isPrivate() {
        return Utils.getNative(is_private, false);
    }

    public int getItemsCount() {
        return Utils.getNative(items, -1);
    }

    public Type getType() {
        return type != null ? type : Type.undefined;
    }
}
