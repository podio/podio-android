/*
 * Copyright (C) 2015 Citrix Systems, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

/**
 * @author László Urszuly
 */
public class File {

    public static class PushData {
        private final java.io.File file;
        private final String name;
        private final String mimeType;

        public PushData(java.io.File file, String name, String mimeType) {
            this.file = file;
            this.name = name;
            this.mimeType = mimeType;
        }

        public java.io.File getFile() {
            return file;
        }

        public String getName() {
            return name;
        }

        public String getMimeType() {
            return mimeType;
        }
    }

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