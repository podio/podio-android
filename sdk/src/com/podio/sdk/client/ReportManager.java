package com.podio.sdk.client;

import java.lang.ref.WeakReference;
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

    protected abstract boolean executeReport(WeakReference<T> reference);

    void reportToListeners(final Handler handler, final WeakReference<T> customListener, final List<WeakReference<T>> globalListeners) {
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
                    // global listeners
                    // present, then continue reporting to them as well.
                    if (!didConsumeEvent && globalListeners != null && !globalListeners.isEmpty()) {
                        for (WeakReference<T> reference : globalListeners) {
                            if (reference.get() != null) {
                                if (executeReport(reference)) {
                                    // This particular listener has consumed the
                                    // event. No
                                    // further listeners will be notified.
                                    break;
                                }
                            } else {
                                // The callback has most likely been GCed, clean
                                // it out from
                                // the list of global listeners as well.
                                reference.clear();
                                globalListeners.remove(reference);
                            }
                        }
                    }
                }

            });

        }
    }

}
