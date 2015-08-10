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

import com.podio.sdk.domain.Application;
import com.podio.sdk.domain.Item;
import com.podio.sdk.internal.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author László Urszuly
 */
public class ReferenceField extends Field<ReferenceField.Value> {
    /**
     * This class describes the particular settings of a Reference field configuration.
     *
     * @author László Urszuly
     */
    private static class Settings {
        private final Boolean multiple = null;
        private final Application[] apps = null;
        private final Long[] referencable_types = null;
    }

    /**
     * This class describes the specific configuration of a Reference field.
     *
     * @author László Urszuly
     */
    public static class Configuration extends Field.Configuration {
        private final Value default_value = null;
        private final Settings settings = null;

        public Value getDefaultValue() {
            return default_value;
        }

        public List<Application> getApps() {
            return settings != null && settings.apps != null ? Arrays.asList(settings.apps) : Arrays.asList(new Application[0]);
        }

        public List<Long> getReferencableTypes() {
            return settings != null && settings.referencable_types != null ? Arrays.asList(settings.referencable_types) : Arrays.asList(new Long[0]);
        }

        public boolean isMultiple() {
            return settings != null && Utils.getNative(settings.multiple, false);
        }
    }

    /**
     * This class describes a Reference field value.
     *
     * @author László Urszuly
     */
    public static class Value extends Field.Value {
        private final Item value;

        public Value(Item data) {
            this.value = data;
        }

        @Override
        public Map<String, Object> getCreateData() {
            HashMap<String, Object> data = null;
            long itemId = value != null ? value.getId() : 0L;
            if (itemId > 0L) {
                data = new HashMap<String, Object>();
                data.put("value", itemId);
            }

            return data;
        }

        public Item getItem(){
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

    // Private fields.
    private final Configuration config = null;
    private final ArrayList<Value> values;

    public ReferenceField(String externalId) {
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
