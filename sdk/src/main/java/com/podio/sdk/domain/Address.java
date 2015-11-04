
package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

public class Address {

    private final String formatted = null;
    private final String street_number = null;
    private final String street_name = null;
    private final String postal_code = null;
    private final String city = null;
    private final String state = null;
    private final String country = null;
    private final Double lat = null;
    private final Double lng = null;

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getFormatted() {
        return formatted;
    }

    public String getStreetNumber() {
        return street_number;
    }

    public String getStreetName() {
        return street_name;
    }

    public String getPostalCode() {
        return postal_code;
    }

    public String getState() {
        return state;
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
}
