package com.podio.sdk.json;
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

    private Map<Notification.NotificationType, Class<? extends Notification>> mNotificatinClassesMap;

    public NotificationDeserializerSerializer() {
        mNotificatinClassesMap = new DefaultHashMap<Notification.NotificationType, Class<? extends Notification>>(UnknownNotification.class);
        mNotificatinClassesMap.put(Notification.NotificationType.comment, CommentNotification.class);
        mNotificatinClassesMap.put(Notification.NotificationType.rating, RatingNotification.class);
        mNotificatinClassesMap.put(Notification.NotificationType.participation, ParticipationNotification.class);
        mNotificatinClassesMap.put(Notification.NotificationType.vote, VoteNotification.class);
        mNotificatinClassesMap.put(Notification.NotificationType.grant_create, GrantNotification.class);
    }

    @Override
    public Notification deserialize(JsonElement element, Type type, JsonDeserializationContext gsonContext) throws JsonParseException {
        if (element == null || element.isJsonNull()) {
            return null;
        }

        JsonObject jsonObject = element.getAsJsonObject();

        Notification.NotificationType notificationType = Notification.NotificationType.getType(jsonObject.get("type").getAsString());

        return gsonContext.deserialize(jsonObject, mNotificatinClassesMap.get(notificationType));
    }

    @Override
    public JsonElement serialize(Notification notification, Type typeOfSrc, JsonSerializationContext gsonContext) {
        return gsonContext.serialize(notification, mNotificatinClassesMap.get(notification.getType()));
    }
}
