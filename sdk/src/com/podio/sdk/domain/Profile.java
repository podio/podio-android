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

public class Profile implements Parcelable {
    public static final Profile EMPTY = new Profile();

    public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>() {
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    private final Long user_id;
    private final Long profile_id;
    private final Long org_id;
    private final Long space_id;
    private final String external_id;
    private final String last_seen_on;
    private final String type; // See the "Type" enum.
    private final String link;
    private final List<String> rights; // See the "Right" enum.
    private final Push push;
    private final File image;

    // Not found in the API ProfileDTO
    private final String name;
    private final String about;
    private final String[] title;
    private final String[] location;
    private final String[] phone;
    private final String[] mail;
    private final String[] address;         // -> ContactAPI, not in ProfileDTO
    private final String zip;               // -> ContactAPI, not in ProfileDTO
    private final String city;              // -> ContactAPI, not in ProfileDTO
    private final String country;           // -> ContactAPI, not in ProfileDTO

    private Profile(Parcel parcel) {
        this.push = parcel.readParcelable(Push.class.getClassLoader());
        this.image = parcel.readParcelable(File.class.getClassLoader());
        this.user_id = parcel.readLong();
        this.profile_id = parcel.readLong();
        this.org_id = parcel.readLong();
        this.space_id = parcel.readLong();
        this.external_id = parcel.readString();
        this.last_seen_on = parcel.readString();
        this.type = parcel.readString();
        this.link = parcel.readString();
        this.rights = parcel.createStringArrayList();

        // Non-ProfileDTO's
        this.name = parcel.readString();
        this.about = parcel.readString();
        this.title = parcel.createStringArray();
        this.location = parcel.createStringArray();
        this.phone = parcel.createStringArray();
        this.mail = parcel.createStringArray();
        this.address = parcel.createStringArray();
        this.zip = parcel.readString();
        this.city = parcel.readString();
        this.country = parcel.readString();
    }

    private Profile() {
        this.user_id = null;
        this.profile_id = null;
        this.org_id = null;
        this.space_id = null;
        this.external_id = null;
        this.last_seen_on = null;
        this.type = null;
        this.link = null;
        this.rights = null;
        this.push = null;
        this.image = null;

        // Non-ProfileDTO's
        this.name = null;
        this.about = null;
        this.title = null;
        this.location = null;
        this.phone = null;
        this.mail = null;
        this.address = null;
        this.zip = null;
        this.city = null;
        this.country = null;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Profile)) {
            return false;
        }

        Profile other = (Profile) o;
        long pid1 = Utils.getNative(profile_id, -1);
        long uid1 = Utils.getNative(user_id, -1);
        long pid2 = other.getId();
        long uid2 = other.getUserId();

        return (pid1 == pid2 && pid1 != -1) || (uid1 == uid2 && uid1 != -1);
    }

    @Override
    public int hashCode() {
        final int prime = 29;
        int result = 1;

        result = prime * result + (user_id != null ? user_id.hashCode() : 0);
        result = prime * result + (profile_id != null ? profile_id.hashCode() : 0);

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(Utils.getObject(push, Push.EMPTY), flags);
        dest.writeParcelable(Utils.getObject(image, File.EMPTY), flags);
        dest.writeLong(Utils.getNative(user_id, -1L));
        dest.writeLong(Utils.getNative(profile_id, -1L));
        dest.writeLong(Utils.getNative(org_id, -1L));
        dest.writeLong(Utils.getNative(space_id, -1L));
        dest.writeString(Utils.getObject(external_id, ""));
        dest.writeString(Utils.getObject(last_seen_on, ""));
        dest.writeString(Utils.getObject(type, ""));
        dest.writeString(Utils.getObject(link, ""));
        dest.writeStringList(Utils.getObject(rights, new ArrayList<String>()));

        // Non-ProfileDTO's
        String[] stringArrayEmpty = {};
        dest.writeString(Utils.getObject(name, ""));
        dest.writeString(Utils.getObject(about, ""));
        dest.writeStringArray(Utils.getObject(title, stringArrayEmpty));
        dest.writeStringArray(Utils.getObject(location, stringArrayEmpty));
        dest.writeStringArray(Utils.getObject(phone, stringArrayEmpty));
        dest.writeStringArray(Utils.getObject(mail, stringArrayEmpty));
        dest.writeStringArray(Utils.getObject(address, stringArrayEmpty));
        dest.writeString(Utils.getObject(zip, ""));
        dest.writeString(Utils.getObject(city, ""));
        dest.writeString(Utils.getObject(country, ""));
    }

    public String getAbout() {
        return about;
    }

    public String getType() {
        return type;
    }

    public String[] getEmailAddresses() {
        return mail != null ? mail.clone() : new String[0];
    }

    public String getExternalId() {
        return external_id;
    }

    public long getId() {
        return Utils.getNative(profile_id, -1L);
    }

    public File getImage() {
        return image;
    }

    public String getImageUrl() {
        return image != null ? image.getLink() : null;
    }

    public Date getLastSeenDate() {
        return Utils.parseDateTime(last_seen_on);
    }

    public String getLastSeenDateString() {
        return last_seen_on;
    }

    public String getLink() {
        return link;
    }

    public String[] getLocations() {
        return location != null ? location.clone() : new String[0];
    }

    public String getName() {
        return name;
    }

    public long getOrganizationId() {
        return Utils.getNative(org_id, -1L);
    }

    public String[] getPhoneNumbers() {
        return phone != null ? phone.clone() : new String[0];
    }

    public Push getPushTokens() {
        return push;
    }

    public String getThumbnailUrl() {
        return image != null ? image.getThumbnailLink() : null;
    }

    public String[] getTitles() {
        return title != null ? title.clone() : new String[0];
    }

    public long getUserId() {
        return Utils.getNative(user_id, -1L);
    }

    public long getWorkspaceId() {
        return Utils.getNative(space_id, -1L);
    }

    /**
     * Checks whether the list of rights the user has for this domain object
     * contains the given permission.
     * 
     * @param permission
     *        The permission to verify.
     * @return True if the given permission is granted, false otherwise.
     */
    public boolean hasRight(Right permission) {
        return rights != null && rights.contains(permission.name());
    }

}
