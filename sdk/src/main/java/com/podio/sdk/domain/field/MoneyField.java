
package com.podio.sdk.domain.field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author László Urszuly
 */
public class MoneyField extends Field<MoneyField.Value> {
    /**
     * This class describes the particular settings of a Money field configuration.
     *
     * @author László Urszuly
     */
    private static class Settings {
        private final String[] allowed_currencies = null;
    }

    /**
     * This class describes the specific configuration of a Money field.
     *
     * @author László Urszuly
     */
    public static class Configuration extends Field.Configuration {
        private final Value default_value = null;
        private final Settings settings = null;

        public Value getDefaultValue() {
            return default_value;
        }

        public List<String> getAllowedCurrencies() {
            return settings != null ? Arrays.asList(settings.allowed_currencies) : Arrays.asList(new String[0]);
        }
    }

    /**
     * This class describes a Money field value.
     *
     * @author László Urszuly
     */
    public static class Value extends Field.Value {
        private final String currency;
        private final String value;

        public Value(String currency, String value) {
            this.currency = currency;
            this.value = value;
        }

        public String getCurrency() {
            return currency;
        }

        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Value) {
                Value other = (Value) o;

                if (other.value != null && other.currency != null && this.value != null && this.currency != null) {
                    return other.currency.equals(this.currency) && other.value.equals(this.value);
                }
            }

            return false;
        }

        @Override
        public Map<String, Object> getCreateData() {
            HashMap<String, Object> data = null;

            if (value != null && currency != null) {
                data = new HashMap<String, Object>();
                data.put("currency", currency);
                data.put("value", value);
            }

            return data;
        }

        @Override
        public int hashCode() {
            return currency != null && value != null ? (currency + value).hashCode() : 0;
        }
    }

    // Private fields.
    private final Configuration config = null;
    private final ArrayList<Value> values;

    public MoneyField(String externalId) {
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
