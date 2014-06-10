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

package com.podio.sdk.domain.field.value;

import java.util.HashMap;

/**
 * @author László Urszuly
 */
public final class MapValue extends AbstractValue {

    public static final class Data {
        private final Double lat = null;
        private final Double lng = null;
        private final String city = null;
        private final String country = null;
        private final String formatted = null;
        private final String postal_code = null;
        private final String state = null;
        private final String street_name = null;
        private final String street_number = null;
        private final String value;

        public Data(String value) {
            this.value = value;
        }

        public double getLatitude() {
            return lat != null ? lat.doubleValue() : 0D;
        }

        public double getLongitude() {
            return lng != null ? lng.doubleValue() : 0D;
        }

        public String getCity() {
            return city;
        }

        public String getCountry() {
            return country;
        }

        public String getFormattedLocation() {
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
    }

    private final Data value;

    public MapValue(Data value) {
        this.value = value;
    }

    public MapValue(String value) {
        this.value = new Data(value);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MapValue) {
            MapValue other = (MapValue) o;

            if (other.value != null && other.value.value != null && this.value != null) {
                return other.value.equals(this.value.value);
            }
        }

        return false;
    }

    @Override
    public Object getPushData() {
        HashMap<String, String> data = null;

        if (value != null && value.value != null) {
            data = new HashMap<String, String>();
            data.put("value", value.value);
        }

        return data;
    }

    @Override
    public int hashCode() {
        return this.value != null && this.value.value != null ? this.value.value.hashCode() : 0;
    }

    public double getLatitude() {
        return value != null ? value.getLatitude() : 0D;
    }

    public double getLongitude() {
        return value != null ? value.getLongitude() : 0D;
    }

    public String getCity() {
        return value != null ? value.getCity() : null;
    }

    public String getCountry() {
        return value != null ? value.getCountry() : null;
    }

    public String getFormattedLocation() {
        return value != null ? value.getFormattedLocation() : null;
    }

    public String getPostalCode() {
        return value != null ? value.getPostalCode() : null;
    }

    public String getState() {
        return value != null ? value.getState() : null;
    }

    public String getStreetName() {
        return value != null ? value.getStreetName() : null;
    }

    public String getStreetNumber() {
        return value != null ? value.getStreetNumber() : null;
    }

    public String getValue() {
        return value != null ? value.getValue() : null;
    }

}
