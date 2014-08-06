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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.podio.sdk.domain.field.Field;
import com.podio.sdk.domain.field.FieldTypeMismatchException;
import com.podio.sdk.domain.field.Pushable;
import com.podio.sdk.internal.Utils;

public class Item implements Pushable {

    public static class FilterData {
        public static final transient int DEFAULT_LIMIT = 20;
        public static final transient int DEFAULT_OFFSET = 0;

        private boolean remember;
        private boolean sort_desc;
        private int limit;
        private int offset;
        private Map<String, Object> filters;
        private String sort_by;

        public FilterData() {
            this.filters = new HashMap<String, Object>();
            this.sort_desc = true;
            this.limit = DEFAULT_LIMIT;
            this.offset = DEFAULT_OFFSET;
            this.remember = true;
        }

        public void addConstraint(String key, Object value) {
            if (Utils.notEmpty(key) && value != null) {
                filters.put(key, value);
            }
        }

        public Object getConstraint(String key) {
            return filters.get(key);
        }

        public boolean getDoRemember() {
            return Utils.getNative(remember, false);
        }

        public boolean getDoSortDescending() {
            return Utils.getNative(sort_desc, false);
        }

        public int getLimit() {
            return Utils.getNative(limit, 0);
        }

        public int getOffset() {
            return Utils.getNative(offset, 0);
        }

        public String getSortKey() {
            return sort_by;
        }

        public Object removeConstraint(String key) {
            return filters.remove(key);
        }

        public boolean hasConstraint(String key) {
            return filters.containsKey(key);
        }

        public FilterData setDoRemember(boolean doRemember) {
            this.remember = doRemember;
            return this;
        }

        public FilterData setLimit(int limit) {
            this.limit = limit;
            return this;
        }

        public FilterData setOffset(int offset) {
            this.offset = offset;
            return this;
        }

        public FilterData setOrderByField(String fieldName, boolean doSortDescending) {
            this.sort_by = fieldName;
            this.sort_desc = doSortDescending;
            return this;
        }
    }

    public static class FilterResult {
        private final Integer total = null;
        private final Integer filtered = null;
        private final List<Item> items = null;

        public int getTotalCount() {
            return Utils.getNative(total, 0);
        }

        public int getFilteredCount() {
            return Utils.getNative(filtered, 0);
        }

        public List<Item> getItems() {
            return items != null ?
                    new ArrayList<Item>(items) :
                    new ArrayList<Item>();
        }
    }

    public static class PushData {
        @SuppressWarnings("unused")
        private String external_id;
        private Map<String, Object> fields;

        private PushData(String externalId) {
            this.external_id = externalId;
            this.fields = new HashMap<String, Object>();
        }

        private void setValues(String field, Object values) {
            if (field != null && values != null) {
                fields.put(field, values);
            }
        }
    }

    public static class PushResult {
        private final Long item_id = null;
        private final Long revision = null;
        private final String title = null;

        public long getItemId() {
            return Utils.getNative(item_id, -1L);
        }

        public long getRevisionId() {
            return Utils.getNative(revision, -1L);
        }

        public String getTitle() {
            return title;
        }
    }

    public static class Excerpt {
        private final String label = null;
        private final String text = null;

        public String getLabel() {
            return label;
        }

        public String getText() {
            return text;
        }
    }

    private final Application app = null;
    private final Boolean pinned = null;
    private final Boolean subscribed = null;
    private final Excerpt excerpt = null;
    private final Integer priority = null;
    private final Integer subscribed_count = null;
    private final Long item_id = null;
    private final Long revision = null;
    private final List<Field> fields;
    private final List<Right> rights = null;
    private final List<String> tags = null;
    private final Space space = null;
    private final String created_on = null;
    private final String external_id;
    private final String last_event_on = null;
    private final String link = null;
    private final String title = null;
    private final User created_by = null;

    // This member should not be included in any JSON built from this class,
    // hence the 'transient' keyword.
    private transient final HashMap<String, List<Object>> data = new HashMap<String, List<Object>>();

    /**
     * Creates a new, empty {@link Item} with no fields.
     */
    public Item() {
        this.external_id = null;
        this.fields = new ArrayList<Field>();
    }

    /**
     * Creates a new, empty {@link Item} with the fields from the given
     * application template.
     * 
     * @param application
     *        The application to use as a template.
     */
    public Item(Application application) {
        this();

        if (application != null) {
            this.fields.addAll(application.getFields());
        }
    }

