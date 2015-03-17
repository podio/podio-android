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

/**
 * @author rabie
 */
public class Comment {
    private final String value = null;
    private final String rich_value = null;
    private final List<File> files = null;
    private final List<Right> rights = null;
    private final Long comment_id = null;
    private final Byline created_by = null;
    private final String created_on = null;
    private final Integer like_count = null;
    private Boolean is_liked = null;
    private Embed embed = null;
    private File embed_file = null;

    public String getRichValue() {
        return rich_value;
    }

    public File getEmbedFile() {
        return embed_file;
    }

    public Embed getEmbed() {
        return embed;
    }

    public String getValue() {
        return value;
    }

    public List<File> getFiles() {
        return files;
    }

    /**
     * Checks whether the list of rights the user has for this comment contains <em>all</em> the
     * given permissions.
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

    public long getCommentId() {
        return Utils.getNative(comment_id, -1L);
    }

    public Byline getCreatedBy() {
        return created_by;
    }

    public Date getCreatedOnDate() {
        return Utils.parseDateTime(created_on);
    }

    public String getCreatedOnString() {
        return created_on;
    }

    public Integer getLikeCount() {
        return Utils.getNative(like_count, 0);
    }

    public Boolean isLiked() {
        return is_liked;
    }
}
