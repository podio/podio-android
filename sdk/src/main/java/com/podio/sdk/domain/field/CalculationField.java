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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.podio.sdk.internal.Utils;

/**
 * @author László Urszuly
 */
public class CalculationField extends Field<CalculationField.Value> {
    /**
     * This class describes the particular settings of a calculation field configuration.
     *
     * @author László Urszuly
     */
    private static class Settings {
        private final Integer decimals = null;
        private final String return_type = null;
        private final String script = null;
        private final String time = null;
        private final String unit = null;

        // FIXME:
        // Decide on the extent of script field support. Should Android users be allowed to modify
        // the script e.g.?
        // public final Object expression = null;
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
        public String getReturnType() {
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
    }

    /**
     * This class describes a calculation field value.
     *
     * @author László Urszuly
     */
    public static class Value extends Field.Value {
        private final String value;

        private Value(String value) {
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
            return null;
        }

        @Override
        public int hashCode() {
            return this.value != null ? this.value.hashCode() : 0;
        }

    }

    // Private fields.
    private final Configuration config = null;
    private final ArrayList<Value> values;

    public CalculationField(String externalId) {
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
    public int valuesCount() {
        return values != null ? values.size() : 0;
    }

    public Configuration getConfiguration() {
        return config;
    }
}
