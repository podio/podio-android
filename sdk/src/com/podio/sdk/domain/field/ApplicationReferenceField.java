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

import com.podio.sdk.domain.Application;
import com.podio.sdk.domain.Space;
import com.podio.sdk.domain.helper.UserInfo;

public final class ApplicationReferenceField extends Field {

    /**
     * The value content of this field.
     * 
     * @author László Urszuly
     */
    public static final class Data implements Pushable {
        public final Application app = null;
        public final Integer app_item_id = null;
        public final UserInfo created_by = null;
        public final String created_on = null;
        public final String link = null;
        public final Space space = null;
        public final String title = null;

        public final Integer item_id;

        public Data(Integer itemId) {
            this.item_id = itemId;
        }

        @Override
        public boolean equals(Object o) {
            return o != null && o instanceof Data && item_id == ((Data) o).item_id;
        }

        @Override
        public Object getPushData() {
            HashMap<String, Integer> pushData = new HashMap<String, Integer>();
            pushData.put("value", item_id);
            return pushData;
        }

        @Override
        public int hashCode() {
            return item_id;
        }
    }

    /**
     * The value of this field.
     * 
     * @author László Urszuly
     */
    public static final class Value {
        public final Data value;

        public Value(Data data) {
            this.value = data;
        }

        public Value(Integer itemId) {
            this.value = new Data(itemId);
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

    public final List<Value> values;

    public ApplicationReferenceField(String externalId) {
        super(externalId);
        this.values = new ArrayList<Value>();
    }

    @Override
    public void clear(Object value) throws FieldTypeMismatchException {
        Data data = tryCast(value);
        Value v = new Value(data);
        values.remove(v);
    }

    @Override
    public Object getPushData() {
        ArrayList<Object> pushData = new ArrayList<Object>();

        for (Value value : values) {
            Data data = value.value;

            if (data != null) {
                pushData.add(data.getPushData());
            }
        }
        
        return pushData;
    }

    @Override
    public void set(Object value) throws FieldTypeMismatchException {
        Data data = tryCast(value);
        clear(data);
        values.add(new Value(data));
    }

    private Data tryCast(Object value) {
        if (value instanceof Data) {
            return (Data) value;
        } else {
            throw new FieldTypeMismatchException();
        }
    }
}
