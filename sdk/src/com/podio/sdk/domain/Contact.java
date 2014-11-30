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

import android.os.Parcel;
import android.os.Parcelable;

import com.podio.sdk.internal.Utils;

public class Contact implements Parcelable {

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public static enum Type {
        user, unknown
    }

    private final Long profile_id;
    private final Long user_id;
    private final Long space_id;
    private final Long org_id;
    private final Long avatar;
    private final String external_id;
    private final String type;
    private final String name;
    private final String[] title;
    private final String[] address;
    private final String zip;
    private final String city;
    private final String country;
    private final File image;
    private final String[] mail;
    private final String[] phone;
    private final String link;
    private final String last_seen_on;
    private final List<String> rights;

    private Contact(Parcel parcel) {
        this.profile_id = parcel.readLong();
        this.user_id = parcel.readLong();
        this.space_id = parcel.readLong();
        this.org_id = parcel.readLong();
        this.avatar = parcel.readLong();
        this.external_id = parcel.readString();
        this.type = parcel.readString();
        this.name = parcel.readString();
        this.title = parcel.createStringArray();
        this.address = parcel.createStringArray();
        this.zip = parcel.readString();
        this.city = parcel.readString();
        this.country = parcel.readString();
        this.image = parcel.readParcelable(ClassLoader.getSystemClassLoader());
        this.mail = parcel.createStringArray();
        this.phone = parcel.createStringArray();
        this.link = parcel.readString();
        this.last_seen_on = parcel.readString();

        this.rights = new ArrayList<String>();
        parcel.readStringList(rights);
    }

    private Contact() {
        this.profile_id = null;
        this.user_id = null;
        this.space_id = null;
        this.org_id = null;
        this.avatar = null;
        this.external_id = null;
        this.type = null;
        this.name = null;
        this.title = null;
        this.address = null;
        this.zip = null;
        this.city = null;
        this.country = null;
        this.image = null;
        this.mail = null;
        this.phone = null;
        this.link = null;
        this.last_seen_on = null;
        this.rights = null;
    }

    public long getProfileId() {
        return Utils.getNative(profile_id, -1L);
    }

    public long getUserId() {
        return Utils.getNative(user_id, -1L);
    }

    public long getSpaceId() {
        return Utils.getNative(space_id, -1L);
    }

    public long getOrganizationId() {
        return Utils.getNative(org_id, -1L);
    }

    public long getAvatarId() {
        return Utils.getNative(avatar, -1L);
    }

    public String getExternalId() {
        return external_id;
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

    public String getName() {
        return name;
    }

    public String getTitle() {
        return Utils.notEmpty(title) ? Utils.join(title, ", ") : null;
    }

    public String getAddress() {
        return Utils.notEmpty(address) ? Utils.join(address, ", ") : null;
    }

    public String[] getAddressLines() {
        return address;
    }

    public String getZip() {
        return zip;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public File getImage() {
        return image;
    }

    public String getImageUrl() {
        return image != null ? image.getLink() : null;
    }

    public String[] getEmailAddresses() {
        return mail;
    }

    public String[] getPhoneNumbers() {
        return phone;
    }

    public String getPodioLink() {
        return link;
    }

    public Date getLastSeenDate() {
        return Utils.parseDateTime(last_seen_on);
    }

    public String getLastSeenDateString() {
        return last_seen_on;
    }

    public boolean hasRights(String... permissions) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(Utils.getNative(this.profile_id, -1L));
        dest.writeLong(Utils.getNative(this.user_id, -1L));
        dest.writeLong(Utils.getNative(this.space_id, -1L));
        dest.writeLong(Utils.getNative(this.org_id, -1L));
        dest.writeLong(Utils.getNative(this.avatar, -1L));
        dest.writeString(this.external_id);
        dest.writeString(this.type);
        dest.writeString(this.name);
        dest.writeStringArray(this.title);
        dest.writeStringArray(this.address);
        dest.writeString(this.zip);
        dest.writeString(this.city);
        dest.writeString(this.country);
        dest.writeParcelable(this.image, 0);
        dest.writeStringArray(this.mail);
        dest.writeStringArray(this.phone);
        dest.writeString(this.link);
        dest.writeString(this.last_seen_on);
        dest.writeStringList(this.rights);
    }

}
