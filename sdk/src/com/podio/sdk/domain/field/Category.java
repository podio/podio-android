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
import java.util.HashMap;
import java.util.List;

public final class Category extends Field {

    /**
     * A category option.
     * 
     * @author László Urszuly
     */
    public static final class Option implements Pushable {
        public final String status = null;
        public final String text = null;
        public final String color = null;

        public final Integer id;

        public Option(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            return o != null && o instanceof Option && id == ((Option) o).id;
        }

        @Override
        public Object getPushData() {
            HashMap<String, Integer> pushData = new HashMap<String, Integer>();
            pushData.put("value", id);
            return pushData;
        }

        @Override
        public int hashCode() {
            return id;
        }
    }

    /**
     * The category field settings.
     * 
     * @author László Urszuly
     */
    public static final class Settings {
        public final String display = null;
        public final Boolean multiple = null;
        public final List<Option> options = null;
    }

    /**
     * The category field configuration. This object holds the field settings.
     * 
     * @author László Urszuly
     */
    public static final class Config {
        public final Settings settings = null;
    }

    /**
     * A picked category option.
     * 
     * @author László Urszuly
     */
    public static final class Value {
        public final Option value;

        public Value(Option option) {
            this.value = option;
        }

        @Override
        public boolean equals(Object o) {
            return o != null && o instanceof Value && ((Value) o).value.equals(value);
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }

    public final Config config = null;

    // TODO: This isn't good. We shouldn't make a member field publicly
    // modifiable like this. Have a second look at it.
    public final List<Value> values;

    public Category(String externalId) {
        super(externalId);
        this.values = new ArrayList<Value>();
    }

    @Override
    public boolean clear(Object value) throws FieldTypeMismatchException {
        boolean isSuccess = false;
        Option option = tryCast(value);
        Value v = new Value(option);

        if (values != null) {
            values.remove(v);
            isSuccess = true;
        }

        return isSuccess;
    }

    @Override
    public Object getPushData() {
        ArrayList<Object> pushData = new ArrayList<Object>();

        if (values != null) {
            for (Value value : values) {
                Option option = value.value;

                if (option != null) {
                    pushData.add(option.getPushData());
                }
            }
        }

        return pushData;
    }

    @Override
    public boolean set(Object value) throws FieldTypeMismatchException {
        boolean isSuccess = false;
        Option option = tryCast(value);

        clear(option);

        if (values != null) {
            isSuccess = values.add(new Value(option));
        }

        return isSuccess;
    }

    private Option tryCast(Object value) throws FieldTypeMismatchException {
        if (value instanceof Option) {
            return (Option) value;
        } else {
            throw new FieldTypeMismatchException();
        }
    }
}
