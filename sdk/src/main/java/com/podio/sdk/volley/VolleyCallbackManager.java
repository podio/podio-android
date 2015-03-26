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
package com.podio.sdk.volley;

import com.podio.sdk.Request.SessionListener;
import com.podio.sdk.Session;
import com.podio.sdk.internal.CallbackManager;

import java.util.ArrayList;

final class VolleyCallbackManager<T> extends CallbackManager<T> {
    static final ArrayList<SessionListener> GLOBAL_SESSION_LISTENERS;

    static {
        GLOBAL_SESSION_LISTENERS = new ArrayList<SessionListener>();
    }

    static SessionListener addGlobalSessionListener(SessionListener sessionListener) {
        return sessionListener != null && GLOBAL_SESSION_LISTENERS.add(sessionListener) ?
                sessionListener :
                null;
    }

    static SessionListener removeGlobalSessionListener(SessionListener sessionListener) {
        int index = GLOBAL_SESSION_LISTENERS.indexOf(sessionListener);

        return GLOBAL_SESSION_LISTENERS.contains(sessionListener) ?
                GLOBAL_SESSION_LISTENERS.remove(index) :
                null;
    }

    private final ArrayList<SessionListener> sessionListeners;
    private final Object SESSION_LISTENER_LOCK = new Object();

    VolleyCallbackManager() {
        this.sessionListeners = new ArrayList<SessionListener>();
    }

    void addSessionListener(SessionListener listener, boolean deliverSessionNow) {
        if (listener != null) {
            if (deliverSessionNow) {
                listener.onSessionChanged(Session.accessToken(), Session.refreshToken(), Session.transferToken(), Session.expires());
            } else {
                synchronized (SESSION_LISTENER_LOCK) {
                    sessionListeners.add(listener);
                }
            }
        }
    }

    void deliverSession() {
        String accessToken = Session.accessToken();
        String refreshToken = Session.refreshToken();
        String transferToken = Session.transferToken();
        long expires = Session.expires();

        synchronized (SESSION_LISTENER_LOCK) {
            for (SessionListener listener : sessionListeners) {
                if (listener != null) {
                    if (listener.onSessionChanged(accessToken, refreshToken, transferToken, expires)) {
                        // The callback consumed the event, stop the bubbling.
                        break;
                    }
                }
            }

            sessionListeners.clear();
        }

        for (SessionListener listener : GLOBAL_SESSION_LISTENERS) {
            if (listener != null) {
                if (listener.onSessionChanged(accessToken, refreshToken, transferToken, expires)) {
                    // The callback consumed the event, stop the bubbling.
                    return;
                }
            }
        }
    }

    SessionListener removeSessionListener(SessionListener listener) {
        synchronized (SESSION_LISTENER_LOCK) {
            if (sessionListeners.contains(listener)) {
                int index = sessionListeners.indexOf(listener);
                return sessionListeners.remove(index);
            }

            return null;
        }
    }

}
