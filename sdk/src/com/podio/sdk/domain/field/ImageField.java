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

import com.podio.sdk.domain.File;
import com.podio.sdk.domain.field.configuration.ImageConfiguration;
import com.podio.sdk.domain.field.value.ImageValue;

/**
 * @author László Urszuly
 */
public final class ImageField extends Field {
    private final ImageConfiguration config = null;
    private final List<ImageValue> values;

    public ImageField(String externalId) {
        super(externalId);
        this.values = new ArrayList<ImageValue>();
    }

    @Override
    public void addValue(Object value) throws FieldTypeMismatchException {
        ImageValue v = validateValue(value);

        if (values != null && !values.contains(v)) {
            values.add(v);
        }
    }

    @Override
    protected List<ImageValue> getPushables() {
        return values;
    }

    @Override
    public ImageValue getValue(int index) {
        return values != null ? values.get(index) : null;
    }

    @Override
    public void removeValue(Object value) throws FieldTypeMismatchException {
        ImageValue v = validateValue(value);

        if (values != null && values.contains(v)) {
            values.remove(v);
        }
    }

    /**
     * Returns the configuration metrics for this field.
     * 
     * @return The configuration data structure.
     */
    public ImageConfiguration getConfiguration() {
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
    private ImageValue validateValue(Object value) throws FieldTypeMismatchException {
        if (value instanceof ImageValue) {
            return (ImageValue) value;
        } else if (value instanceof File) {
            return new ImageValue((File) value);
        } else if (value instanceof Integer) {
            return new ImageValue(((Integer) value).longValue());
        } else if (value instanceof Long) {
            return new ImageValue(((Long) value).longValue());
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
