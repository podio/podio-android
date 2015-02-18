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

import com.podio.sdk.internal.Utils;

public class Byline {
    private final Long id = null;
    private final Long user_id = null;
    private final Long avatar = null;
    private final Long avatar_id = null;
    private final String avatar_type = null; // See "Type" enum.
    private final String name = null;
    private final String last_seen_on = null;
    private final String type = null; // See "Type" enum.
    private final String url = null;
    private final File image = null;

    public long getId() {
        return Utils.getNative(id, -1L);
    }

    public long getUserId() {
        return Utils.getNative(user_id, -1L);
    }

    public long getAvatar() {
        return Utils.getNative(avatar, -1L);
    }

    public long getAvatarId() {
        return Utils.getNative(avatar_id, -1L);
    }

    public ReferenceType getAvatarType() {
        try {
            return ReferenceType.valueOf(avatar_type);
        } catch (NullPointerException e) {
            return ReferenceType.unknown;
        } catch (IllegalArgumentException e) {
            return ReferenceType.unknown;
        }
    }

    public String getName() {
        return name;
    }

    public Date getLastSeenDate() {
        return Utils.parseDateTime(last_seen_on);
    }

    public String getLastSeenDateString() {
        return last_seen_on;
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

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return image != null ? image.getLink() : null;
    }

    public File getImage() {
        return image;
    }

}
