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

import com.podio.sdk.internal.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author László Urszuly
 */
public class LocationField extends Field<LocationField.Value> {
    /**
     * This class describes the particular settings of a Map field configuration.
     *
     * @author László Urszuly
     */
    private static class Settings {
        private final Boolean structured = null;
        private final Boolean has_map = null;

        public boolean isStructured() {
            return Utils.getNative(structured, false);
        }

        public boolean getHasMap() {
            return Utils.getNative(has_map, false);
        }
    }

    /**
     * This class describes the specific configuration of a Map field.
     *
     * @author László Urszuly
     */
    public static class Configuration extends Field.Configuration {
        private final Value default_value = null;
        private final Settings settings = null;

        public Value getDefaultValue() {
            return default_value;
        }
    }

    /**
     * This class describes a Map field value.
     *
     * @author László Urszuly
     */
    public static class Value extends Field.Value {
        private final String formatted;
        private final String street_number;
        private final String street_name;
        private final String postal_code;
        private final String city;
        private final String state;
        private final String country;
        private final Double lat;
        private final Double lng;
        private final String value;
        private final Boolean map_in_sync;

        public Value(String value) {
            this.value = value;
            this.formatted = null;
            this.street_number = null;
            this.street_name = null;
            this.postal_code = null;
            this.city = null;
            this.state = null;
            this.country = null;
            this.lat = null;
            this.lng = null;
            this.map_in_sync = null;
        }

        public Value(String formatted, String street_number, String street_name, String postal_code, String city, String state, String country, Double lat, Double lng){
            this.formatted = formatted;
            this.street_number = street_number;
            this.street_name = street_name;
            this.postal_code = postal_code;
            this.city = city;
            this.state = state;
            this.country = country;
            this.lat = lat;
            this.lng = lng;
            this.value = null;
            this.map_in_sync = null;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Value) {
                Value other = (Value) o;

                if (other.value != null && this.value != null) {
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
            return this.value != null ? this.value.hashCode() : 0;
        }

        /**
         *
         * @return The latitude or Double.MIN_VALUE if the value is not set.
         */
        public double getLat() {
            return Utils.getNative(lat, Double.MIN_VALUE);
        }

        /**
         *
         * @return The longitude or Double.MIN_VALUE if the value is not set.
         */
        public double getLng() {
            return Utils.getNative(lng, Double.MIN_VALUE);
        }

        public String getCity() {
            return city;
        }

        public String getCountry() {
            return country;
        }

        public String getFormatted() {
            return formatted;
        }

        public String getPostalCode() {
            return postal_code;
        }

        public String getState() {
            return state;
        }

        public String getStreetName() {
            return street_name;
        }

        public String getStreetNumber() {
            return street_number;
        }

        public String getValue() {
            return value;
        }

        public boolean isMapInSync(){
            return Utils.getNative(map_in_sync, false);
        }
    }

    // Private fields.
    private final Configuration config = null;
    private final ArrayList<Value> values;

    public LocationField(String externalId) {
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
