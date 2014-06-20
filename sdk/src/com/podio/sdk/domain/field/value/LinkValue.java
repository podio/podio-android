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

import com.podio.sdk.domain.Embed;
import com.podio.sdk.domain.File;

/**
 * @author László Urszuly
 */
public final class LinkValue extends AbstractValue {

    public static final class Data {
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

    private final Data value;

    public LinkValue(Data value) {
        this.value = value;
    }

    public LinkValue(String value) {
        this.value = new Data(value);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LinkValue) {
            LinkValue other = (LinkValue) o;

            if (other.value != null && other.value.embed != null && this.value != null && this.value.embed != null && this.value.embed.getUrl() != null) {
                return this.value.embed.getUrl().equals(other.value.embed.getUrl());
            }
        }

        return false;
    }

    @Override
    public Object getPushData() {
        HashMap<String, String> data = null;

        if (value != null && value.embed != null && value.embed.getUrl() != null) {
            data = new HashMap<String, String>();
            data.put("value", value.embed.getUrl());
        }

        return data;
    }

    @Override
    public int hashCode() {
        return value != null && value.embed != null ? value.embed.getEmbedId() : -1;
    }

    public File getFile() {
        return value != null ? value.getFile() : null;
    }

    public Embed getEmbed() {
        return value != null ? value.getEmbed() : null;
    }

}
