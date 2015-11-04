
package com.podio.sdk.domain.field;

import com.podio.sdk.internal.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class ProgressField extends Field<ProgressField.Value> {
    /**
     * This class describes the particular settings of a Progress field configuration.
     *
     */
    private static class Settings {
    }

    /**
     * This class describes the specific configuration of a Progress field.
     *
     */
    public static class Configuration extends Field.Configuration {
        private final Value default_value = null;
        private final Settings settings = null;

        public Value getDefaultValue() {
            return default_value;
        }
    }

    /**
     * This class describes a Progress field value.
     *
     */
    public static class Value extends Field.Value {
        private final Integer value;

        public Value(int value) {
            this.value = value;
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
            return Utils.getNative(this.value, 0);
        }

        public int getValue() {
            return Utils.getNative(this.value, -1);
        }
    }

    // Private fields.
    private final Configuration config = null;
    private final ArrayList<Value> values;

    public ProgressField(String externalId) {
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
            values.clear();
            values.add(0,value);
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
