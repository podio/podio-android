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

package com.podio.sdk.domain.notification;

import com.podio.sdk.domain.Organization;
import com.podio.sdk.domain.Reference;
import com.podio.sdk.domain.Right;
import com.podio.sdk.domain.Space;
import com.podio.sdk.internal.Utils;

import java.util.List;

/**
 * This class is the base class of all notification contexts.
 *
 * @author Tobias Lindberg
 */
public abstract class NotificationContext {

    private final Reference ref = null;
    private final String title = null;
    private final List<Right> rights = null;
    private final Integer comment_count = null;
    private final Space space = null;
    private final Organization org = null;

    public Organization getOrganization() {
        return org;
    }

    public Space getSpace() {
        return space;
    }

    public int getCommentCount() {
        return Utils.getNative(comment_count, -1);
    }

    /**
     * Checks whether the list of rights the user has for this notification context contains
     * <em>all</em> the given permissions.
     *
     * @param permissions
     *         The list of permissions to check for.
     *
     * @return Boolean true if all given permissions are found or no permissions are given. Boolean
     * false otherwise.
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

    public String getTitle() {
        return title;
    }

    public Reference getReference() {
        return ref;
    }
}
