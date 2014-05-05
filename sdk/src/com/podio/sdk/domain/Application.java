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

import com.podio.sdk.domain.helper.UserInfo;

public final class Application {

    public static final class Configuration {
        public final String type = null;
        public final String name = null;
        public final String item_name = null;
        public final String description = null;
        public final String usage = null;
        public final String external_id = null;
        public final String icon = null;
        public final Integer icon_id = null;
    }

    public static final class IntegrationInfo {
        private IntegrationInfo() {
            // hide the constructor.
        }
    }

    public final Long app_id = null;
    public final Configuration config = null;
    public final IntegrationInfo integration = null;
    public final String link = null;
    public final String link_add = null;
    public final Long original = null;
    public final Integer original_revision = null;
    public final UserInfo owner = null;
    public final Boolean pinned = null;
    public final String[] rights = null;
    public final Space space = null;
    public final Long space_id = null;
    public final String status = null;
    public final Boolean subscribed = null;
    public final String url = null;
    public final String url_add = null;
    public final String url_label = null;

    private Application() {
        // Hide the constructor.
    }
}
