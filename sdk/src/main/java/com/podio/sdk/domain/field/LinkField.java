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

import com.podio.sdk.domain.Embed;
import com.podio.sdk.domain.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author László Urszuly
 */
public class LinkField extends Field<LinkField.Value> {
    /**
     * This class describes the particular settings of a Link field configuration.
     *
     * @author László Urszuly
     */
    private static class Settings {
    }

    /**
     * This class describes the specific configuration of a Link field.
     *
     * @author László Urszuly
     */
    public static class Configuration extends Field.Configuration {
        private final Value default_value = null;
        private final Settings settings = null;

        public Value getDefaultValue() {
            return default_value;
        }
    }

    /**
     * This class describes a Link field value.
     *
     * @author László Urszuly
     */
    public static class Value extends Field.Value {
        private final Embed embed;
        private final File file;

        public Value(Embed embed, File file) {
            this.embed = embed;
            this.file = file;
        }

        public File getFile() {
            return file;
        }

        public Embed getEmbed() {
            return embed;
        }

        @Override
        public Map<String, Object> getCreateData() {
            HashMap<String, Object> data = null;

            if (embed != null || file != null) {
                data = new HashMap<String, Object>();
            }

            if (embed != null) {
                long embedId = embed.getId();

                if (embedId > 0L) {
                    data.put("embed", embedId);
                }
            }

            if (file != null) {
                long id = file.getId();

                if (id > 0L) {
                    data.put("value", file.getId());
                }
            }

            return data;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Value value = (Value) o;

            if (embed != null ? !embed.equals(value.embed) : value.embed != null) return false;
            return !(file != null ? !file.equals(value.file) : value.file != null);

        }

        @Override
        public int hashCode() {
            int result = embed != null ? embed.hashCode() : 0;
            result = 31 * result + (file != null ? file.hashCode() : 0);
            return result;
        }
    }

    // Private fields.
    private final Configuration config = null;
    private final ArrayList<Value> values;

    public LinkField(String externalId) {
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
    public void clearValues() {
        values.clear();
    }

    @Override
    public int valuesCount() {
        return values != null ? values.size() : 0;
    }

    public Configuration getConfiguration() {
        return config;
    }
}
