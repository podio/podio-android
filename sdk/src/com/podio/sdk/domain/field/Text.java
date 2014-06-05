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
import java.util.HashMap;
import java.util.List;

/**
 * The Podio Text field domain object.
 * 
 * @author László Urszuly
 */
public final class Text extends Field {

    /**
     * The values for the named sizes a text field can have.
     * 
     * @author László Urszuly
     */
    public static enum Size {
        large, small, undefined
    }

    /**
     * Settings for the text field.
     * 
     * @author László Urszuly
     */
    public static final class Settings {
        public final Size size = null;
    }

    /**
     * Configuration of the text field.
     * 
     * @author László Urszuly
     */
    public static final class Config {
        public final Value default_value = null;
        public final Integer delta = null;
        public final String description = null;
        public final Boolean hidden = null;
        public final String label = null;
        public final Boolean required = null;
        public final Settings settings = null;
        public final Boolean visible = null;
    }

    /**
     * The field value container.
     * 
     * @author László Urszuly
     */
    public static final class Value implements Pushable {
        public String value;

        public Value(String value) {
            this.value = value;
        }

        @Override
        public Object getPushData() {
            HashMap<String, String> pushData = new HashMap<String, String>();
            pushData.put("value", value.toString());
            return pushData;
        }
    }

    public final Config config = null;
    public final List<Value> values;

    public Text(String externalId) {
        super(externalId);
        this.values = new ArrayList<Value>();
    }

    @Override
    public void clear(Object value) throws FieldTypeMismatchException {
        values.clear();
    }

    @Override
    public Object getPushData() {
        ArrayList<Object> pushData = new ArrayList<Object>();

        for (Value value : values) {
            pushData.add(value.getPushData());
        }

        return pushData;
    }

    @Override
    public void set(Object value) throws FieldTypeMismatchException {
        String text = tryCast(value);
        clear(value);
        values.add(new Value(text));
    }

    private String tryCast(Object value) throws FieldTypeMismatchException {
        if (value instanceof String) {
            return (String) value;
        } else {
            throw new FieldTypeMismatchException();
        }
    }
}
