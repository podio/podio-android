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

package com.podio.sdk.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.podio.sdk.domain.field.Field;
import com.podio.sdk.domain.field.FieldTypeMismatchException;
import com.podio.sdk.domain.field.Pushable;
import com.podio.sdk.domain.helper.UserInfo;
import com.podio.sdk.internal.utils.Utils;

public final class Item implements Pushable {

    public static final class PushData {
        public final String external_id;
        public final Map<String, Object> fields;

        private PushData(String externalId) {
            this.external_id = externalId;
            this.fields = new HashMap<String, Object>();
        }

        private void addData(int fieldId, Object value) {
            if (fieldId > 0 && value != null) {
                String idString = Integer.toString(fieldId);
                fields.put(idString, value);
            }
        }
    }

    public static final class PushResult {
        public final Integer revision = null;
        public final Long item_id = null;
        public final String title = null;
    }

    public static final class Excerpt {
        public final String label = null;
        public final String text = null;
    }

    public final Application app = null;
    public final UserInfo created_by = null;
    public final String created_on = null;
    public final Excerpt excerpt = null;
    public final String[] grant = null;
    public final Integer grant_count = null;
    public final Long item_id = null;
    public final String link = null;
    public final Boolean pinned = null;
    public final Integer priority = null;
    public final Integer revision = null;
    public final String[] rights = null;
    public final Space space = null;
    public final Boolean subscribed = null;
    public final Integer subscribed_count = null;
    public final String[] tags = null;
    public final String title = null;

    public final String external_id;
    public final List<Field> fields;

    public Item() {
    	this(null);
    }

    public Item(String externalId) {
        this.external_id = externalId;
        this.fields = new ArrayList<Field>();
    }

    /**
     * Creates a new, empty {@link Item} with the fields from the given
     * application template.
     * 
     * @param application
     *        The application to use as a template.
     * @return A new, empty item.
     */
    public static Item newInstance(Application application) {
    	if (application == null) {
    		throw new NullPointerException("application cannot be null");
    	}
    	
        Item item = new Item(null);
        item.fields.addAll(application.fields);
        return item;
    }

    @Override
    public PushData getPushData() {
        PushData pushData = new PushData(external_id);

        for (Field field : fields) {
            Object data = field.getPushData();
            pushData.addData(field.field_id, data);
        }

        return pushData;
    }

    /**
     * Tries to set the given value to the field with the given name on this
     * item.
     * 
     * @param field
     *        The external id of the field.
     * @param value
     *        The field type specific domain object describing the new value.
     * @return Boolean true. Always.
     * @throws FieldTypeMismatchException
     *         If the passed value doesn't match the field with the given name.
     */
    public boolean setValue(String field, Object value) throws FieldTypeMismatchException {
        Field f = findField(field);

        if (f != null) {
            f.set(value);
        }

        return true;
    }

    /**
     * Tries to clear the given value from the field with the given name on this
     * item.
     * 
     * @param field
     *        The external id of the field.
     * @param value
     *        The field type specific domain object describing the new value.
     * @return Boolean true. Always.
     * @throws FieldTypeMismatchException
     *         If the passed value doesn't match the field with the given name.
     */
    public boolean clearValue(String field, Object value) throws FieldTypeMismatchException {
        Field f = findField(field);

        if (f != null) {
            f.clear(value);
        }

        return true;
    }

    /**
     * Looks for the field with with given external id in the list of fields.
     * 
     * @param externalId
     *        The external id of the field to find.
     * @return The field domain object if found, or null.
     */
    private Field findField(String externalId) {
    	if (fields == null) {
    		throw new IllegalStateException("fields has not been loaded");
    	}
    	if (Utils.isEmpty(externalId)) {
    		throw new IllegalArgumentException("externalId cannot be empty");
    	}

        for (Field field : fields) {
            if (field.external_id.equals(externalId)) {
                return field;
            }
        }
        
        return null;
    }
}
