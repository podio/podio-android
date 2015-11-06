
package com.podio.sdk.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.podio.sdk.domain.ReferenceType;
import com.podio.sdk.domain.notification.AppNotificationContext;
import com.podio.sdk.domain.notification.BatchNotificationContext;
import com.podio.sdk.domain.notification.ItemNotificationContext;
import com.podio.sdk.domain.notification.NotificationContext;
import com.podio.sdk.domain.notification.UnknownNotificationContext;
import com.podio.sdk.internal.DefaultHashMap;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Notification context contains a dynamic "data" part that can have different content depending on
 * the type of context so we need to have this deserializer/serializer to decide what kind of
 * context we are handling.
 *
 */
class NotificationContextDeserializerSerializer implements JsonDeserializer<NotificationContext>, JsonSerializer<NotificationContext> {

    private Map<ReferenceType, Class<? extends NotificationContext>> mNotificationContextClassesMap;

    public NotificationContextDeserializerSerializer() {
        mNotificationContextClassesMap = new DefaultHashMap<ReferenceType, Class<? extends NotificationContext>>(UnknownNotificationContext.class);
        mNotificationContextClassesMap.put(ReferenceType.app, AppNotificationContext.class);
        mNotificationContextClassesMap.put(ReferenceType.item, ItemNotificationContext.class);
        mNotificationContextClassesMap.put(ReferenceType.batch, BatchNotificationContext.class);
    }

    @Override
    public NotificationContext deserialize(JsonElement element, Type type, JsonDeserializationContext gsonContext) throws JsonParseException {
        if (element == null || element.isJsonNull()) {
            return null;
        }

        JsonObject jsonObject = element.getAsJsonObject();
        ReferenceType referenceType = ReferenceType.getType(jsonObject.get("ref").getAsJsonObject().get("type").getAsString());

        return gsonContext.deserialize(jsonObject, mNotificationContextClassesMap.get(referenceType));
    }

    @Override
    public JsonElement serialize(NotificationContext notificationContext, Type typeOfSrc, JsonSerializationContext gsonContext) {
        return gsonContext.serialize(notificationContext, mNotificationContextClassesMap.get(notificationContext.getReference().getType()));
    }
}
