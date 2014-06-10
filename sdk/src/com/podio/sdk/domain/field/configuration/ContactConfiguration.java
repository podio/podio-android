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

import com.podio.sdk.domain.field.ContactField.Type;
import com.podio.sdk.domain.field.value.ContactValue;

/**
 * @author László Urszuly
 */
public final class ContactConfiguration extends AbstractConfiguration {

    public static final class ContactSettings {
        private final String type = null;
        private final List<Type> valid_types = null;

        /**
         * @return A type string.
         */
        public String getType() {
            return type;
        }

        /**
         * Returns a list of valid contact types.
         * 
         * @return A list of valid type enumeration values.
         */
        public List<Type> getValidTypes() {
            return new ArrayList<Type>(valid_types);
        }
    }

    private final ContactValue default_value = null;
    private final ContactSettings settings = null;

    public ContactValue getDefaultValue() {
        return default_value;
    }

    public ContactSettings getSettings() {
        return settings;
    }

}
