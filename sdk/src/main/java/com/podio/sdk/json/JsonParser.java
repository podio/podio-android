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

package com.podio.sdk.json;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.podio.sdk.domain.TaskAction;
import com.podio.sdk.domain.field.Field;
import com.podio.sdk.domain.notification.Notification;
import com.podio.sdk.domain.notification.NotificationContext;
import com.podio.sdk.domain.reference.ReferenceGroup;
import com.podio.sdk.domain.stream.EventActivity;
import com.podio.sdk.domain.stream.EventContext;

public class JsonParser {

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .registerTypeAdapter(Field.class, new FieldDeserializerSerializer())
            .registerTypeAdapter(Notification.class, new NotificationDeserializerSerializer())
            .registerTypeAdapter(NotificationContext.class, new NotificationContextDeserializerSerializer())
            .registerTypeAdapter(EventContext.class, new EventContextDeserializerSerializer())
            .registerTypeAdapter(EventActivity.class, new EventActivityDeserializerSerializer())
            .registerTypeAdapter(TaskAction.class, new TaskActionDeserializerSerializer())
            .registerTypeAdapter(ReferenceGroup.class, new ReferenceGroupDeserializerSerializer())
            .disableHtmlEscaping()
            .create();

    public static <T> T fromJson(String json, Class<T> classOfResult) {
        try {
            return GSON.fromJson(json, classOfResult);
        } catch (JsonSyntaxException e) {
            throw new JsonSyntaxException("Couldn't parse API error json: " + json, e);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Couldn't parse API error json: " + json, e);
        }
    }

    public static <T> String toJson(T item) {
        return GSON.toJson(item);
    }
}
