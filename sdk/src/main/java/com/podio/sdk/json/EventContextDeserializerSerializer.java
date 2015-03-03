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
 * @author Tobias Lindberg
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
