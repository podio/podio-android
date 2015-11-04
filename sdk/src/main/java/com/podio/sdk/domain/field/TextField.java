
package com.podio.sdk.domain.field;

import com.podio.sdk.internal.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Podio Text field domain object.
 *
 * @author László Urszuly
 */
public class TextField extends Field<TextField.Value> {
    /**
     * This class describes the particular settings of a Text field configuration.
     *
     * @author László Urszuly
     */
    private static class Settings {
        private final String size = null;
    }

    /**
     * This class describes the specific configuration of a Text field.
     *
     * @author László Urszuly
     */
    public static class Configuration extends Field.Configuration {
        private final Value default_value = null;
        private Settings settings = null;

        public Configuration() {
            settings = new Settings();
        }

        public Value getDefaultValue() {
            return default_value;
        }

        public Size getSize() {
            try {
                return Size.valueOf(settings.size);
            } catch (NullPointerException e) {
                return Size.undefined;
            } catch (IllegalArgumentException e) {
                return Size.undefined;
            }
        }
    }

    /**
     * This class describes a Text field value.
     *
     * @author László Urszuly
     */
    public static class Value extends Field.Value {
        private final String value;

        public Value(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Value) {
                Value other = (Value) o;

                if (other.value != null) {
                    return other.value.equals(this.value);
                }
            }

            return false;
        }

        @Override
        public Map<String, Object> getCreateData() {
            HashMap<String, Object> data = null;

            if (Utils.notEmpty(value)) {
                data = new HashMap<String, Object>();
                data.put("value", value);
            }

            return data;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }

        public String getValue() {
            return value;
        }

    }

    /**
     * The values for the named sizes a text field can have.
     *
     * @author László Urszuly
     */
    public static enum Size {
        large, small, undefined
    }

    // Private fields.
    private Configuration config = null;
    private ArrayList<Value> values;

    public TextField(String externalId) {
        super(externalId);
        this.values = new ArrayList<Value>();
    }

    public TextField(CalculationField calculationField) {
        super(calculationField);

        config = new Configuration();
        this.values = new ArrayList<>();
        for (Field.Value calcValue : calculationField.getValues()) {
            this.values.add((TextField.Value)calcValue);
        }
    }

    @Override
    public void setValues(List<Value> values) {
        this.values.clear();
        this.values.addAll(values);
    }

    @Override
    public void addValue(Value value) {
        if (values != null && !values.contains(value)) {
            //text field do not support multiple values so
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
