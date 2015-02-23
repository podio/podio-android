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
package com.podio.sdk.domain.stream;

import com.podio.sdk.domain.Application;
import com.podio.sdk.domain.Byline;
import com.podio.sdk.domain.Comment;
import com.podio.sdk.domain.File;
import com.podio.sdk.domain.Organization;
import com.podio.sdk.domain.ReferenceType;
import com.podio.sdk.domain.Right;
import com.podio.sdk.domain.Space;
import com.podio.sdk.internal.Utils;

import java.util.Date;
import java.util.List;

/**
 * This class is the base class for all stream objects.
 * <p/>
 * In most cases all information we are interested in is provided by this class so even if you are
 * getting notifications of type {@link UnknownEventContext} there is still plenty of information
 * available in that one.
 *
 * @author Tobias Lindberg
 */
public abstract class EventContext {

    private final List<File> files = null;
    private final String type = null;
    private final List<Right> rights = null;
    private final String title = null;
    private final Application application = null;
    private final Boolean comments_allowed = null;
    private final Space space = null;
    private final Byline created_by = null;
    private final String created_on = null;

    private final List<Comment> comments = null;

    private final Organization org = null;

    private List<EventActivity> activity = null;

   public static class UserRatings{
    private Integer like = null;

       public Integer getLike(){
           return Utils.getNative(like, 0);
       }
   }

    private UserRatings user_ratings = null;

    public UserRatings getUserRatings(){
        return user_ratings;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Space getSpace() {
        return space;
    }

    public Boolean isCommentsAllowed() {
        return Utils.getNative(comments_allowed, false);
    }

    public Application getApplication() {
        return application;
    }

    public String getTitle() {
        return title;
    }

    public List<File> getFiles() {
        return files;
    }

    public Date getCreatedOnDate() {
        return Utils.parseDateTime(created_on);
    }

    public String getCreatedOnString() {
        return created_on;
    }

    public Byline getCreatedBy() {
        return created_by;
    }

    public ReferenceType getType() {
        try {
            return ReferenceType.valueOf(type);
        } catch (NullPointerException e) {
            return ReferenceType.unknown;
        } catch (IllegalArgumentException e) {
            return ReferenceType.unknown;
        }
    }

    public List<EventActivity> getActivity() {
        return activity;
    }

    public Organization getOrg() {
        return org;
    }

    /**
     * Checks whether the list of rights the user has for this stream object contains <em>all</em>
     * the given permissions.
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

}
