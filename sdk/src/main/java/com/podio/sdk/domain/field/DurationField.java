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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.podio.sdk.internal.Utils;

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

        public boolean hasAllFieldTypes(FieldType... fieldTypes) {
            if ((settings == null || Utils.isEmpty(settings.fields)) && Utils.isEmpty(fieldTypes)) {
                // This field has no field types and the user wants to verify that.
                return true;
            }

            if ((settings != null && Utils.notEmpty(settings.fields)) && Utils.notEmpty(fieldTypes)) {
                for (FieldType fieldType : fieldTypes) {
                    if (!settings.fields.contains(fieldType.name())) {
                        return false;
                    }
                }
                return true;
            }

            return false;
        }

        public boolean hasAnyFieldType(FieldType... fieldTypes) {
            if ((settings == null || Utils.isEmpty(settings.fields)) && Utils.isEmpty(fieldTypes)) {
                // This field has no field types and the user wants to verify that.
                return true;
            }

            if ((settings != null && Utils.notEmpty(settings.fields)) && Utils.notEmpty(fieldTypes)) {
                for (FieldType fieldType : fieldTypes) {
                    if (settings.fields.contains(fieldType.name())) {
                        return true;
                    }
                }
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
        private final Long value;

        public Value(long value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Value) {
                Value other = (Value) o;
                return Utils.getNative(other.value, -1L) == Utils.getNative(this.value, -1L);
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

        public long getDuration() {
            return Utils.getNative(value, 0L);
        }
    }

    public static enum FieldType {
        hours, minutes, seconds, undefined
    }

    // Private fields.
    private final Configuration config = null;
    private final ArrayList<Value> values;

    public DurationField(String externalId) {
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
