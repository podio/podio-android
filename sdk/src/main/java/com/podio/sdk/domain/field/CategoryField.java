
package com.podio.sdk.domain.field;

import com.podio.sdk.internal.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author László Urszuly
 */
public class CategoryField extends Field<CategoryField.Value> {
    /**
     * This class describes the particular settings of a Category field configuration.
     *
     * @author László Urszuly
     */
    private static class Settings {
        private final String display = null;
        private final Boolean multiple = null;
        private final Data[] options = null;
    }

    /**
     * This class describes the specific configuration of a Category field.
     *
     * @author László Urszuly
     */
    public static class Configuration extends Field.Configuration {
        private final Value default_value = null;
        private final Settings settings = null;

        public Value getDefaultValue() {
            return default_value;
        }

        /**
         * Returns the display state of this field.
         * @return
         */
        public Display getDisplay() {
            try {
                return Display.valueOf(settings.display);
            } catch (NullPointerException e) {
                return Display.undefined;
            } catch (IllegalArgumentException e) {
                return Display.undefined;
            }
        }

        /**
         * Returns a list of the available options for this category field.
         *
         * @return A list of options data structures.
         */
        public List<Data> getOptions() {
            return settings != null && settings.options != null ? Arrays.asList(settings.options) : Arrays.asList(new Data[0]);
        }

        /**
         * Returns the values behavior for this field.
         *
         * @return Boolean true if this field allows multiple options being selected or boolean
         * false if only one option can be selected.
         */
        public boolean isMultiSelect() {
            return settings != null && Utils.getNative(settings.multiple, false);
        }
    }

    public static enum Display {
        inline, list, dropdown, undefined
    }

    /**
     * This class describes a Category option.
     *
     * @author László Urszuly
     */
    public static class Data {
        private final String status = null;
        private final String text = null;
        private final String color = null;
        private final Long id;

        private Data(long id) {
            this.id = id;
        }

        /**
         * Returns the status of this category option.
         *
         * @return An enumeration value, describing the category status.
         */
        public Status getStatus() {
            try {
                return Status.valueOf(status);
            } catch (NullPointerException e) {
                return Status.undefined;
            } catch (IllegalArgumentException e) {
                return Status.undefined;
            }
        }
        /**
         * Returns the name of this category option.
         *
         * @return The user-facing label.
         */
        public String getText() {
            return text;
        }

        /**
         * Returns the color of this category option.
         *
         * @return The HTML RGB notation color (excluding the '#').
         */
        public String getColor() {
            return color;
        }

        /**
         * Returns the id of this category option.
         *
         * @return The numeric id.
         */
        public long getId() {
            return Utils.getNative(id, -1L);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Data data = (Data) o;

            return id.equals(data.id);

        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }

    /**
     * This class describes a Category field value.
     *
     * @author László Urszuly
     */
    public static class Value extends Field.Value {
        private final Data value;

        public Value(Data option) {
            this.value = option;
        }

        public Value(long optionId) {
            this.value = new Data(optionId);
        }

        @Override
        public Map<String, Object> getCreateData() {
            HashMap<String, Object> data = null;
            long id = getId();

            if (id > 0L) {
                data = new HashMap<String, Object>();
                data.put("value", id);
            }

            return data;
        }

        /**
         * {@inheritDoc Data#getColor()}
         */
        public String getColor() {
            return value != null ? value.getColor() : null;
        }

        public Data getData() {
            return value;
        }

        /**
         * {@inheritDoc Data#getId()}
         */
        public long getId() {
            return value != null ? value.getId() : -1L;
        }

        /**
         * {@inheritDoc Data#getStatus()}
         */
        public Status getStatus() {
            return value != null ? value.getStatus() : null;
        }

        /**
         * {@inheritDoc Data#getText()}
         */
        public String getText() {
            return value != null ? value.getText() : null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Value value1 = (Value) o;

            return value.equals(value1.value);

        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }
    }

    // Private fields.
    private final Configuration config = null;
    private final ArrayList<Value> values;

    public CategoryField(String externalId) {
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
