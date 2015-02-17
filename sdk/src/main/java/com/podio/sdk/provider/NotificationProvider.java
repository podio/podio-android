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
import com.podio.sdk.domain.NotificationGroup;

/**
 * Enables access to the NotificationGroup API end point.
 *
 * @author Tobias Lindberg
 */
public class NotificationProvider extends Provider {

    static class Path extends Filter {

        protected Path() {
            super("notification");
        }

        Path withId(long id) {
            addPathSegment(Long.toString(id, 10));
            return this;
        }

        Path withViewed(){
            addPathSegment("viewed");
            return this;
        }
    }

    /**
     * Fetches the NotificationGroup with the given id.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<NotificationGroup> getNotification(long id) {
        Path filter = new Path().withId(id);
        return get(filter, NotificationGroup.class);
    }

    /**
     * Marks all the users notifications as viewed.
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Void> markAllNotificationsAsViewed() {
        Path filter = new Path().withViewed();
        return post(filter,null,Void.class);

    }
}
