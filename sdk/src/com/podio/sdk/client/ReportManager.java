package com.podio.sdk.client;

import java.util.List;

import android.os.Handler;

/**
 * This class helps performing the common routines when reporting back a result
 * to a callback interface.
 * 
 * @author László Urszuly
 * @param <T>
 */
abstract class ReportManager<T> {

    protected abstract boolean executeReport(T reference);

    void reportToListeners(final Handler handler, final T customListener, final List<T> globalListeners) {
        if (handler != null) {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    boolean didConsumeEvent = false;

                    // Report to the custom listener first...
                    if (customListener != null) {
                        didConsumeEvent = executeReport(customListener);
                    }

                    // ...and if it didn't consume the event, AND there are
                    // global listeners present, then continue reporting to
                    // them as well.
                    if (!didConsumeEvent && globalListeners != null && !globalListeners.isEmpty()) {
                        for (T reference : globalListeners) {
                            if (reference != null && executeReport(reference)) {
                                // The listener has consumed the event. Stop
                                // bubbling.
                                break;

                            }
                        }
                    }
                }

            });

        }
    }

}
