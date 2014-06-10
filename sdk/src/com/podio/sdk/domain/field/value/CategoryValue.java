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
public class CategoryValue extends AbstractValue {

    /**
     * A category option.
     * 
     * @author László Urszuly
     */
    public static final class Data {
        private final String status = null;
        private final String text = null;
        private final String color = null;
        private final Integer id;

        private Data(int id) {
            this.id = id;
        }

        /**
         * Returns the status of this category option.
         * 
         * @return The status string.
         */
        public String getStatus() {
            return status;
        }

        /**
         * Returns the name of this category option.
         * 
         * @return The user-facing label.
         */
        public String getText() {
            return text;
        }

        /**
         * Returns the color of this category option.
         * 
         * @return The HTML RGB notation color (excluding the '#').
         */
        public String getColor() {
            return color;
        }

        /**
         * Returns the id of this category option.
         * 
         * @return The numeric id.
         */
        public int getId() {
            return id != null ? id.intValue() : 0;
        }
    }

    private final Data value;

    public CategoryValue(Data option) {
        this.value = option;
    }

    public CategoryValue(int optionId) {
        this.value = new Data(optionId);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CategoryValue) {
            CategoryValue other = (CategoryValue) o;

            if (other.value != null && other.value.id != null && this.value != null && this.value.id != null) {
                return other.value.id.intValue() == this.value.id.intValue();
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return value != null ? value.getId() : 0;
    }

    @Override
    public Object getPushData() {
        HashMap<String, Integer> data = null;
        int id = getId();

        if (id > 0) {
            data = new HashMap<String, Integer>();
            data.put("value", id);
        }

        return data;
    }

    /**
     * {@inheritDoc Data#getColor()}
     */
    public String getColor() {
        return value != null ? value.getColor() : null;
    }

    public CategoryValue.Data getData() {
        return value;
    }

    /**
     * {@inheritDoc Data#getId()}
     */
    public int getId() {
        return value != null ? value.getId() : 0;
    }

    /**
     * {@inheritDoc Data#getStatus()}
     */
    public String getStatus() {
        return value != null ? value.getStatus() : null;
    }

    /**
     * {@inheritDoc Data#getText()}
     */
    public String getText() {
        return value != null ? value.getText() : null;
    }

}
