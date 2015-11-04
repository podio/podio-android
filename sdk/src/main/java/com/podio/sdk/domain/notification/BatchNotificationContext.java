
package com.podio.sdk.domain.notification;

import com.podio.sdk.domain.Batch;

/**
 * This class is used when the notification context object is of type "batch_process" or "batch_complete".
 *
 */
public class BatchNotificationContext extends NotificationContext {

    private final Batch data = null;

    public Batch getBatch() {
        return data;
    }
}
