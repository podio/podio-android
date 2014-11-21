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
package com.podio.sdk.domain.push;

import com.podio.sdk.internal.Utils;

public class StreamCreate extends Event {

    private static class Data {
        /**
         * The main object that the event happened on.
         */
        private final Reference context_ref = null;

        /**
         * The object that was the cause of the event.
         */
        private final Reference data_ref = null;

        /**
         * The id of the app the event was on, if any.
         */
        private final Long app_id = null;
    }

    private final Data data = null;

    public String contextReferenceType() {
        return data != null && data.context_ref != null ? data.context_ref.type() : null;
    }

    public long contextReferenceId() {
        return data != null && data.context_ref != null ? data.context_ref.id() : -1L;
    }

    public String dataReferenceType() {
        return data != null && data.data_ref != null ? data.data_ref.type() : null;
    }

    public long dataReferenceId() {
        return data != null && data.data_ref != null ? data.data_ref.id() : -1L;
    }

    public long appId() {
        return data != null ? Utils.getNative(data.app_id, -1L) : -1L;
    }

}
