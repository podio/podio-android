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

import com.podio.sdk.internal.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author László Urszuly
 */
public class DurationField extends Field<DurationField.Value> {
    /**
     * This class describes the particular settings of a Duration field configuration.
     *
     * @author László Urszuly
     */
    private static class Settings {
        private final List<String> fields = null;
    }

    /**
     * This class describes the specific configuration of a Duration field.
     *
     * @author László Urszuly
     */
    public static class Configuration extends Field.Configuration {
        private final Value default_value = null;
        private final Settings settings = null;

        public Value getDefaultValue() {
            return default_value;
        }

        public boolean hasAllSettingsFieldTypes(SettingFieldType... settingFieldTypes) {
            if ((settings == null || Utils.isEmpty(settings.fields)) && Utils.isEmpty(settingFieldTypes)) {
                // This field has no field types and the user wants to verify that.
                return true;
            }

            if ((settings != null && Utils.notEmpty(settings.fields)) && Utils.notEmpty(settingFieldTypes)) {
                for (SettingFieldType settingFieldType : settingFieldTypes) {
                    if (!settings.fields.contains(settingFieldType.name())) {
                        return false;
                    }
                }
                return true;
            }

            return false;
        }

        public boolean hasSettingsFieldType(SettingFieldType settingFieldType) {

            if ((settings != null && Utils.notEmpty(settings.fields)) && settingFieldType != null) {
                return settings.fields.contains(settingFieldType.name());
            }

            return false;
        }
    }

    /**
     * This class describes a Duration field value.
     *
     * @author László Urszuly
     */
    public static class Value extends Field.Value {
        private final Integer value;

        /**
         *
         * @param value -1 if not set.
         */
        public Value(int value) {
            this.value = value != -1 ? value : null;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Value) {
                Value other = (Value) o;
                return Utils.getNative(other.value, -1) == Utils.getNative(this.value, -1);
            }

            return false;
        }

        @Override
        public Map<String, Object> getCreateData() {
            HashMap<String, Object> data = null;

            if (value != null) {
                data = new HashMap<String, Object>();
                data.put("value", value);
            }

            return data;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }

        /**
         * Will return -1 if not set.
         *
         * @return
         */
        public int getDuration() {
            return Utils.getNative(value, -1);
        }
    }

    public static enum SettingFieldType {
        days, hours, minutes, seconds
    }

    // Private fields.
    private final Configuration config = null;
    private final ArrayList<Value> values;

    public DurationField(String externalId) {
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
            //duration field do not support multiple values so
            // that is why we clear the values on each method call
            values.clear();
            values.add(0, value);
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
