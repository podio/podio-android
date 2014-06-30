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

import com.podio.sdk.domain.field.configuration.DurationConfiguration;
import com.podio.sdk.domain.field.value.DurationValue;

/**
 * @author László Urszuly
 */
public final class DurationField extends Field {

    public static enum FieldType {
        hours, minutes, seconds, undefined
    }

    private final DurationConfiguration config = null;
    private final List<DurationValue> values;

    public DurationField(String externalId) {
        super(externalId);
        this.values = new ArrayList<DurationValue>();
    }

    @Override
    public void addValue(Object value) throws FieldTypeMismatchException {
        DurationValue v = validateValue(value);

        if (values != null && !values.contains(v)) {
            values.add(v);
        }
    }

    @Override
    protected List<DurationValue> getPushables() {
        return values;
    }

    @Override
    public DurationValue getValue(int index) {
        return values != null ? values.get(index) : null;
    }

    @Override
    public void removeValue(Object value) throws FieldTypeMismatchException {
        DurationValue v = validateValue(value);

        if (values != null && values.contains(v)) {
            values.remove(v);
        }
    }

    /**
     * Returns the configuration metrics for this field.
     * 
     * @return The configuration data structure.
     */
    public DurationConfiguration getConfiguration() {
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
    private DurationValue validateValue(Object value) throws FieldTypeMismatchException {
        if (value instanceof DurationValue) {
            return (DurationValue) value;
        } else if (value instanceof Integer) {
            return new DurationValue(((Integer) value).intValue());
        } else {
            throw new FieldTypeMismatchException();
        }
    }

    /**
     * Returns the number of values for this field.
     * 
     * @return The size of the values list.
     */
    public int valuesCount() {
        return values != null ? values.size() : 0;
    }

}
