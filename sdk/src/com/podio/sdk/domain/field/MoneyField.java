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

import com.podio.sdk.domain.field.configuration.MoneyConfiguration;
import com.podio.sdk.domain.field.value.MoneyValue;

/**
 * @author László Urszuly
 */
public final class MoneyField extends Field {
    private final MoneyConfiguration config = null;
    private final List<MoneyValue> values;

    public MoneyField(String externalId) {
        super(externalId);
        this.values = new ArrayList<MoneyValue>();
    }

    @Override
    public void addValue(Object value) throws FieldTypeMismatchException {
        MoneyValue v = validateValue(value);

        if (values != null && !values.contains(v)) {
            values.add(v);
        }
    }

    @Override
    protected List<MoneyValue> getPushables() {
        return values;
    }

    @Override
    public MoneyValue getValue(int index) {
        return values != null ? values.get(index) : null;
    }

    @Override
    public void removeValue(Object value) throws FieldTypeMismatchException {
        MoneyValue v = validateValue(value);

        if (values != null && values.contains(v)) {
            values.remove(v);
        }
    }

    /**
     * Returns the configuration metrics for this field.
     * 
     * @return The configuration data structure.
     */
    public MoneyConfiguration getConfiguration() {
        return config;
    }

    /**
     * Determines whether the given object can be used as value for this field
     * or not.
     * 
     * @param value
     *        The object to use as value.
     * @return A type specific value representation of the given object.
     * @throws FieldTypeMismatchException
     *         If the given object can't be used as value for this field.
     */
    private MoneyValue validateValue(Object value) throws FieldTypeMismatchException {
        if (value instanceof MoneyValue) {
            return (MoneyValue) value;
        } else if (value instanceof String[]) {
            String[] v = (String[]) value;

            if (v.length == 2) {
                return new MoneyValue(v[0], v[1]);
            } else {
                throw new FieldTypeMismatchException();
            }
        } else {
            throw new FieldTypeMismatchException();
        }
    }

    @Override
    public int valuesCount() {
        return values != null ? values.size() : 0;
    }

}
