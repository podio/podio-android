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

import com.podio.sdk.domain.Profile;

import java.util.ArrayList;
import java.util.Arrays;
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
        private final String[] valid_types = null;
    }

    /**
     * This class describes the specific configuration of a Contact field.
     *
     * @author László Urszuly
     */
    public static class Configuration extends Field.Configuration {

        public enum Type {
            space_contacts, space_users, all_users, undefined;

            public static Type fromString(String string) {
                try {
                    return Type.valueOf(string);
                } catch (IllegalArgumentException e) {
                    return Type.undefined;
                } catch (NullPointerException e) {
                    return Type.undefined;
                }
            }
        }

        private final Value default_value = null;
        private final Settings settings = null;

        public Value getDefaultValue() {
            return default_value;
        }

        public Type getType() {
            return settings != null ? Type.fromString(settings.type) : Type.undefined;
        }

        public List<String> getValidTypes() {
            return settings != null && settings.valid_types != null ? Arrays.asList(settings.valid_types) : Arrays.asList(new String[0]);
        }
    }

    /**
     * This class describes a Contact field value.
     *
     * @author László Urszuly
     */
    public static class Value extends Field.Value {
        private final Profile value;

        public Value(Profile contact) {
            this.value = contact;
        }

        @Override
        public Map<String, Object> getCreateData() {
            HashMap<String, Object> data = null;
            long profileId = value != null ? value.getId() : 0L;

            if (profileId > 0L) {
                data = new HashMap<String, Object>();
                data.put("value", profileId);
            }

            return data;
        }

        public String getExternalId() {
            return value != null ? value.getExternalId() : null;
        }

        public Profile getProfile() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Value value1 = (Value) o;

            return !(value != null ? !value.equals(value1.value) : value1.value != null);

        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }

    // Private fields
    private final Configuration config = null;
    private final ArrayList<Value> values;

    public ContactField(String externalId) {
        super(externalId);
        this.values = new ArrayList<Value>();
    }

    @Override
    public void setValues(List<Value> values) {
        this.values.clear();
        this.values.addAll(values);
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
    public void clearValues() {
        values.clear();
    }

    @Override
    public int valuesCount() {
        return values != null ? values.size() : 0;
    }

    public Configuration getConfiguration() {
        return config;
    }

}
