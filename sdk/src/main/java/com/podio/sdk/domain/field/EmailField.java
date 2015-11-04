
package com.podio.sdk.domain.field;

import com.podio.sdk.internal.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Podio Email field domain object.
 *
 * @author Tobias Lindberg
 */
public class EmailField extends Field<EmailField.Value> {
    /**
     * This class describes the particular settings of a Email field configuration.
     *
     * @author Tobias Lindberg
     */
    private static class Settings {
        private final Boolean include_in_bcc = null;
        private final Boolean include_in_cc = null;
    }

    /**
     * This class describes the specific configuration of a Email field.
     *
     * @author Tobias Lindberg
     */
    public static class Configuration extends Field.Configuration {
        private final Value default_value = null;
        private final Settings settings = null;

        public Value getDefaultValue() {
            return default_value;
        }

        public boolean isIncludeInBcc() {
            return Utils.getNative(settings.include_in_bcc, false);
        }

        public boolean isIncludeInCc() {
            return Utils.getNative(settings.include_in_cc, false);
        }
    }

    public static enum Type {
        work, home, other, undefined
    }

    /**
     * This class describes a Email field value.
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

    public EmailField(String externalId) {
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
