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
import java.util.Date;
import java.util.List;

import com.podio.sdk.domain.field.configuration.DateConfiguration;
import com.podio.sdk.domain.field.value.DateValue;

/**
 * @author László Urszuly
 */
public final class DateField extends Field {

    public static enum State {
        disabled, enabled, undefined
    }

    private final DateConfiguration config = null;
    private final List<DateValue> values;

    public DateField(String externalId) {
        super(externalId);
        this.values = new ArrayList<DateValue>();
    }

    @Override
    public void addValue(Object value) throws FieldTypeMismatchException {
        DateValue v = validateValue(value);

        if (values != null && !values.contains(v)) {
            values.add(v);
        }
    }

    @Override
    protected List<DateValue> getPushables() {
        return values;
    }

    @Override
    public DateValue getValue(int index) {
        return values != null ? values.get(index) : null;
    }

    @Override
    public void removeValue(Object value) throws FieldTypeMismatchException {
        DateValue v = validateValue(value);

        if (values != null && values.contains(v)) {
            values.remove(v);
        }
    }

    /**
     * Returns the configuration metrics for this field.
     * 
     * @return The configuration data structure.
     */
    public DateConfiguration getConfiguration() {
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
    private DateValue validateValue(Object value) throws FieldTypeMismatchException {
        if (value instanceof DateValue) {
            return (DateValue) value;
        } else if (value instanceof Date) {
            return new DateValue((Date) value);
        } else if (value instanceof Date[]) {
            Date[] v = (Date[]) value;

            if (v.length == 2) {
                return new DateValue(v[0], v[1]);
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
