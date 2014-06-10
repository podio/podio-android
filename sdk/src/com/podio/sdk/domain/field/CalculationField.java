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

import com.podio.sdk.domain.field.configuration.CalculationConfiguration;
import com.podio.sdk.domain.field.value.CalculationValue;

/**
 * @author László Urszuly
 */
public final class CalculationField extends Field {
    private final CalculationConfiguration config = null;
    private final List<CalculationValue> values;

    public CalculationField(String externalId) {
        super(externalId);
        this.values = new ArrayList<CalculationValue>();
    }

    @Override
    protected List<CalculationValue> getPushables() {
        return null;
    }

    /**
     * Returns the configuration metrics for this field.
     * 
     * @return The configuration data structure.
     */
    public CalculationConfiguration getConfiguration() {
        return config;
    }

    /**
     * Returns the value at the given position for this field.
     * 
     * @return A value object specific for this field type.
     */
    public CalculationValue getValue(int index) {
        return values != null ? values.get(index) : null;
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
