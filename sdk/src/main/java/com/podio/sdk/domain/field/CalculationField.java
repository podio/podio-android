
package com.podio.sdk.domain.field;

import com.podio.sdk.internal.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class CalculationField extends Field<Field.Value> {

    /**
     * This enum defines the supported types of calculation field.
     */
    public enum ReturnType {
        text(TextField.Value.class),
        date(DateField.Value.class),
        number(NumberField.Value.class),
        undefined(TextField.Value.class);

        private final Class<? extends Field.Value> fieldValueClass;

        ReturnType(Class<? extends Field.Value> fieldValueClass) {
            this.fieldValueClass = fieldValueClass;
        }

        public static ReturnType getReturnType(String return_type) {
            try {
                return ReturnType.valueOf(return_type);
            } catch (NullPointerException e) {
                return ReturnType.undefined;
            } catch (IllegalArgumentException e) {
                return ReturnType.undefined;
            }
        }

        public Class<? extends Field.Value> getFieldValueClass() {
            return fieldValueClass;
        }
    }

    /**
     * This class describes the particular settings of a calculation field configuration.
     *
     */
    private static class Settings {
        private final Integer decimals = null;
        private final ReturnType return_type = null;
        private final String script = null;
        private final String time = null;
        private final Boolean calendar = null;
        private final String unit = null;

        // FIXME:
        // Decide on the extent of script field support. Should Android users be allowed to modify
        // the script e.g.?
        // public final Object expression = null;
    }

    /**
     * This class describes the specific configuration of a calculation field.
     *
     */
    public static class Configuration extends Field.Configuration {
        private final Value default_value = null;
        private final Settings settings = null;

        public Value getDefaultValue() {
            return default_value;
        }

        /**
         * Returns the number of decimals to show for this field.
         *
         * @return A count value.
         */
        public int getNumberOfDecimals() {
            return settings != null ? Utils.getNative(settings.decimals, 0) : 0;
        }

        /**
         * Returns the type of output from this script.
         *
         * @return A status string.
         */
        public ReturnType getReturnType() {
            return settings != null ? settings.return_type : null;
        }

        /**
         * Returns the actual script for this field.
         *
         * @return A script string.
         */
        public String getScript() {
            return settings != null ? settings.script : null;
        }

        /**
         * @return A status string.
         */
        public String getTimeState() {
            return settings != null ? settings.time : null;
        }

        /**
         * Returns the user-facing unit of the calculated value.
         *
         * @return A unit name.
         */
        public String getUnit() {
            return settings != null ? settings.unit : null;
        }

        public boolean isCalendar() {
            return settings != null ? settings.calendar : false;
        }
    }

    // Private fields.
    private final Configuration config = null;
    private final ArrayList<Value> values;

    public CalculationField(String externalId) {
        super(externalId);
        this.values =  new ArrayList<Value>();
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
