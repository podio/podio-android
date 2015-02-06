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
package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.Profile;
import com.podio.sdk.internal.Utils;

/**
 * Enables access to the Conversation API end point.
 *
 * @author László Urszuly
 */
public class ContactProvider extends Provider {
    public static enum ContactType {
        user, space
    }

    public static enum ResultType {
        mini, full
    }

    public static enum Order {
        assign, message, reference, alert, overall
    }

    public class ContactFilterProvider extends Filter {

        protected ContactFilterProvider() {
            super("contact");
        }

        public ContactFilterProvider withField(String fieldName, String value) {
            addQueryParameter(fieldName, value);
            return this;
        }

        public ContactFilterProvider withContactType(ContactType type) {
            addQueryParameter("contact_type", type.name());
            return this;
        }

        public ContactFilterProvider withMyselfExcluded(boolean excludeMyself) {
            addQueryParameter("exclude_self", excludeMyself ? "true" : "false");
            return this;
        }

        public ContactFilterProvider withExternalId(String externalId) {
            addQueryParameter("external_id", externalId);
            return this;
        }

        public ContactFilterProvider withSpan(int limit, int offset) {
            addQueryParameter("limit", Integer.toString(limit, 10));
            addQueryParameter("offset", Integer.toString(offset, 10));
            return this;
        }

        public ContactFilterProvider withOrder(Order order) {
            addQueryParameter("order", order.name());
            return this;
        }

        public ContactFilterProvider withRequiredFields(String[] fieldNames) {
            if (Utils.notEmpty(fieldNames)) {
                String fields = Utils.join(fieldNames, ",");
                addQueryParameter("required", fields);
            }
            return this;
        }

        public ContactFilterProvider withResultType(ResultType resultType) {
            addQueryParameter("type", resultType.name());
            return this;
        }

        public Request<Profile[]> get() {
            return ContactProvider.this.get(this, Profile[].class);
        }
    }

    static class Path extends Filter {

        protected Path() {
            super("contact");
        }

        Path withProfileIds(String[] ids) {
            if (Utils.notEmpty(ids)) {
                String segment = Utils.join(ids, ",");
                addPathSegment(segment);
                addPathSegment("v2");
            }

            return this;
        }
    }

    /**
     * Returns a filter the caller can use to define which contacts to get.
     *
     * @return A contacts filter object.
     */
    public ContactFilterProvider filter() {
        return new ContactFilterProvider();
    }

    public Request<Profile[]> getWithProfileIds(long[] ids) {
        String[] strings = null;

        if (Utils.notEmpty(ids)) {
            int size = ids.length;
            strings = new String[size];

            for (int i = 0; i < size; i++) {
                strings[i] = Long.toString(ids[i], 10);
            }
        }

        Path filter = new Path().withProfileIds(strings);
        return get(filter, Profile[].class);
    }

}
