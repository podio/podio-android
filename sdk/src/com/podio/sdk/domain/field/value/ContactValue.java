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

package com.podio.sdk.domain.field.value;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.podio.sdk.domain.File;
import com.podio.sdk.domain.field.ContactField.Rights;
import com.podio.sdk.domain.field.ContactField.Type;
import com.podio.sdk.internal.Utils;

/**
 * @author László Urszuly
 */
public final class ContactValue extends AbstractValue {

    public static final class Data {
        private final File image = null;
        private final Long org_id = null;
        private final Long profile_id = null;
        private final Long space_id = null;
        private final Long user_id;
        private final List<String> mail = null;
        private final List<Rights> rights = null;
        private final String external_id = null;
        private final String last_seen_on = null;
        private final String link = null;
        private final String name = null;
        private final Type type = null;

        public Data(long contactId) {
            this.user_id = contactId;
        }

        public File getImageData() {
            return image;
        }

        public long getOrganizationId() {
            return Utils.getNative(org_id, -1L);
        }

        public long getProfileId() {
            return Utils.getNative(profile_id, -1L);
        }

        public long getSpaceId() {
            return Utils.getNative(space_id, -1L);
        }

        public long getUserId() {
            return Utils.getNative(user_id, -1L);
        }

        public List<String> getEmailAddresses() {
            return new ArrayList<String>(mail);
        }

        public List<Rights> getRights() {
            return new ArrayList<Rights>(rights);
        }

        public String getExternalId() {
            return external_id;
        }

        public Date getLastSeenDate() {
            return Utils.parseDateTime(last_seen_on);
        }

        public String getLink() {
            return link;
        }

        public String getName() {
            return name;
        }

        public Type getType() {
            return type != null ? type : Type.undefined;
        }
    }

    private final Data value;

    public ContactValue(Data contact) {
        this.value = contact;
    }

    public ContactValue(long contactId) {
        this.value = new Data(contactId);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ContactValue) {
            ContactValue other = (ContactValue) o;

            if (other.value != null && other.value.user_id != null && this.value != null && this.value.user_id != null) {
                return other.value.user_id.intValue() == this.value.user_id.intValue();
            }
        }

        return false;
    }

    @Override
    public Object getPushData() {
        HashMap<String, Long> data = null;
        long userId = getUserId();

        if (userId > 0L) {
            data = new HashMap<String, Long>();
            data.put("value", userId);
        }

        return data;
    }

    @Override
    public int hashCode() {
        return value != null && value.user_id != null ? value.user_id.intValue() : 0;
    }

    public File getImageData() {
        return value != null ? value.getImageData() : null;
    }

    public long getOrganizationId() {
        return value != null ? value.getOrganizationId() : -1L;
    }

    public long getProfileId() {
        return value != null ? value.getProfileId() : -1L;
    }

    public long getSpaceId() {
        return value != null ? value.getSpaceId() : -1L;
    }

    public long getUserId() {
        return value != null ? value.getUserId() : -1L;
    }

    public List<String> getEmailAddresses() {
        return value != null ? value.getEmailAddresses() : new ArrayList<String>();
    }

    public List<Rights> getRights() {
        return value != null ? value.getRights() : new ArrayList<Rights>();
    }

    public String getExternalId() {
        return value != null ? value.getExternalId() : null;
    }

    public Date getLastSeenDate() {
        return value != null ? value.getLastSeenDate() : null;
    }

    public String getLink() {
        return value != null ? value.getLink() : null;
    }

    public String getName() {
        return value != null ? value.getName() : null;
    }

    public Type getType() {
        return value != null ? value.getType() : Type.undefined;
    }

}
