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
import com.podio.sdk.Request;
import com.podio.sdk.domain.Search;
import com.podio.sdk.volley.VolleyProvider;

/**
 * Enables access to the Search API end point.
 * 
 * @author László Urszuly
 */
public class SearchProvider extends VolleyProvider {

    /**
     * The definition of currently supported searchable object types.
     * 
     * @author László Urszuly
     */
    public static enum Type {
        item, task, conversation, app, status, file, profile
    }

    static class Path extends Filter {
        protected Path() {
            super("search/v2");
        }

        Path withCounts() {
            addQueryParameter("counts", "true");
            return this;
        }

        Path withQuery(String query) {
            addQueryParameter("query", query);
            return this;
        }

        Path withSpan(int limit, int offset) {
            addQueryParameter("limit", Integer.toString(limit, 10));
            addQueryParameter("offset", Integer.toString(offset, 10));
            return this;
        }

        Path withType(Type type) {
            addQueryParameter("ref_type", type.name());
            return this;
        }
    }

    /**
     * Searches globally for the given object types matching the query.
     * 
     * @param text
     *        The text to match conversations to.
     * @param limit
     *        The maximum number of replies to return.
     * @param offset
     *        The number of replies to skip before start counting the number of
     *        replies to pick.
     * @param searchParticipants
     *        Whether to search for participant names or not.
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Search.Result<?>> globally(String text, Type type, int limit, int offset, boolean includeTotals) {
        Path filter = new Path().withType(type).withQuery(text).withSpan(limit, offset);
        if (includeTotals) {
            filter.withCounts();
        }

        return null;
    }
}
