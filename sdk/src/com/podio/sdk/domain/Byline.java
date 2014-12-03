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

import android.os.Parcel;
import android.os.Parcelable;

import com.podio.sdk.internal.Utils;

public class Byline implements Parcelable {
    public static final Byline EMPTY = new Byline();

    public static final Parcelable.Creator<Byline> CREATOR = new Parcelable.Creator<Byline>() {
        public Byline createFromParcel(Parcel in) {
            return new Byline(in);
        }

        public Byline[] newArray(int size) {
            return new Byline[size];
        }
    };

    private final Long id;
    private final Long user_id;
    private final Long avatar;
    private final Long avatar_id;
    private final String avatar_type; // See "Type" enum.
    private final String name;
    private final String last_seen_on;
    private final String type; // See "Type" enum.
    private final String url;
    private final File image;

    private Byline(Parcel parcel) {
        this.image = parcel.readParcelable(File.class.getClassLoader());
        this.id = parcel.readLong();
        this.user_id = parcel.readLong();
        this.avatar = parcel.readLong();
        this.avatar_id = parcel.readLong();
        this.avatar_type = parcel.readString();
        this.name = parcel.readString();
        this.last_seen_on = parcel.readString();
        this.type = parcel.readString();
        this.url = parcel.readString();
    }

    private Byline() {
        this.id = null;
        this.user_id = null;
        this.avatar = null;
        this.avatar_id = null;
        this.avatar_type = null;
        this.name = null;
        this.last_seen_on = null;
        this.type = null;
        this.url = null;
        this.image = null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(Utils.getObject(image, File.EMPTY), flags);
        dest.writeLong(Utils.getNative(id, -1L));
        dest.writeLong(Utils.getNative(user_id, -1L));
        dest.writeLong(Utils.getNative(avatar, -1L));
        dest.writeLong(Utils.getNative(avatar_id, -1L));
        dest.writeString(Utils.getObject(avatar_type, Type.unknown.name()));
        dest.writeString(Utils.getObject(name, ""));
        dest.writeString(Utils.getObject(last_seen_on, ""));
        dest.writeString(Utils.getObject(type, Type.unknown.name()));
        dest.writeString(Utils.getObject(url, ""));
    }

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
        return Utils.getNative(avatar, -1L);
    }

    public Type getAvatarType() {
        try {
            return Type.valueOf(avatar_type);
        } catch (NullPointerException e) {
            return Type.unknown;
        } catch (IllegalArgumentException e) {
            return Type.unknown;
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

    public Type getType() {
        try {
            return Type.valueOf(type);
        } catch (NullPointerException e) {
            return Type.unknown;
        } catch (IllegalArgumentException e) {
            return Type.unknown;
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
