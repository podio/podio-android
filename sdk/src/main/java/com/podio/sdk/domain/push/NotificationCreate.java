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

public class NotificationCreate extends Event {

    private static class Settings {
        /**
         * Flag telling whether a sound should be emitted or not.
         */
        private final Boolean sound = null;

        /**
         * Flag telling whether the notification should be shown using the
         * browsers native system or not.
         */
        private final Boolean browser = null;
    }

    private static class Data {
        /**
         * A default text to show for the notification.
         */
        private final String text = null;

        /**
         * The link to the object that was the source of the notification.
         */
        private final String link = null;

        /**
         * The name of the icon to use.
         */
        private final String icon = null;

        /**
         * The id of the notification.
         */
        private final Long notification_id = null;

        /**
         * The users push notification settings.
         */
        private final Settings settings = null;
    }

    private final Data data = null;

    public String text() {
        return data != null ? data.text : null;
    }

    public String link() {
        return data != null ? data.link : null;
    }

    public String icon() {
        return data != null ? data.icon : null;
    }

    public long notificationId() {
        return data != null ? Utils.getNative(data.notification_id, -1L) : -1L;
    }

    public boolean shouldPlaySound() {
        return data != null && data.settings != null ? Utils.getNative(data.settings.sound, false) : false;
    }

    public boolean shouldShowInBrowserNativeSystem() {
        return data != null && data.settings != null ? Utils.getNative(data.settings.browser, false) : false;
    }

}
