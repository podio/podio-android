

package com.podio.sdk.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.podio.sdk.domain.reference.AppReferenceGroup;
import com.podio.sdk.domain.reference.AppsReferenceGroup;
import com.podio.sdk.domain.reference.OrganisationTagReferenceGroup;
import com.podio.sdk.domain.reference.ProfilesReferenceGroup;
import com.podio.sdk.domain.reference.ReferenceGroup;
import com.podio.sdk.domain.reference.SpaceContactsSpaceMembersReferenceGroup;
import com.podio.sdk.domain.reference.SpacesReferenceGroup;
import com.podio.sdk.domain.reference.TasksReferenceGroup;
import com.podio.sdk.domain.reference.UnknownReferenceGroup;
import com.podio.sdk.internal.DefaultHashMap;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Reference groups contains a dynamic "data" and "contents" part that can have different content
 * depending on the name of the group so we need to have this deserializer/serializer to decide what
 * kind of group we are handling.
 *
 * @author Tobias Lindberg
 */
class ReferenceGroupDeserializerSerializer implements JsonDeserializer<ReferenceGroup>, JsonSerializer<ReferenceGroup> {

    private Map<ReferenceGroup.ReferenceGroupName, Class<? extends ReferenceGroup>> referenceGroupNameClassesMap;

    public ReferenceGroupDeserializerSerializer() {
        referenceGroupNameClassesMap = new DefaultHashMap<ReferenceGroup.ReferenceGroupName, Class<? extends ReferenceGroup>>(UnknownReferenceGroup.class);
        referenceGroupNameClassesMap.put(ReferenceGroup.ReferenceGroupName.space_contacts, SpaceContactsSpaceMembersReferenceGroup.class);
        referenceGroupNameClassesMap.put(ReferenceGroup.ReferenceGroupName.space_members, SpaceContactsSpaceMembersReferenceGroup.class);
        referenceGroupNameClassesMap.put(ReferenceGroup.ReferenceGroupName.spaces, SpacesReferenceGroup.class);
        referenceGroupNameClassesMap.put(ReferenceGroup.ReferenceGroupName.app, AppReferenceGroup.class);
        referenceGroupNameClassesMap.put(ReferenceGroup.ReferenceGroupName.profiles, ProfilesReferenceGroup.class);
        referenceGroupNameClassesMap.put(ReferenceGroup.ReferenceGroupName.tasks, TasksReferenceGroup.class);
        referenceGroupNameClassesMap.put(ReferenceGroup.ReferenceGroupName.apps, AppsReferenceGroup.class);
        referenceGroupNameClassesMap.put(ReferenceGroup.ReferenceGroupName.tag_field, OrganisationTagReferenceGroup.class);
    }

    @Override
    public ReferenceGroup deserialize(JsonElement element, Type type, JsonDeserializationContext gsonContext) throws JsonParseException {
        if (element == null || element.isJsonNull()) {
            return null;
        }

        JsonObject jsonObject = element.getAsJsonObject();
        ReferenceGroup.ReferenceGroupName referenceGroupName = ReferenceGroup.ReferenceGroupName.getReferenceGroupName(jsonObject.get("name").getAsString());

        return gsonContext.deserialize(jsonObject, referenceGroupNameClassesMap.get(referenceGroupName));
    }

    @Override
    public JsonElement serialize(ReferenceGroup referenceGroup, Type typeOfSrc, JsonSerializationContext gsonContext) {
        return gsonContext.serialize(referenceGroup, referenceGroupNameClassesMap.get(referenceGroup.getName()));
    }
}
