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

import java.util.ArrayList;

import com.podio.sdk.Request;
import com.podio.sdk.Request.AuthErrorListener;
import com.podio.sdk.Request.SessionListener;
import com.podio.sdk.Session;
import com.podio.sdk.internal.CallbackManager;

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

    private final ArrayList<AuthErrorListener<T>> authErrorListeners;
    private final ArrayList<SessionListener> sessionListeners;

    VolleyCallbackManager() {
        this.authErrorListeners = new ArrayList<AuthErrorListener<T>>();
        this.sessionListeners = new ArrayList<SessionListener>();
    }

    void addAuthErrorListener(AuthErrorListener<T> listener, boolean deliverNow, Request<T> originalRequest) {
        if (listener != null) {
            authErrorListeners.add(listener);

            if (deliverNow) {
                listener.onAuthErrorOccured(originalRequest);
            }
        }
    }

    void addSessionListener(SessionListener listener, boolean deliverSessionNow) {
        if (listener != null) {
            sessionListeners.add(listener);

            if (deliverSessionNow) {
                listener.onSessionChanged(Session.accessToken(), Session.refreshToken(), Session.expires());
            }
        }
    }

    void deliverAuthError(Request<T> originalRequest) {
        for (AuthErrorListener<T> listener : authErrorListeners) {
            if (listener != null) {
                if (listener.onAuthErrorOccured(originalRequest)) {
                    // The callback consumed the event, stop the bubbling.
                    return;
                }
            }
        }

    }

    void deliverSession() {
        String accessToken = Session.accessToken();
        String refreshToken = Session.refreshToken();
        long expires = Session.expires();

        for (SessionListener listener : sessionListeners) {
            if (listener != null) {
                if (listener.onSessionChanged(accessToken, refreshToken, expires)) {
                    // The callback consumed the event, stop the bubbling.
                    return;
                }
            }
        }

        for (SessionListener listener : GLOBAL_SESSION_LISTENERS) {
            if (listener != null) {
                if (listener.onSessionChanged(accessToken, refreshToken, expires)) {
                    // The callback consumed the event, stop the bubbling.
                    return;
                }
            }
        }
    }

    AuthErrorListener<T> removeAuthErrorListener(AuthErrorListener<T> listener) {
        int index = authErrorListeners.indexOf(listener);

        return authErrorListeners.contains(listener) ?
                authErrorListeners.remove(index) :
                null;
    }

    SessionListener removeSessionListener(SessionListener listener) {
        int index = sessionListeners.indexOf(listener);

        return sessionListeners.contains(listener) ?
                sessionListeners.remove(index) :
                null;
    }

}
