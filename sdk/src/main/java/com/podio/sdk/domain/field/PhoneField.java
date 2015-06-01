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
 * The Podio Phone field domain object.
 *
 * @author Tobias Lindberg
 */
public class PhoneField extends Field<PhoneField.Value> {

    /**
     * This class describes the specific configuration of a Phone field.
     *
     * @author Tobias Lindberg
     */
    public static class Configuration extends Field.Configuration {
        private final Value default_value = null;

        public Value getDefaultValue() {
            return default_value;
        }
    }

    public static enum Type {
        mobile, work, home, main, work_fax, private_fax, other, undefined
    }

    /**
     * This class describes a Phone field value.
     *
     * @author Tobias Lindberg
     */
    public static class Value extends Field.Value {
        private final String type;
        private final String value;

        public Value(Type type, String value) {
            this.type = type.name();
            this.value = value;
        }

        @Override
        public Map<String, Object> getCreateData() {
            HashMap<String, Object> data = null;

            if (Utils.notEmpty(value) && Utils.notEmpty(type)) {
                data = new HashMap<String, Object>();
                data.put("value", value);
                data.put("type", type);
            }

            return data;
        }

        public String getValue() {
            return value;
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

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Value value1 = (Value) o;

            if (getType() != value1.getType()) return false;
            return value.equals(value1.value);

        }
    }

    // Private fields.
    private final Configuration config = null;
    private final ArrayList<Value> values;

    public PhoneField(String externalId) {
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
