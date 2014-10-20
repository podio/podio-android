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
package com.podio.sdk;

import java.lang.reflect.Type;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.podio.sdk.domain.field.Field;

public class JsonParser {

    private static final class FieldDeserializer implements JsonDeserializer<Field> {

        @Override
        public Field deserialize(JsonElement element, Type type, JsonDeserializationContext gsonContext) throws JsonParseException {
            if (element == null || element.isJsonNull()) {
                return null;
            }

            JsonObject jsonObject = element.getAsJsonObject();

            // Ensure that we always have a "values" array, even if it's empty,
            // as this is needed when creating new items.
            if (!jsonObject.has("values")) {
                jsonObject.add("values", new JsonArray());
            }

            Field.Type typeEnum = Field.Type.undefined;
            JsonElement fieldType = jsonObject.get("type");

            if (fieldType != null && !fieldType.isJsonNull()) {
                try {
                    typeEnum = Field.Type.valueOf(fieldType.getAsString());
                } catch (IllegalArgumentException e) {
                }
            }

            if (typeEnum == Field.Type.undefined) {
                // Overwrite the type in the json so we get undefined instead of
                // null.
                jsonObject.addProperty("type", Field.Type.undefined.name());
            }

            return gsonContext.deserialize(jsonObject, typeEnum.getFieldClass());
        }
    }

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .registerTypeAdapter(Field.class, new FieldDeserializer())
            .disableHtmlEscaping()
            .create();

    public static <T> T fromJson(String json, Class<T> classOfResult) {
        return GSON.fromJson(json, classOfResult);
    }

    public static <T> String toJson(T item) {
        return GSON.toJson(item);
    }
}
