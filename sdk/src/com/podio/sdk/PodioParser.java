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
import com.podio.sdk.domain.field.ApplicationReference;
import com.podio.sdk.domain.field.CalculationField;
import com.podio.sdk.domain.field.Category;
import com.podio.sdk.domain.field.ContactField;
import com.podio.sdk.domain.field.DateField;
import com.podio.sdk.domain.field.DurationField;
import com.podio.sdk.domain.field.EmbedField;
import com.podio.sdk.domain.field.EmptyField;
import com.podio.sdk.domain.field.Field;
import com.podio.sdk.domain.field.ImageField;
import com.podio.sdk.domain.field.LocationField;
import com.podio.sdk.domain.field.MoneyField;
import com.podio.sdk.domain.field.NumberField;
import com.podio.sdk.domain.field.ProgressField;
import com.podio.sdk.domain.field.Text;
import com.podio.sdk.domain.field.TitleField;
import com.podio.sdk.internal.utils.Utils;

/**
 * Defines a parser class to convert json data to and from domain model data
 * structures.
 * 
 * @param <T>
 *        The type of the domain model data structure.
 * @author László Urszuly
 */
public class PodioParser<T> {

    private static final class FieldDeserializer implements JsonDeserializer<Field> {

        @Override
        public Field deserialize(JsonElement element, Type type,
                JsonDeserializationContext gsonContext) throws JsonParseException {
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
				//Overwrite the type in the json so we get undefined instead of null
				jsonObject.addProperty("type", Field.Type.undefined.name());
			}
			
			return gsonContext.deserialize(jsonObject, typeEnum.getFieldClass());
        }
    }
    
	private static final Gson GSON_PARSER = new GsonBuilder()
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			.registerTypeAdapter(Field.class, new FieldDeserializer())
			.disableHtmlEscaping().create();

    private final Class<T> classOfItem;

    public PodioParser(Class<T> classOfItem) {
        this.classOfItem = classOfItem;
    }

    /**
     * Performs the parsing of the given json string to domain model
     * representation.
     * 
     * @param json
     *        The json string to parse.
     * @return A domain model representation of the given json string.
     */
    public T parseToItem(String source) {
        if (source == null || Utils.isEmpty(source.trim())) {
        	return null;
        }
        
        return GSON_PARSER.fromJson(source, classOfItem);
    }

    /**
     * Performs the parsing of the given domain model object to a json string.
     * 
     * @param item
     *        The item to parse.
     * @return A json string representation of the given item.
     */
    public String parseToJson(Object item) {
    	if (item == null) {
    		return null;
    	}
    	
    	return GSON_PARSER.toJson(item);
    }
    
    public static <T> PodioParser<T> fromClass(Class<T> classOfItem) {
    	return new PodioParser<T>(classOfItem);
    }

}
