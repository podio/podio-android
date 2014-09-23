//@formatter:off

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

//@formatter:on

package com.podio.sdk.domain.field;

import java.util.ArrayList;
import java.util.List;

import com.podio.sdk.domain.field.configuration.CategoryConfiguration;
import com.podio.sdk.domain.field.value.CategoryValue;

public final class CategoryField extends Field {
    private final CategoryConfiguration config = null;
    private final List<CategoryValue> values;

    public CategoryField(String externalId) {
        super(externalId);
        this.values = new ArrayList<CategoryValue>();
    }

    @Override
    public void addValue(Object value) throws FieldTypeMismatchException {
        CategoryValue v = validateValue(value);

        if (values != null && !values.contains(v)) {
            values.add(v);
        }
    }

    @Override
    protected List<CategoryValue> getPushables() {
        return values;
    }

    @Override
    public CategoryValue getValue(int index) {
        return values != null ? values.get(index) : null;
    }

    /**
     * Tries to locally clear the given value from the current field. The
     * changes are NOT updated on the servers by this method call.
     * 
     * @param value
     *        The value domain object to clear.
     * @return Boolean true if the value could be cleared, boolean false
     *         otherwise.
     * @throws FieldTypeMismatchException
     *         If the value can't be applied to this field type.
     */
    @Override
    public void removeValue(Object value) throws FieldTypeMismatchException {
        CategoryValue v = validateValue(value);

        if (values != null && values.contains(v)) {
            values.remove(v);
        }
    }

    /**
     * Returns the configuration metrics for this field.
     * 
     * @return The configuration data structure.
     */
    public CategoryConfiguration getConfiguration() {
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
    private CategoryValue validateValue(Object value) throws FieldTypeMismatchException {
        if (value instanceof CategoryValue) {
            return (CategoryValue) value;
        } else if (value instanceof CategoryValue.Data) {
            return new CategoryValue((CategoryValue.Data) value);
        } else if (value instanceof Integer) {
            return new CategoryValue(((Integer) value).longValue());
        } else if (value instanceof Long) {
            return new CategoryValue(((Long) value).longValue());
        } else {
            throw new FieldTypeMismatchException();
        }
    }

    @Override
    public int valuesCount() {
        return values != null ? values.size() : 0;
    }

}
