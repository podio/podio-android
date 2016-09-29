
package com.podio.sdk.domain;

import com.podio.sdk.domain.data.Data;
import com.podio.sdk.domain.field.Field;
import com.podio.sdk.domain.stream.EventContext;
import com.podio.sdk.internal.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Item implements Data {

    /**
     * A class describing the filter for the client side search request.
     */
    public static class FilterData {
        public static final transient int DEFAULT_LIMIT = 20;
        public static final transient int DEFAULT_OFFSET = 0;

        private boolean remember;
        private boolean sort_desc;
        private int limit;
        private int offset;
        private Map<String, Object> filters;
        private String sort_by;
        private boolean sort_nulls_last;

        public FilterData() {
            this.filters = new HashMap<String, Object>();
            this.sort_desc = true;
            this.limit = DEFAULT_LIMIT;
            this.offset = DEFAULT_OFFSET;
            this.remember = true;
            this.sort_nulls_last = false;
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

        public FilterData setSortNullLast(boolean sortNullLast) {
            this.sort_nulls_last = sortNullLast;
            return this;
        }
    }

    /**
     * A class representing the result given by the API when a items are being filtered.
     */
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
            return items != null ? new ArrayList<Item>(items) : new ArrayList<Item>();
        }
    }

    /**
     * A class representing the new item the client wants to create.
     */
    public static class CreateData {
        @SuppressWarnings("unused")
        private final String external_id;
        @SuppressWarnings("unused")
        private final Map<String, Object> fields;
        @SuppressWarnings("unused")
        private final List<Long> file_ids;
        @SuppressWarnings("unused")
        private final List<String> tags;

        private CreateData(String externalId) {
            this.external_id = externalId;
            this.fields = new HashMap<String, Object>();
            this.file_ids = new ArrayList<Long>();
            this.tags = new ArrayList<String>();
        }

        private void setValues(String field, Object values) {
            if (field != null && values != null) {
                fields.put(field, values);
            }
        }

        private void addFileId(long fileId) {
            file_ids.add(fileId);
        }

        private void addTag(String tag) {
            tags.add(tag);
        }
    }

    /**
     * A class representing the result given by the API when a new item is created.
     */
    public static class CreateResult {
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

    /**
     * A class representing an Excerpt of the item.
     */
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
    private final Byline created_by = null;
    private final Comment[] comments = null;
    private final Double priority = null;
    private final Excerpt excerpt = null;
    private final File[] files;
    private final Grant grant = null;
    private final Integer comment_count = null;
    private final Integer file_count = null;
    private final Integer grant_count = null;
    private final Integer like_count = null;
    private final Integer subscribed_count = null;
    private final List<Field> fields;
    private final List<String> rights = null;
    private final Long app_item_id = null;
    private final Long item_id = null;
    private final Long revision = null;
    private final Push push = null;
    private final Space space = null;
    private final String created_on = null;
    private final String external_id;
    private final String last_event_on = null;
    private final String link = null;
    private final String title = null;
    private final String[] tags = null;
    private final EventContext.UserRatings user_ratings = null;
    private final HashMap<Long, ItemParticipation> participants = null;
    private final ItemReferenceCount[] refs = null;
    private final LinkedAccountData linked_account_data = null;
    private final Reminder reminder = null;
    private final Recurrence recurrence = null;

    // These attributes are defined in the API source code,
    // but not supported by the SDK right now.
    //private final Object linked_account_id = null;
    //private final Object app_item_id_formatted = null;
    //private final Object is_liked = null;
    //private final Object ratings = null;
    //private final Object revisions = null;
    //private final Object initial_revision = null;
    //private final Object current_revisiion = null;
    //private final Object values = null;
    //private final Object ref = null;
    //private final Object invite = null;
    //private final Object presence = null;
    //private final Object created_via = null;
    //private final Object activity = null;

    // This member should not be included in any JSON built from this class,
    // hence the 'transient' keyword.
    private transient final HashMap<String, List<Field.Value>> unverifiedFieldValues = new HashMap<String, List<Field.Value>>();

    /**
     * Creates a new, empty Item with no fields.
     */
    public Item() {
        this.external_id = null;
        this.fields = new ArrayList<Field>();
        this.files = null;
    }

    /**
     * Creates a new, empty Item with no fields.
     */
    public Item(File[] files) {
        this.external_id = null;
        this.fields = new ArrayList<Field>();
        this.files = files;
    }

    /**
     * Creates a new, empty Item with the fields from the given application template.
     *
     * @param application
     *         The application to use as a template.
     */
    public Item(Application application) {
        this();

        if (application != null) {
            Field[] template = application.getTemplate();

            if (Utils.notEmpty(template)) {
                this.fields.addAll(Arrays.asList(template));
            }
        }
    }

    /**
     * Constructs a data structure which describes the changes to an item in a way so that the API
     * understands it.
     *
     * @return The change data structure.
     */
    public CreateData getCreateData() {
        CreateData createData = new CreateData(external_id);

        for (Field field : fields) {
            if (field != null) {
                Object data = field.getCreateData();

                if (data != null) {
                    createData.setValues(field.getExternalId(), data);
                }
            }
        }
        if(files != null) {
            for (File file : files) {
                createData.addFileId(file.getId());
            }
        }

        // Iterate over our unknown field types and blindly trust that the associated values match
        // the field. The server will do a validation and throw an error response back at us if they
        // don't match.
        for (Entry<String, List<Field.Value>> entry : unverifiedFieldValues.entrySet()) {
            String key = entry.getKey();
            List<Field.Value> values = entry.getValue();

            if (Utils.notEmpty(key) && values != null) {
                ArrayList<Map<String, Object>> createDataValues = new ArrayList<Map<String, Object>>();

                // Build "createData" objects for each value associated with the current field.
                for (Field.Value value : values) {
                    Map<String, Object> data = value != null ? value.getCreateData() : null;

                    if (data != null) {
                        createDataValues.add(data);
                    }
                }

                createData.setValues(entry.getKey(), createDataValues);
            }
        }

        return createData;
    }

    public void addValues(String field, List<Field.Value> fieldValues) {
        Field f = findField(field, fields);

        if (f != null) {
            f.setValues(fieldValues);
        } else {
            unverifiedFieldValues.put(field, new ArrayList<Field.Value>(fieldValues));
        }
    }

    /**
     * Tries to set the given value to the field with the given name on this item.
     *
     * @param field
     *         The external id of the field.
     * @param value
     *         The field type specific domain object describing the new value.
     */
    public void addValue(String field, Field.Value value) {
        Field f = findField(field, fields);

        if (f != null) {
            f.addValue(value);
        } else {
            List<Field.Value> values = unverifiedFieldValues.get(field);

            if (values == null) {
                values = new ArrayList<Field.Value>();
                unverifiedFieldValues.put(field, values);
            }

            values.add(value);
        }
    }

    public Application getApplication() {
        return app;
    }

    public long getApplicationItemId() {
        return Utils.getNative(app_item_id, -1L);
    }

    public int getCommentCount() {
        return Utils.getNative(comment_count, -1);
    }

    /**
     * Returns a mapping of profile ids and their RSVP status.
     *
     * @return the participants or null if this item {@link com.podio.sdk.domain.Application.Type}
     * isn't a "meeting"
     */
    public HashMap<Long, ItemParticipation> getParticipants() {
        return participants == null ? null : new HashMap<>(participants);
    }

    /**
     * Use this method to update the RSVP status of a profile (e.g. your own RSVP status). This
     * method is handy for updating a cached version of the item after doing a "set participation"
     * API call. With this method you don't need to ask the API for a fresh version of the item in
     * order to make sure a cached version is up to date.
     *
     * @param profileId
     * @param itemParticipation
     */
    public void putParticipant(long profileId, ItemParticipation itemParticipation) {
        participants.put(profileId, itemParticipation);
    }

    public List<Comment> getComments() {
        return Utils.notEmpty(comments) ? Arrays.asList(comments) : Arrays.asList(new Comment[0]);
    }

    public List<ItemReferenceCount> getReferenceCounts() {
        return Utils.notEmpty(refs) ? Arrays.asList(refs) : Arrays.asList(new ItemReferenceCount[0]);
    }

    public Byline getCreatedBy() {
        return created_by;
    }

    /**
     * Gets the end date of the calendar event as a Java Date object.
     *
     * @return A date object, or null if the date couldn't be parsed.
     */
    public Date getCreatedDate() {
        return Utils.parseDateTimeUtc(created_on);
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
        return Utils.notEmpty(fields) ? new ArrayList<Field>(fields) : new ArrayList<Field>();
    }

    public int getFileCount() {
        return Utils.getNative(file_count, -1);
    }

    public List<File> getFiles() {
        return Utils.notEmpty(files) ? Arrays.asList(files) : Arrays.asList(new File[0]);
    }

    public Grant getGrant() {
        return grant;
    }

    public int getGrantCount() {
        return Utils.getNative(grant_count, -1);
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
        return Utils.parseDateTimeUtc(last_event_on);
    }

    public String getLastEventDateString() {
        return last_event_on;
    }

    public int getLikeCount() {
        return Utils.getNative(like_count, -1);
    }

    public String getLink() {
        return link;
    }

    public double getPriority() {
        return Utils.getNative(priority, 0.0d);
    }

    public Push getPushMetaData() {
        return push;
    }

    public long getRevisionId() {
        return Utils.getNative(revision, -1L);
    }

    public int getSubscribedCount() {
        return Utils.getNative(subscribed_count, 0);
    }

    public List<String> getTags() {
        return Utils.notEmpty(tags) ? Arrays.asList(tags) : Arrays.asList(new String[0]);
    }

    public String getTitle() {
        return title;
    }

    public LinkedAccountData getLinkedAccountData() {
        return linked_account_data;
    }

    public Reminder getReminder() {
        return reminder;
    }

    public Recurrence getRecurrence() {
        return recurrence;
    }

    /**
     * Tries to return a value for the given field within this item. The value may or may not have
     * been prior verified by the API. If you only want to get values that have been verified by the
     * server you should call the {@link com.podio.sdk.domain.Item#getVerifiedValue(String, int)
     * getVerifiedValue(String, int)} method instead.
     *
     * @param field
     *         The external id of the field you wish to fetch the value for.
     * @param index
     *         The index of the value you wish to get (fields can have multiple values, you know).
     *
     * @return A generic {@link com.podio.sdk.domain.field.Field.Value Field.Value} representation
     * of the value for the field, or null if no field could be found with the given external id.
     * The caller is responsible for typecasting into any appropriate subclass implementation.
     */
    public Field.Value getValue(String field, int index) {
        if (Utils.notEmpty(field)) {
            Field f = findField(field, fields);

            if (f != null) {
                return f.getValue(index);
            } else {
                List<Field.Value> values = unverifiedFieldValues.get(field);

                if (Utils.notEmpty(values)) {
                    return values.get(index);
                }
            }
        }

        return null;
    }

    /**
     * Returns all values for the given field within this item. The value may or may not have been
     * prior verified by the API. If you only want to get values that have been verified by the
     * server you should call the {@link com.podio.sdk.domain.Item#getVerifiedValues(String)
     * getVerifiedValues(String)} method instead.
     *
     * @param field
     *         The external id of the field you wish to fetch the value for.
     *
     * @return A list of generic {@link com.podio.sdk.domain.field.Field.Value Field.Value} objects,
     * representing the values for the field, or an empty list (never null) if no field was found
     * with the given external id. The caller is responsible for typecasting any items into any
     * appropriate subclass implementation.
     */
    public List<Field.Value> getValues(String field) {
        List<Field.Value> values = null;

        if (Utils.notEmpty(field)) {
            Field f = findField(field, fields);
            values = f != null ? f.getValues() : unverifiedFieldValues.get(field);
        }

        return values != null ? values : new ArrayList<Field.Value>(0);
    }

    /**
     * Returns a value for the given API verified field.
     *
     * @param field
     *         The external id of the field to fetch the value for.
     * @param index
     *         The index of the value to get.
     *
     * @return A generic {@link com.podio.sdk.domain.field.Field.Value Field.Value} representation
     * of the value for the field, or null if no field could be found with the given external id.
     * The caller is responsible for typecasting into any appropriate subclass implementation.
     */
    public Field.Value getVerifiedValue(String field, int index) {
        Field f = null;

        if (Utils.notEmpty(field)) {
            f = findField(field, fields);
        }

        return f != null ? f.getValue(index) : null;
    }

    /**
     * Returns all values for the given API verified field.
     *
     * @param field
     *         The external id of the field to fetch the value for.
     *
     * @return A list of generic {@link com.podio.sdk.domain.field.Field.Value Field.Value} objects,
     * representing the values for the field, or an empty list (never null) if no field was found
     * with the given external id. The caller is responsible for typecasting any items into any
     * appropriate subclass implementation.
     */
    public List<Field.Value> getVerifiedValues(String field) {
        List<Field.Value> values = null;

        if (Utils.notEmpty(field)) {
            Field f = findField(field, fields);

            if (field != null) {
                values = f.getValues();
            }
        }

        return values != null ? values : new ArrayList<Field.Value>(0);
    }

    /**
     * Returns the workspace the app of this item lives in.
     *
     * @return The {@link com.podio.sdk.domain.Space Space} domain object representation for the
     * parent app.
     */
    public Space getSpace() {
        return space;
    }

    /**
     * @return returns the ratings of the active user on the object.
     */
    public EventContext.UserRatings getUserRatings() {
        return user_ratings;
    }

    /**
     * Checks whether the list of rights the user has for this Organization contains <em>all</em>
     * the given permissions.
     *
     * @param rights
     *         The list of permissions to check for.
     *
     * @return Boolean true if all given permissions are granted for this Organization. Boolean
     * false otherwise.
     */
    public boolean hasAllRights(Right... rights) {
        if (Utils.isEmpty(this.rights) && Utils.isEmpty(rights)) {
            // The user has no rights and wants to verify that.
            return true;
        }

        if (Utils.notEmpty(this.rights) && Utils.notEmpty(rights)) {
            for (Right right : rights) {
                if (!this.rights.contains(right.name())) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    /**
     * Checks whether the list of rights the user has for this Organization contains <em>any</em> of
     * the given permissions.
     *
     * @param rights
     *         The list of permissions to check any single one for.
     *
     * @return Boolean true if any given permission is granted for this Organization. Boolean false
     * otherwise.
     */
    public boolean hasAnyRights(Right... rights) {
        if (Utils.isEmpty(this.rights) && Utils.isEmpty(rights)) {
            // The user has no rights and wants to verify that.
            return true;
        }

        if (Utils.notEmpty(this.rights) && Utils.notEmpty(rights)) {
            for (Right right : rights) {
                if (this.rights.contains(right.name())) {
                    return true;
                }
            }
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
     * Removes the given value from the field with the given name on this item.
     *
     * @param field
     *         The external id of the field.
     * @param value
     *         The field type specific domain object describing the new value.
     */
    public void removeValue(String field, Field.Value value) {
        Field f = findField(field, fields);

        if (f != null) {
            f.removeValue(value);
        } else {
            List<Field.Value> values = unverifiedFieldValues.get(field);

            if (Utils.notEmpty(values)) {
                values.remove(value);
            }
        }
    }

    /**
     * Looks for a field with with a known external id in a collection of fields.
     *
     * @param externalId
     *         The external id of the field to find.
     * @param source
     *         The collection of fields to search in.
     *
     * @return The field domain object if found, otherwise null.
     */
    private Field findField(String externalId, List<Field> source) {
        if (Utils.notEmpty(externalId) && Utils.notEmpty(source)) {
            for (Field field : source) {
                if (field != null && externalId.equals(field.getExternalId())) {
                    return field;
                }
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return item_id.equals(item.item_id);

    }

    @Override
    public int hashCode() {
        return item_id.hashCode();
    }
}
