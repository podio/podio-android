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

import java.util.List;

import com.podio.sdk.domain.field.Field;
import com.podio.sdk.domain.helper.UserInfo;

public final class Application {

    public static final class Configuration {
        public static enum Type {
            standard, meeting
        }

        public final Boolean allow_attachments = null;
        public final Boolean allow_comments = null;
        public final Boolean allow_edit = null;
        public final Boolean approved = null;
        public final String description = null;
        public final String external_id = null;
        public final Boolean fivestar = null;
        public final String fivestar_label = null;
        public final String icon = null;
        public final Integer icon_id = null;
        public final String item_name = null;
        public final String name = null;
        public final Boolean rsvp = null;
        public final String rsvp_label = null;
        public final Boolean thumbs = null;
        public final String thumbs_label = null;
        public final Type type = null;
        public final Boolean yesno = null;
        public final String yesno_label = null;
    }

    public final Long app_id = null;
    public final Configuration config = null;
    public final List<Field> fields = null;
    public final String link = null;
    public final String link_add = null;
    public final UserInfo owner = null;
    public final String[] rights = null;
    public final Space space = null;
    public final Long space_id = null;
    public final Status status = null;
    public final String url = null;
    public final String url_add = null;
    public final String url_label = null;

}
