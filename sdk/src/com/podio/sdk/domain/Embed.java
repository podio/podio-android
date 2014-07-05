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

import com.podio.sdk.internal.Utils;

/**
 * @author László Urszuly
 */
public class Embed {

    public static enum Type {
        link, undefined
    }

    private final Integer embed_height = null;
    private final Integer embed_id = null;
    private final Integer embed_width = null;

    private final String description = null;
    private final String embed_html = null;
    private final String hostname = null;
    private final String original_url = null;
    private final String resolved_url = null;
    private final String title = null;
    private final String url;

    private final Type type = null;

    public Embed(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public int getEmbedHeight() {
        return Utils.getNative(embed_height, 0);
    }

    public String getEmbedHtml() {
        return embed_html;
    }

    public int getEmbedId() {
        return Utils.getNative(embed_id, -1);
    }

    public int getEmbedWidth() {
        return Utils.getNative(embed_width, 0);
    }

    public String getHostName() {
        return hostname;
    }

    public String getOriginalUrl() {
        return original_url;
    }

    public String getResolvedUrl() {
        return resolved_url;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public Type getType() {
        return type != null ? type : Type.undefined;
    }
}
