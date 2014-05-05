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

public final class ItemRequest {

    public static final class Filter {
        private Filter() {
            // Hide the constructor.
        }
    }

    public static final class Result {
        public final Integer total = null;
        public final Integer filtered = null;
        public final Item[] items = null;

        private Result() {
            // Hide the constructor.
        }
    }

    public final String sort_by;
    public final Boolean sort_desc;
    public final Filter filters;
    public final Integer limit;
    public final Integer offset;
    public final Boolean remember;

    public ItemRequest(String sortBy, Boolean doSortDescending, Filter filter, Integer limit,
            Integer offset, Boolean doRemember) {

        this.sort_by = sortBy;
        this.sort_desc = doSortDescending;
        this.filters = filter;
        this.limit = limit;
        this.offset = offset;
        this.remember = doRemember;
    }

}
