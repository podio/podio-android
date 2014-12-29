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

package com.podio.sdk.domain.field.configuration;

import java.util.ArrayList;
import java.util.List;

import com.podio.sdk.domain.field.DurationField.FieldType;
import com.podio.sdk.domain.field.value.DurationValue;

/**
 * @author László Urszuly
 */
public final class DurationConfiguration extends AbstractConfiguration {

    public static final class DurationSettings {
        private final List<FieldType> fields = null;

        public List<FieldType> getFieldTypes() {
            return new ArrayList<FieldType>(fields);
        }

    }

    private final DurationValue default_value = null;
    private final DurationSettings settings = null;

    public DurationValue getDefaultValue() {
        return default_value;
    }

    public DurationSettings getSettings() {
        return settings;
    }

}
