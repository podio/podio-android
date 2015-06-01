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

package com.podio.sdk.domain.field;

import com.podio.sdk.domain.File;
import com.podio.sdk.domain.Right;
import com.podio.sdk.internal.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author László Urszuly
 */
public class ContactField extends Field<ContactField.Value> {
    /**
     * This class describes the particular settings of a Contact field configuration.
     *
     * @author László Urszuly
     */
    private static class Settings {
        private final String type = null;
        private final Type[] valid_types = null;
    }

    /**
     * This class describes the specific configuration of a Contact field.
     *
     * @author László Urszuly
     */
    public static class Configuration extends Field.Configuration {
        private final Value default_value = null;
        private final Settings settings = null;

        public Value getDefaultValue() {
            return default_value;
        }

        public String getType() {
            return settings != null ? settings.type : null;
        }

        public List<Type> getValidTypes() {
            return settings != null && settings.valid_types != null ? Arrays.asList(settings.valid_types) : Arrays.asList(new Type[0]);
        }
    }

    /**
     * This class describes the Contact value data.
     *
     * @author László Urszuly
     */
    public static class Data {
        private final File image = null;
        private final Long org_id = null;
        private final Long profile_id = null;
        private final Long space_id = null;
        private final Long user_id;
        private final String[] mail = null;
        private final List<String> rights = null;
        private final String external_id = null;
        private final String last_seen_on = null;
        private final String link = null;
        private final String name = null;
        private final String type = null;

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
            return Arrays.asList(mail);
        }

        public boolean hasAllRights(Right... rights) {
            if (Utils.isEmpty(this.rights) && Utils.isEmpty(rights)) {
                // The user has no rights and wants to verify that.
                return true;
            }

            if (Utils.notEmpty(this.rights) && Utils.notEmpty(rights)) {
                for (Right right : rights) {
                    if (!this.rights.contains(right.name())) {
                        return false;
                    }
                }
                return true;
            }

            return false;
        }

        public boolean hasAnyRights(Right... rights) {
            if (Utils.isEmpty(this.rights) && Utils.isEmpty(rights)) {
                // The user has no rights and wants to verify that.
                return true;
            }

            if (Utils.notEmpty(this.rights) && Utils.notEmpty(rights)) {
                for (Right right : rights) {
                    if (this.rights.contains(right.name())) {
                        return true;
                    }
                }
            }

            return false;
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
            try {
                return Type.valueOf(type);
            } catch (NullPointerException e) {
                return Type.undefined;
            } catch (IllegalArgumentException e) {
                return Type.undefined;
            }
        }
    }

    /**
     * This class describes a Contact field value.
     *
     * @author László Urszuly
     */
    public static class Value extends Field.Value {
        private final Data value;

        public Value(Data contact) {
            this.value = contact;
        }

        public Value(long contactId) {
            this.value = new Data(contactId);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Value) {
                Value other = (Value) o;

                if (other.value != null && this.value != null) {
                    return other.value.getUserId() == this.value.getUserId();
                }
            }

            return false;
        }

        @Override
        public Map<String, Object> getCreateData() {
            HashMap<String, Object> data = null;
            long userId = getUserId();

            if (userId > 0L) {
                data = new HashMap<String, Object>();
                data.put("value", userId);
            }

            return data;
        }

        @Override
        public int hashCode() {
            return value != null ? (int) value.getUserId() : 0;
        }

        public List<String> getEmailAddresses() {
            return value != null ? value.getEmailAddresses() : new ArrayList<String>();
        }

        public String getExternalId() {
            return value != null ? value.getExternalId() : null;
        }

        public File getImageData() {
            return value != null ? value.getImageData() : null;
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

        public long getOrganizationId() {
            return value != null ? value.getOrganizationId() : -1L;
        }

        public long getProfileId() {
            return value != null ? value.getProfileId() : -1L;
        }

        public long getSpaceId() {
            return value != null ? value.getSpaceId() : -1L;
        }

        public Type getType() {
            return value != null ? value.getType() : Type.undefined;
        }

        public long getUserId() {
            return value != null ? value.getUserId() : -1L;
        }

        public boolean hasAllRights(Right... rights) {
            return value != null ? value.hasAllRights(rights) : false;
        }

        public boolean hasAnyRights(Right... rights) {
            return value != null ? value.hasAnyRights(rights) : false;
        }
    }

    public static enum Type {
        user, space, undefined
    }

    // Private fields
    private final Configuration config = null;
    private final ArrayList<Value> values;

    public ContactField(String externalId) {
        super(externalId);
        this.values = new ArrayList<Value>();
    }

    @Override
    public void addValue(Value value) {
        if (values != null && !values.contains(value)) {
            values.add(value);
        }
    }

    @Override
    public Value getValue(int index) {
        return values != null ? values.get(index) : null;
    }

    @Override
    public List<Value> getValues() {
        return values;
    }

    @Override
    public void removeValue(Value value) {
        if (values != null && values.contains(value)) {
            values.remove(value);
        }
    }

    @Override
    public int valuesCount() {
        return values != null ? values.size() : 0;
    }

    public Configuration getConfiguration() {
        return config;
    }

}
