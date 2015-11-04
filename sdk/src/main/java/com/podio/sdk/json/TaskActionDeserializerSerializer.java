package com.podio.sdk.json;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.podio.sdk.domain.AssignTaskAction;
import com.podio.sdk.domain.TaskAction;
import com.podio.sdk.domain.TextTaskAction;
import com.podio.sdk.internal.DefaultHashMap;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Notifications contains a dynamic "data" part that can have different content depending on the
 * type of notifications so we need to have this deserializer/serializer to decide what kind of
 * notification we are handling.
 *
 */
class TaskActionDeserializerSerializer implements JsonDeserializer<TaskAction>, JsonSerializer<TaskAction> {

    private Map<TaskAction.TaskActionType, Class<? extends TaskAction>> mTaskActionClassesMap;

    public TaskActionDeserializerSerializer() {
        mTaskActionClassesMap = new DefaultHashMap<TaskAction.TaskActionType, Class<? extends TaskAction>>(TextTaskAction.class);
        mTaskActionClassesMap.put(TaskAction.TaskActionType.assign, AssignTaskAction.class);
    }

    @Override
    public TaskAction deserialize(JsonElement element, Type type, JsonDeserializationContext gsonContext) throws JsonParseException {
        if (element == null || element.isJsonNull()) {
            return null;
        }

        JsonObject jsonObject = element.getAsJsonObject();

        TaskAction.TaskActionType taskActionType = TaskAction.TaskActionType.getType(jsonObject.get("type").getAsString());

        return gsonContext.deserialize(jsonObject, mTaskActionClassesMap.get(taskActionType));
    }

    @Override
    public JsonElement serialize(TaskAction taskAction, Type typeOfSrc, JsonSerializationContext gsonContext) {
        return gsonContext.serialize(taskAction, mTaskActionClassesMap.get(taskAction.getType()));
    }
}
