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
import com.podio.sdk.internal.Utils;

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
     * This class describes the Link value data.
     *
     * @author László Urszuly
     */
    public static class Data {
        private final File file = null;
        private final Embed embed;

        public Data(String url) {
            this.embed = new Embed(url);
        }

        public File getFile() {
            return file;
        }

        public Embed getEmbed() {
            return embed;
        }
    }

    /**
     * This class describes a Link field value.
     *
     * @author László Urszuly
     */
    public static class Value extends Field.Value {
        private final Data value;

        public Value(Data value) {
            this.value = value;
        }

        public Value(String url) {
            this.value = new Data(url);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Value) {
                Value other = (Value) o;

                if (other.value != null && other.value.embed != null && this.value != null && this.value.embed != null && this.value.embed.getUrl() != null) {
                    return this.value.embed.getUrl().equals(other.value.embed.getUrl());
                }
            }

            return false;
        }

        @Override
        public Map<String, Object> getCreateData() {
            HashMap<String, Object> data = null;

            if (value != null) {
                if (value.embed != null) {
                    String url = value.embed.getUrl();

                    if (Utils.notEmpty(url)) {
                        data = new HashMap<String, Object>();
                        data.put("value", url);
                    }
                }

                if (data == null && value.file != null) {
                    long id = value.file.getId();

                    if (id > 0L) {
                        data = new HashMap<String, Object>();
                        data.put("value", id);
                    }
                }
            }

            return data;
        }

        @Override
        public int hashCode() {
            return (value != null && value.embed != null) ? value.embed.hashCode() : (value.file != null) ? value.file.hashCode() : 0;
        }

        public File getFile() {
            return value != null ? value.getFile() : null;
        }

        public Embed getEmbed() {
            return value != null ? value.getEmbed() : null;
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
