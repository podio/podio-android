package com.podio.sdk.json;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.podio.sdk.domain.notification.CommentNotification;
import com.podio.sdk.domain.notification.GrantNotification;
import com.podio.sdk.domain.notification.Notification;
import com.podio.sdk.domain.notification.ParticipationNotification;
import com.podio.sdk.domain.notification.RatingNotification;
import com.podio.sdk.domain.notification.UnknownNotification;
import com.podio.sdk.domain.notification.VoteNotification;
import com.podio.sdk.internal.DefaultHashMap;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Notifications contains a dynamic "data" part that can have different content depending on the
 * type of notifications so we need to have this deserializer/serializer to decide what kind of
 * notification we are handling.
 *
 * @author Tobias Lindberg
 */
class NotificationDeserializerSerializer implements JsonDeserializer<Notification>, JsonSerializer<Notification> {

    private Map<Notification.NotificationType, Class<? extends Notification>> mNotificationClassesMap;

    public NotificationDeserializerSerializer() {
        mNotificationClassesMap = new DefaultHashMap<Notification.NotificationType, Class<? extends Notification>>(UnknownNotification.class);
        mNotificationClassesMap.put(Notification.NotificationType.comment, CommentNotification.class);
        mNotificationClassesMap.put(Notification.NotificationType.rating, RatingNotification.class);
        mNotificationClassesMap.put(Notification.NotificationType.participation, ParticipationNotification.class);
        mNotificationClassesMap.put(Notification.NotificationType.vote, VoteNotification.class);
        mNotificationClassesMap.put(Notification.NotificationType.grant_create, GrantNotification.class);
    }

    @Override
    public Notification deserialize(JsonElement element, Type type, JsonDeserializationContext gsonContext) throws JsonParseException {
        if (element == null || element.isJsonNull()) {
            return null;
        }

        JsonObject jsonObject = element.getAsJsonObject();

        Notification.NotificationType notificationType = Notification.NotificationType.getType(jsonObject.get("type").getAsString());

        return gsonContext.deserialize(jsonObject, mNotificationClassesMap.get(notificationType));
    }

    @Override
    public JsonElement serialize(Notification notification, Type typeOfSrc, JsonSerializationContext gsonContext) {
        return gsonContext.serialize(notification, mNotificationClassesMap.get(notification.getType()));
    }
}
