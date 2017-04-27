
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
                listener.onSessionChanged();
            } else {
                synchronized (SESSION_LISTENER_LOCK) {
                    sessionListeners.add(listener);
                }
            }
        }
    }

    void deliverSession() {
        synchronized (SESSION_LISTENER_LOCK) {
            boolean isConsumed = false;
            for (SessionListener listener : sessionListeners) {
                if (listener != null) {
                    if (listener.onSessionChanged()) {
                        // The callback consumed the event, stop the bubbling.
                        isConsumed = true;
                        break;
                    }
                }
            }

            sessionListeners.clear();
            if (isConsumed) {
                return;
            }
        }

        for (SessionListener listener : GLOBAL_SESSION_LISTENERS) {
            if (listener != null) {
                if (listener.onSessionChanged()) {
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
