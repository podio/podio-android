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
import com.podio.sdk.domain.field.Field;
import com.podio.sdk.domain.notification.Notification;

import java.lang.reflect.Type;

/**
 * Notifications contains a dynamic "data" part that can have different content depending on the
 * type of notifications so we need to have this deserializer to decide what kind of notification we
 * are handling.
 *
 * @author Tobias Lindberg
 */
class NotificationDeserializer implements JsonDeserializer<Notification> {

    @Override
    public Notification deserialize(JsonElement element, Type type, JsonDeserializationContext gsonContext) throws JsonParseException {
        if (element == null || element.isJsonNull()) {
            return null;
        }

        JsonObject jsonObject = element.getAsJsonObject();

        Notification.Type typeEnum = Notification.Type.undefined;
        JsonElement fieldType = jsonObject.get("type");

        if (fieldType != null && !fieldType.isJsonNull()) {
            try {
                typeEnum = Notification.Type.valueOf(fieldType.getAsString());
            } catch (IllegalArgumentException e) {
            }
        }

        if (typeEnum == Notification.Type.undefined) {
            // Overwrite the type in the json so we get undefined instead of
            // null.
            jsonObject.addProperty("type", Field.Type.undefined.name());
        }

        return gsonContext.deserialize(jsonObject, typeEnum.getNotificationClass());
    }
}
