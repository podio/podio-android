
package com.podio.sdk.domain.notification;

import com.podio.sdk.domain.Item;

/**
 * This class is used when the notification context object is of type "item".
 *
 */
public class ItemNotificationContext extends NotificationContext {

    private final Item data = null;

    public Item getItem() {
        return data;
    }
}
