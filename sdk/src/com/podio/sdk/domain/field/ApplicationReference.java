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
import com.podio.sdk.domain.Space;
import com.podio.sdk.domain.helper.UserInfo;

public final class ApplicationReference extends Field {

    public static final class Value {

        public static final class Data {
            public final Long app_item_id;
            public final Long item_id;

            public final Application app = null;
            public final UserInfo created_by = null;
            public final String created_on = null;
            public final String link = null;
            public final Space space = null;
            public final String title = null;

            public Data(Long itemId, Long appItemId) {
                this.app_item_id = appItemId;
                this.item_id = itemId;
            }
        }

        public final Data value;

        public Value(Long itemId, Long appItemId) {

            this.value = new Data(itemId, appItemId);
        }
    }

    public final Value[] values;

    public ApplicationReference(Value[] values) {
        this.values = values;
    }

    @Override
    public Object getPushData() {
        return null;
    }
}
