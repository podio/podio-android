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

import com.podio.sdk.domain.field.value.CategoryValue;

/**
 * @author László Urszuly
 */
public final class CategoryConfiguration extends AbstractConfiguration {

    public static final class CategorySettings {
        private final String display = null;
        private final Boolean multiple = null;
        private final List<CategoryValue.Data> options = null;

        /**
         * Returns the display state of this field.
         * 
         * @return A status string.
         */
        public String getDisplay() {
            return display;
        }

        /**
         * Returns a list of the available options for this category field.
         * 
         * @return A list of options data structures.
         */
        public List<CategoryValue.Data> getOptions() {
            return new ArrayList<CategoryValue.Data>(options);
        }

        /**
         * Returns the values behavior for this field.
         * 
         * @return Boolean true if this field allows multiple options being
         *         selected or boolean false if only one option can be selected.
         */
        public boolean isMultiSelect() {
            return multiple != null ? multiple.booleanValue() : false;
        }

    }

    private final CategoryValue default_value = null;
    private final CategorySettings settings = null;

    public CategoryValue getDefaultValue() {
        return default_value;
    }

    public CategorySettings getSettings() {
        return settings;
    }

}
