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
public class File {
    private final Long file_id;
    private final Integer size;
    private final String description;
    private final String hosted_by;
    private final String hosted_by_humanized_name;
    private final String link;
    private final String link_target;
    private final String mimetype;
    private final String name;
    private final String perma_link;
    private final String thumbnail_link;

    public File(long fileId) {
        this.file_id = fileId;
        this.size = null;
        this.description = null;
        this.hosted_by = null;
        this.hosted_by_humanized_name = null;
        this.link = null;
        this.link_target = null;
        this.mimetype = null;
        this.name = null;
        this.perma_link = null;
        this.thumbnail_link = null;
    }

    public String getDescription() {
        return description;
    }

    public long getId() {
        return Utils.getNative(file_id, -1L);
    }

    public String getHostName() {
        return hosted_by;
    }

    public String getHumanizedHostName() {
        return hosted_by_humanized_name;
    }

    public String getLink() {
        return link;
    }

    public String getLinkTarget() {
        return link_target;
    }

    public String getMimeType() {
        return mimetype;
    }

    public String getName() {
        return name;
    }

    public String getPermaLink() {
        return perma_link;
    }

    public int getSize() {
        return Utils.getNative(size, 0);
    }

    public String getThumbnailLink() {
        return thumbnail_link;
    }

}
