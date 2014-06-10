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
public final class MoneyValue extends AbstractValue {

    public static final class Data {
        private final String currency;
        private final String value;

        public Data(String currency, String value) {
            this.currency = currency;
            this.value = value;
        }

        public String getCurrency() {
            return currency;
        }

        public String getValue() {
            return value;
        }
    }

    private final MoneyValue.Data value;

    public MoneyValue(MoneyValue.Data value) {
        this.value = value;
    }

    public MoneyValue(String currency, String value) {
        this.value = new MoneyValue.Data(currency, value);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MoneyValue) {
            MoneyValue other = (MoneyValue) o;

            if (other.value != null && other.value.currency != null && other.value.value != null && this.value != null) {
                return other.value.currency.equals(this.value.currency) && other.value.value.equals(this.value.value);
            }
        }

        return false;
    }

    @Override
    public Object getPushData() {
        HashMap<String, String> data = null;

        if (value != null && value.currency != null && value.value != null) {
            data = new HashMap<String, String>();
            data.put("currency", value.currency);
            data.put("value", value.value);
        }

        return data;
    }

    @Override
    public int hashCode() {
        String c = this.value.currency;
        String v = this.value.value;
        return c != null && v != null ? (c + v).hashCode() : 0;
    }

    public String getCurrency() {
        return value != null ? value.getCurrency() : null;
    }

    public String getValue() {
        return value != null ? value.getValue() : null;
    }

}
