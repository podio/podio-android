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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.podio.sdk.domain.ReferenceType;
import com.podio.sdk.domain.stream.EventActivity;
import com.podio.sdk.domain.stream.GrantEventActivity;
import com.podio.sdk.domain.stream.ItemEventActivity;
import com.podio.sdk.domain.stream.ItemParticipationEventActivity;
import com.podio.sdk.domain.stream.QuestionAnswerEventActivity;
import com.podio.sdk.domain.stream.RatingEventActivity;
import com.podio.sdk.domain.stream.TaskActionEventActivity;
import com.podio.sdk.domain.stream.TaskEventActivity;
import com.podio.sdk.domain.stream.UnknownEventActivity;
import com.podio.sdk.internal.DefaultHashMap;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * EventActivity contains a dynamic "data" part that can have different content depending on the
 * type of activity so we need to have this deserializer to decide what kind of activity we are
 * handling.
 *
 * @author Tobias Lindberg
 */
class EventActivityDeserializer implements JsonDeserializer<EventActivity> {

    private Map<ReferenceType, Class<? extends EventActivity>> mEventActivityClassesMap;

    public EventActivityDeserializer() {
        mEventActivityClassesMap = new DefaultHashMap<ReferenceType, Class<? extends EventActivity>>(UnknownEventActivity.class);
        mEventActivityClassesMap.put(ReferenceType.grant, GrantEventActivity.class);
        mEventActivityClassesMap.put(ReferenceType.item, ItemEventActivity.class);
        mEventActivityClassesMap.put(ReferenceType.item_participation, ItemParticipationEventActivity.class);
        mEventActivityClassesMap.put(ReferenceType.question_answer, QuestionAnswerEventActivity.class);
        mEventActivityClassesMap.put(ReferenceType.rating, RatingEventActivity.class);
        mEventActivityClassesMap.put(ReferenceType.task_action, TaskActionEventActivity.class);
        mEventActivityClassesMap.put(ReferenceType.task, TaskEventActivity.class);
    }

    @Override
    public EventActivity deserialize(JsonElement element, Type type, JsonDeserializationContext gsonContext) throws JsonParseException {
        if (element == null || element.isJsonNull()) {
            return null;
        }

        JsonObject jsonObject = element.getAsJsonObject();
        ReferenceType referenceType = getType(jsonObject.get("type").getAsString());

        return gsonContext.deserialize(jsonObject, mEventActivityClassesMap.get(referenceType));
    }

    private ReferenceType getType(String type) {
        try {
            return ReferenceType.valueOf(type);
        } catch (NullPointerException e) {
            return ReferenceType.unknown;
        } catch (IllegalArgumentException e) {
            return ReferenceType.unknown;
        }
    }
}
