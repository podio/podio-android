
package com.podio.sdk.domain.field;

import com.podio.sdk.internal.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class NumberField extends Field<NumberField.Value> {
    /**
     * This class describes the particular settings of a Number field configuration.
     *
     */
    private static class Settings {
        private Integer decimals = null;
        private Boolean isCreateViewEditHidden = null;

        public Settings(Integer decimals, boolean isCreateViewEdithidden) {
            this.decimals = decimals;
            this.isCreateViewEditHidden = isCreateViewEdithidden;
        }
    }

    /**
     * This class describes the specific configuration of a Number field.
     *
     */
    public static class Configuration extends Field.Configuration {
        private final Value default_value = null;
        private Settings settings = null;

        public Configuration(Settings settings) {
            this.settings = settings;
        }

        public Value getDefaultValue() {
            return default_value;
        }

        public int getNumberOfDecimals() {
            return settings != null ? Utils.getNative(settings.decimals, 0) : 0;
        }

        @Override
        public boolean getIsHiddenCreateViewEdit() {
            if(settings != null && settings.isCreateViewEditHidden != null) {
                return settings.isCreateViewEditHidden;
            }
            return super.getIsHiddenCreateViewEdit();
        }
    }

    /**
     * This class describes a Number field value.
     *
     */
    public static class Value extends Field.Value {
        private final String value;

        public Value(Double value) {
            if (value != null) {
                this.value = Double.toString(value);
            } else {
                this.value = null;
            }
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

        public String getValue() {
            return value;
        }

    }

    // Private fields.
    private Configuration config = null;
    private final ArrayList<Value> values;

    public NumberField(String externalId) {
        super(externalId);
        this.values = new ArrayList<Value>();
    }

    public NumberField(CalculationField calculationField) {
        super(calculationField);

        final CalculationField.Configuration configuration = calculationField.getConfiguration();
        config = new Configuration(new Settings(configuration.getNumberOfDecimals(), configuration.getIsHiddenCreateViewEdit()));

        this.values = new ArrayList<>();
        for (Field.Value calcValue : calculationField.getValues()) {
            this.values.add((NumberField.Value)calcValue);
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