    @Override
    public PushData getPushData() {
        PushData pushData = new PushData(external_id);

        for (Field field : fields) {
            if (field != null) {
                Object data = field.getPushData();

                if (data != null) {
                    pushData.setValues(field.getExternalId(), data);
                }
            }
        }

        for (Entry<String, List<Object>> entry : data.entrySet()) {
            pushData.setValues(entry.getKey(), entry.getValue());
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
     * @throws FieldTypeMismatchException
     *         If the passed value doesn't match the field with the given name.
     */
    public void addValue(String field, Object value) throws FieldTypeMismatchException {
        Field f = findField(field, fields);

        if (f != null) {
            f.addValue(value);
        } else {
            HashMap<String, Object> newValue = new HashMap<String, Object>();
            newValue.put("value", value);

            List<Object> values = data.get(field);

            if (values == null) {
                values = new ArrayList<Object>();
                data.put(field, values);
            }

            values.add(newValue);
        }
    }

    /**
     * The same as {@link Item#addValue(String, Object)} but with some
     * convenience validation of the value (replaces null with empty string).
     * 
     * @throws FieldTypeMismatchException
     * @see {@link Item#addValue(String, Object)}
     */
    public void addValue(String field, String value) throws FieldTypeMismatchException {
        String actualData = Utils.isEmpty(value) ? "" : value;
        addValue(field, (Object) actualData);
    }

    public Application getApplication() {
        return app;
    }

    public User getCreatedByUser() {
        return created_by;
    }

    /**
     * Gets the end date of the calendar event as a Java Date object.
     * 
     * @return A date object, or null if the date couldn't be parsed.
     */
    public Date getCreatedDate() {
        return Utils.parseDateTime(created_on);
    }

    public String getCreatedDateString() {
        return created_on;
    }

    public Excerpt getExcerpt() {
        return excerpt;
    }

    public String getExternalId() {
        return external_id;
    }

    public List<Field> getFields() {
        return fields != null ?
                new ArrayList<Field>(fields) :
                new ArrayList<Field>();
    }

    public long getId() {
        return Utils.getNative(item_id, -1L);
    }

    /**
     * Gets the last event date of the item as a Java Date object.
     * 
     * @return A date object, or null if the date couldn't be parsed.
     */
    public Date getLastEventDate() {
        return Utils.parseDateTime(last_event_on);
    }

    public String getLastEventDateString() {
        return last_event_on;
    }

    public String getLink() {
        return link;
    }

    public int getNumberOfSubscriptions() {
        return Utils.getNative(subscribed_count, 0);
    }

    public int getPriority() {
        return Utils.getNative(priority, 0);
    }

    public long getRevisionId() {
        return Utils.getNative(revision, -1L);
    }

    public List<String> getTags() {
        return tags != null ?
                new ArrayList<String>(tags) :
                new ArrayList<String>();
    }

    public String getTitle() {
        return title;
    }

    /**
     * Tries to return a value for the given field. The value may or may not
     * have been verified by server. If you only want to get values that have
     * been verified by the server you should call the
     * {@link Item#getVerifiedValue(String)} method instead.
     * 
     * @param field
     *        The external id of the field you wish to fetch the value for.
     * @param index
     *        The index of the value you wish to get (fields can have multiple
     *        values, you know).
     * @return A Object representation of the value for the field or null.
     */
    public Object getValue(String field, int index) {
        if (Utils.notEmpty(field)) {
            Field f = findField(field, fields);

            if (f != null) {
                return f.getValue(index);
            } else {
                List<Object> values = data.get(field);

                if (values != null) {
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> value = (HashMap<String, Object>) values.get(index);

                    if (value != null) {
                        return value.get("value");
                    }
                }
            }
        }

        return null;
    }

    /**
     * Tries to return an API verified value for the given field.
     * 
     * @param field
     *        The external id of the field you wish to fetch the value for.
     * @param index
     *        The index of the value you wish to get (fields can have multiple
     *        values, you know).
     * @return A Object representation of the value for the field or null.
     */
    public Object getVerifiedValue(String field, int index) {
        if (Utils.notEmpty(field)) {
            Field f = findField(field, fields);

            if (f != null) {
                return f.getValue(index);
            }
        }

        return null;
    }

    public Space getWorkspace() {
        return space;
    }

    /**
     * Checks whether the list of rights the user has for this application
     * contains <em>all</em> the given permissions.
     * 
     * @param permissions
     *        The list of permissions to check for.
     * @return Boolean true if all given permissions are found or no permissions
     *         are given. Boolean false otherwise.
     */
    public boolean hasRights(Right... permissions) {
        if (rights != null) {
            for (Right permission : permissions) {
                if (!rights.contains(permission)) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public boolean isPinned() {
        return Utils.getNative(pinned, false);
    }

    public boolean isSubscribed() {
        return Utils.getNative(subscribed, false);
    }

    /**
     * Tries to clear the given value from the field with the given name on this
     * item.
     * 
     * @param field
     *        The external id of the field.
     * @param value
     *        The field type specific domain object describing the new value.
     * @throws FieldTypeMismatchException
     *         If the passed value doesn't match the field with the given name.
     */
    public void removeValue(String field, Object value) throws FieldTypeMismatchException {
        Field f = findField(field, fields);

        if (f != null) {
            f.removeValue(value);
        } else {
            List<Object> values = data.get(field);

            if (values != null) {
                for (Object o : values) {
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> v = (HashMap<String, Object>) o;

                    if (v.get("value").equals(value)) {
                        values.remove(o);
                    }
                }
            }
        }
    }

    /**
     * Looks for the field with with given external id in the list of fields.
     * 
     * @param externalId
     *        The external id of the field to find.
     * @return The field domain object if found, or null.
     */
    private Field findField(String externalId, List<Field> source) {
        if (Utils.isEmpty(externalId)) {
            throw new IllegalArgumentException("externalId cannot be empty");
        }

        if (source != null) {
            for (Field field : source) {
                if (field != null && externalId.equals(field.getExternalId())) {
                    return field;
                }
            }
        }

        return null;
    }
}
