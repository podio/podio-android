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

    public double getLat() {
        return Utils.getNative(lat, 0.0D);
    }

    public double getLng() {
        return Utils.getNative(lng, 0.0D);
    }
}
