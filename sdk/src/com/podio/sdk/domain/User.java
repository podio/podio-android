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

import android.os.Parcel;
import android.os.Parcelable;

import com.podio.sdk.internal.Utils;

/**
 * A Java representation of the UserDTO API domain object.
 * 
 * @author László Urszuly
 */
public class User implements Parcelable {
    public static final User EMPTY = new User();

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
        public static final Email EMPTY = new Email();

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
            dest.writeString(Utils.getObject(mail, ""));
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
        dest.writeString(Utils.getObject(status, Status.unknown.name()));
        dest.writeString(Utils.getObject(created_on, ""));
        dest.writeString(Utils.getObject(mail, ""));
        dest.writeTypedArray(Utils.getObject(mails, new Email[0]), flags);
        dest.writeString(Utils.getObject(locale, ""));
        dest.writeString(Utils.getObject(timezone, ""));
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
