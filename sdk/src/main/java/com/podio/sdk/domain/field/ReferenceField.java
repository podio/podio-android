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

import com.podio.sdk.domain.Application;
import com.podio.sdk.domain.File;
import com.podio.sdk.domain.Profile;
import com.podio.sdk.domain.Space;
import com.podio.sdk.internal.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author László Urszuly
 */
public class ReferenceField extends Field<ReferenceField.Value> {
    /**
     * This class describes the particular settings of a Reference field configuration.
     *
     * @author László Urszuly
     */
    private static class Settings {
        private final Boolean multiple = null;
        private final Application[] apps = null;
        private final Long[] referencable_types = null;
    }

    /**
     * This class describes the specific configuration of a Reference field.
     *
     * @author László Urszuly
     */
    public static class Configuration extends Field.Configuration {
        private final Value default_value = null;
        private final Settings settings = null;

        public Value getDefaultValue() {
            return default_value;
        }

        public List<Application> getApps() {
            return settings != null && settings.apps != null ? Arrays.asList(settings.apps) : Arrays.asList(new Application[0]);
        }

        public List<Long> getReferencableTypes() {
            return settings != null && settings.referencable_types != null ? Arrays.asList(settings.referencable_types) : Arrays.asList(new Long[0]);
        }

        public boolean isMultiple() {
            return settings != null && Utils.getNative(settings.multiple, false);
        }
    }

    /**
     * This class describes the Reference value data.
     *
     * @author László Urszuly
     */
    public static class Data {
        private final Application app = null;
        private final Long app_item_id = null;
        private final Long item_id;
        private final Long revision = null;
        private final File[] files = null;
        private final Space space = null;
        private final String created_on = null;
        private final String link = null;
        private final String title = null;
        private final Profile created_by = null;

        public Data(Long itemId) {
            this.item_id = itemId;
        }

        public Application getApplication() {
            return app;
        }

        public long getAppItemId() {
            return Utils.getNative(app_item_id, -1L);
        }

        public long getItemId() {
            return Utils.getNative(item_id, -1L);
        }

        public long getRevisionId() {
            return Utils.getNative(revision, -1L);
        }

        public List<File> getFiles() {
            return files != null ? Arrays.asList(files) : Arrays.asList(new File[0]);
        }

        public Space getSpace() {
            return space;
        }

        public Date getCreationDate() {
            return Utils.parseDateTimeUtc(created_on);
        }

        public String getCreationDateString() {
            return created_on;
        }

        public String getLink() {
            return link;
        }

        public String getTitle() {
            return title;
        }

        public Profile getCreatedBy() {
            return created_by;
        }
    }

    /**
     * This class describes a Reference field value.
     *
     * @author László Urszuly
     */
    public static class Value extends Field.Value {
        private final Data value;

        public Value(Data data) {
            this.value = data;
        }

        public Value(long itemId) {
            this.value = new Data(itemId);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Value) {
                Value other = (Value) o;

                if (other.value.app_item_id != null && this.value != null && this.value.app_item_id != null) {
                    return other.value.app_item_id.intValue() == this.value.app_item_id.intValue();
                }
            }

            return false;
        }

        @Override
        public Map<String, Object> getCreateData() {
            HashMap<String, Object> data = null;

            if (value != null && value.app_item_id != null) {
                data = new HashMap<String, Object>();
                data.put("value", value.app_item_id);
            }

            return data;
        }

        @Override
        public int hashCode() {
            return value != null && value.app_item_id != null ? value.app_item_id.hashCode() : 0;
        }

        public Application getApplication() {
            return value != null ? value.getApplication() : null;
        }

        public long getAppItemId() {
            return value != null ? value.getAppItemId() : -1L;
        }

        public long getItemId() {
            return value != null ? value.getItemId() : -1L;
        }

        public long getRevisionId() {
            return value != null ? value.getRevisionId() : -1L;
        }

        public List<File> getFiles() {
            return value != null ? value.getFiles() : null;
        }

        public Space getSpace() {
            return value != null ? value.getSpace() : null;
        }

        public Date getCreationDate() {
            return value != null ? value.getCreationDate() : null;
        }

        public String getCreationDateString() {
            return value != null ? value.getCreationDateString() : null;
        }

        public String getLink() {
            return value != null ? value.getLink() : null;
        }

        public String getTitle() {
            return value != null ? value.getTitle() : null;
        }

        public Profile getCreatedBy() {
            return value != null ? value.getCreatedBy() : null;
        }
    }

    // Private fields.
    private final Configuration config = null;
    private final ArrayList<Value> values;

    public ReferenceField(String externalId) {
        super(externalId);
        this.values = new ArrayList<Value>();
    }

    @Override
    public void setValues(List<Value> values) {
        this.values.clear();
        this.values.addAll(values);
    }

    @Override
    public void addValue(Value value) {
        if (values != null && !values.contains(value)) {
            values.add(value);
        }
    }

    @Override
    public Value getValue(int index) {
        return values != null ? values.get(index) : null;
    }

    @Override
    public List<Value> getValues() {
        return values;
    }

    @Override
    public void removeValue(Value value) {
        if (values != null && values.contains(value)) {
            values.remove(value);
        }
    }

    @Override
    public int valuesCount() {
        return values != null ? values.size() : 0;
    }

    public Configuration getConfiguration() {
        return config;
    }
}
