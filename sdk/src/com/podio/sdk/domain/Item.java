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

import java.util.HashMap;
import java.util.Map;

import com.podio.sdk.domain.field.Field;
import com.podio.sdk.domain.helper.UserInfo;

public final class Item {

    public static final class PushData {
        public final String external_id;
        public final Map<String, Object> fields;

        private PushData(String externalId) {
            this.external_id = externalId;
            this.fields = new HashMap<String, Object>();
        }

        private void addData(int fieldId, Object value) {
            if (fieldId > 0 && value != null) {
                String idString = Integer.toString(fieldId, 10);
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
    public final String external_id = null;
    public final Field[] fields = null;
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

    public Object getPushData() {
        PushData pushData = new PushData(external_id);

        for (Field field : fields) {
            pushData.addData(field.field_id, field.getPushData());
        }

        return pushData;
    }
}
