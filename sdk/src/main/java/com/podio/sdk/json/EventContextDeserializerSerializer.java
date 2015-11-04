
package com.podio.sdk.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.podio.sdk.domain.ReferenceType;
import com.podio.sdk.domain.stream.EventContext;
import com.podio.sdk.domain.stream.FileEventContext;
import com.podio.sdk.domain.stream.StatusEventContext;
import com.podio.sdk.domain.stream.UnknownEventContext;
import com.podio.sdk.internal.DefaultHashMap;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * EventContext contains a dynamic "data" part that can have different content depending on the type
 * of context so we need to have this deserializer/serializer to decide what kind of context we are
 * handling.
 *
 */
class EventContextDeserializerSerializer implements JsonDeserializer<EventContext>, JsonSerializer<EventContext> {

    private Map<ReferenceType, Class<? extends EventContext>> mEventContextClassesMap;

    public EventContextDeserializerSerializer() {
        mEventContextClassesMap = new DefaultHashMap<ReferenceType, Class<? extends EventContext>>(UnknownEventContext.class);
        mEventContextClassesMap.put(ReferenceType.status, StatusEventContext.class);
        mEventContextClassesMap.put(ReferenceType.file, FileEventContext.class);
    }

    @Override
    public EventContext deserialize(JsonElement element, Type type, JsonDeserializationContext gsonContext) throws JsonParseException {
        if (element == null || element.isJsonNull()) {
            return null;
        }

        JsonObject jsonObject = element.getAsJsonObject();
        ReferenceType referenceType = ReferenceType.getType(jsonObject.get("type").getAsString());

        return gsonContext.deserialize(jsonObject, mEventContextClassesMap.get(referenceType));
    }

    @Override
    public JsonElement serialize(EventContext eventContext, Type typeOfSrc, JsonSerializationContext gsonContext) {
        return gsonContext.serialize(eventContext, mEventContextClassesMap.get(eventContext.getType()));
    }
}
