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

/**
 * A Java representation of the UserDTO API domain object.
 * 
 * @author László Urszuly
 */
public class User {

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
    public static class Email {
        private final Boolean disabled = null;
        private final Boolean primary = null;
        private final Boolean verified = null;
        private final String mail = null;

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

    private final Long user_id = null;
    private final String status = null;
    private final String created_on = null;
    private final String mail = null;
    private final Email[] mails = null;
    private final String locale = null;
    private final String timezone = null;

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
