
package com.podio.sdk.domain.field;

import com.podio.sdk.domain.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class ImageField extends Field<ImageField.Value> {
    /**
     * This class describes the particular settings of an Image field configuration.
     *
     */
    private static class Settings {
        private final String[] allowed_mimetypes = null;
    }

    /**
     * This class describes the specific configuration of an Image field.
     *
     */
    public static class Configuration extends Field.Configuration {
        private final Value default_value = null;
        private final Settings settings = null;

        public Value getDefaultValue() {
            return default_value;
        }

        public List<String> getAllowedMimeTypes() {
            return settings != null ? Arrays.asList(settings.allowed_mimetypes) : Arrays.asList(new String[0]);
        }
    }

    /**
     * This class describes an Image field value.
     *
     */
    public static class Value extends Field.Value {
        private final File value;

        public Value(File file) {
            this.value = file;
        }

        public Value(long fileId) {
            this.value = new File(fileId);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Value) {
                Value other = (Value) o;

                if (other.value != null && this.value != null) {
                    return other.value.getId() == this.value.getId();
                }
            }

            return false;
        }

        @Override
        public Map<String, Object> getCreateData() {
            HashMap<String, Object> data = null;

            if (value != null) {
                long fileId = value.getId();

                if (fileId > 0L) {
                    data = new HashMap<String, Object>();
                    data.put("value", fileId);
                }
            }

            return data;
        }

        public File getFile(){
            return value;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }

    }

    // Private fields.
    private final Configuration config = null;
    private final ArrayList<Value> values;

    public ImageField(String externalId) {
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
