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

import static com.podio.sdk.internal.Utils.FALSE;
import static com.podio.sdk.internal.Utils.TRUE;

import java.util.Date;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.podio.sdk.internal.Utils;

/**
 * A Java representation of the UserDTO API domain object.
 * 
 * @author László Urszuly
 */
public class User implements Parcelable {

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public static enum Status {
        inactive,
        active,
        deleted,
        blocked,
        unknown
    }

    /**
     * A Java implementation of the UserMailDTO domain object.
     * 
     * @author László Urszuly
     */
    public static class Email implements Parcelable {

        public static final Parcelable.Creator<User.Email> CREATOR = new Parcelable.Creator<User.Email>() {
            public User.Email createFromParcel(Parcel in) {
                return new Email(in);
            }

            public User.Email[] newArray(int size) {
                return new Email[size];
            }
        };

        private final Boolean disabled;
        private final Boolean primary;
        private final Boolean verified;
        private final String mail;

        private Email(Parcel parcel) {
            this.disabled = (parcel.readInt() == 1);
            this.primary = (parcel.readInt() == 1);
            this.verified = (parcel.readInt() == 1);
            this.mail = parcel.readString();
        }

        private Email() {
            this.disabled = null;
            this.primary = null;
            this.verified = null;
            this.mail = null;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(disabled ? TRUE : FALSE);
            dest.writeInt(primary ? TRUE : FALSE);
            dest.writeInt(verified ? TRUE : FALSE);
            dest.writeString(mail);
        }

        public String getAddress() {
            return mail;
        }

        public boolean isDisabled() {
            return Utils.getNative(disabled, false);
        }

        public boolean isPrimary() {
            return Utils.getNative(primary, false);
        }

        public boolean isVerified() {
            return Utils.getNative(verified, false);
        }

    }

    public static class Profile implements Parcelable {

        public static final Parcelable.Creator<User.Profile> CREATOR = new Parcelable.Creator<User.Profile>() {
            public User.Profile createFromParcel(Parcel in) {
                return new Profile(in);
            }

            public User.Profile[] newArray(int size) {
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
            this.user_id = parcel.readLong();
            this.profile_id = parcel.readLong();
            this.org_id = parcel.readLong();
            this.space_id = parcel.readLong();
            this.external_id = parcel.readString();
            this.last_seen_on = parcel.readString();
            this.type = parcel.readString();
            this.link = parcel.readString();
            this.rights = parcel.createStringArrayList();
            this.push = parcel.readParcelable(ClassLoader.getSystemClassLoader());
            this.image = parcel.readParcelable(ClassLoader.getSystemClassLoader());

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
            dest.writeLong(user_id);
            dest.writeLong(profile_id);
            dest.writeLong(org_id);
            dest.writeLong(space_id);
            dest.writeString(external_id);
            dest.writeString(last_seen_on);
            dest.writeString(type);
            dest.writeString(link);
            dest.writeStringList(rights);
            dest.writeParcelable(push, flags);
            dest.writeParcelable(image, flags);

            // Non-ProfileDTO's
            dest.writeString(name);
            dest.writeString(about);
            dest.writeStringArray(title);
            dest.writeStringArray(location);
            dest.writeStringArray(phone);
            dest.writeStringArray(mail);
            dest.writeStringArray(address);
            dest.writeString(zip);
            dest.writeString(city);
            dest.writeString(country);
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

    private final Long user_id;
    private final String status;
    private final String created_on;
    private final String mail;
    private final Email[] mails;
    private final String locale;
    private final String timezone;

    private User(Parcel parcel) {
        this.user_id = parcel.readLong();
        this.status = parcel.readString();
        this.created_on = parcel.readString();
        this.mail = parcel.readString();
        this.mails = parcel.createTypedArray(Email.CREATOR);
        this.locale = parcel.readString();
        this.timezone = parcel.readString();
    }

    private User() {
        this.user_id = null;
        this.status = null;
        this.created_on = null;
        this.mail = null;
        this.mails = null;
        this.locale = null;
        this.timezone = null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) {
            return false;
        }

        User other = (User) o;
        long uid1 = Utils.getNative(user_id, -1);
        long uid2 = other.getUserId();

        return uid1 == uid2 && uid1 != -1;
    }

    @Override
    public int hashCode() {
        final int prime = 37;
        int result = 1;
        result = prime * result + (user_id != null ? user_id.hashCode() : 0);

        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(Utils.getNative(user_id, -1L));
        dest.writeString(status);
        dest.writeString(created_on);
        dest.writeString(mail);
        dest.writeTypedArray(mails, flags);
        dest.writeString(locale);
        dest.writeString(timezone);
    }

    public Date getCreatedDate() {
        return Utils.parseDateTime(created_on);
    }

    public String getCreatedDateString() {
        return created_on;
    }

    public String getEmailAddress() {
        return mail;
    }

    public Email[] getEmails() {
        return mails != null ? mails.clone() : new Email[0];
    }

    public long getUserId() {
        return Utils.getNative(user_id, -1L);
    }

    public String getLocale() {
        return locale;
    }

    public Status getStatus() {
        try {
            return Status.valueOf(status);
        } catch (NullPointerException e) {
            return Status.unknown;
        } catch (IllegalArgumentException e) {
            return Status.unknown;
        }
    }

    public String getTimezone() {
        return timezone;
    }

}
