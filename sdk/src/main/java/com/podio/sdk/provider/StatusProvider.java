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
import com.podio.sdk.domain.Status;

import java.util.List;

/**
 * Enables access to the Status API end point.
 *
 * @author rabie
 */
public class StatusProvider extends Provider {

    static class Path extends Filter {

        protected Path() {
            super("status");
        }

        Path withSpace() {
            addPathSegment("space");
            return this;
        }

        Path withId(long id) {
            addPathSegment(Long.toString(id));
            return this;
        }

        Path withAlertInvite(boolean alertInvite) {
            addQueryParameter("alert_invite", alertInvite ? "true" : "false");
            return this;
        }

    }

    /**
     * @param spaceId
     * @param alertInvite
     *         true if any mentioned user should be automatically invited to the workspace if the
     *         user does not have access to the object and access cannot be granted to the object.
     * @param value
     * @param fileIds
     *
     * @return
     */
    public Request<Status> addStatusMessage(long spaceId, boolean alertInvite, String value, List<Long> fileIds) {
        Path path = new Path().withSpace().withId(spaceId).withAlertInvite(alertInvite);
        return post(path, new Status.PushData(value, fileIds), Status.class);
    }

}
