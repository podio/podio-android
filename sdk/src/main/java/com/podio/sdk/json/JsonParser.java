
package com.podio.sdk.json;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.podio.sdk.domain.DataReference;
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
            .registerTypeAdapter(DataReference.class, new DataReferenceDeserializer())
            .disableHtmlEscaping()
            .serializeNulls()
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
