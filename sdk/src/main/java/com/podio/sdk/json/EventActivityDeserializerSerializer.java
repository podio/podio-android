
package com.podio.sdk.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.podio.sdk.domain.ReferenceType;
import com.podio.sdk.domain.stream.EventActivity;
import com.podio.sdk.domain.stream.FileEventActivity;
import com.podio.sdk.domain.stream.GrantEventActivity;
import com.podio.sdk.domain.stream.ItemEventActivity;
import com.podio.sdk.domain.stream.ItemParticipationEventActivity;
import com.podio.sdk.domain.stream.QuestionAnswerEventActivity;
import com.podio.sdk.domain.stream.RatingEventActivity;
import com.podio.sdk.domain.stream.TaskActionEventActivity;
import com.podio.sdk.domain.stream.TaskEventActivity;
import com.podio.sdk.domain.stream.UnknownEventActivity;
import com.podio.sdk.domain.stream.VoteEventActivity;
import com.podio.sdk.internal.DefaultHashMap;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * EventActivity contains a dynamic "data" part that can have different content depending on the
 * type of activity so we need to have this deserializer/serializer to decide what kind of activity
 * we are handling.
 *
 */
class EventActivityDeserializerSerializer implements JsonDeserializer<EventActivity>, JsonSerializer<EventActivity> {

    private Map<ReferenceType, Class<? extends EventActivity>> mEventActivityClassesMap;

    public EventActivityDeserializerSerializer() {
        mEventActivityClassesMap = new DefaultHashMap<ReferenceType, Class<? extends EventActivity>>(UnknownEventActivity.class);
        mEventActivityClassesMap.put(ReferenceType.grant, GrantEventActivity.class);
        mEventActivityClassesMap.put(ReferenceType.item, ItemEventActivity.class);
        mEventActivityClassesMap.put(ReferenceType.item_participation, ItemParticipationEventActivity.class);
        mEventActivityClassesMap.put(ReferenceType.question_answer, QuestionAnswerEventActivity.class);
        mEventActivityClassesMap.put(ReferenceType.rating, RatingEventActivity.class);
        mEventActivityClassesMap.put(ReferenceType.task_action, TaskActionEventActivity.class);
        mEventActivityClassesMap.put(ReferenceType.task, TaskEventActivity.class);
        mEventActivityClassesMap.put(ReferenceType.vote, VoteEventActivity.class);
        mEventActivityClassesMap.put(ReferenceType.file, FileEventActivity.class);
    }

    @Override
    public EventActivity deserialize(JsonElement element, Type type, JsonDeserializationContext gsonContext) throws JsonParseException {
        if (element == null || element.isJsonNull()) {
            return null;
        }

        JsonObject jsonObject = element.getAsJsonObject();
        ReferenceType referenceType = ReferenceType.getType(jsonObject.get("type").getAsString());

        return gsonContext.deserialize(jsonObject, mEventActivityClassesMap.get(referenceType));
    }

    @Override
    public JsonElement serialize(EventActivity eventActivity, Type typeOfSrc, JsonSerializationContext gsonContext) {
        return gsonContext.serialize(eventActivity, mEventActivityClassesMap.get(eventActivity.getType()));
    }
}
