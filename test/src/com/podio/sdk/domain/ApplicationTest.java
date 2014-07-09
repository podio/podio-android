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

package com.podio.sdk.domain;

import java.util.List;

import android.test.AndroidTestCase;

import com.google.gson.Gson;
import com.podio.sdk.domain.field.Field;

public class ApplicationTest extends AndroidTestCase {

    /**
     * Verifies that the members of the {@link Application} class can be
     * initialized by parsing JSON.
     * 
     * <pre>
     * 
     * 1. Describe an Application domain object as a JSON string.
     * 
     * 2. Create an instance from the JSON with the Gson parser.
     * 
     * 3. Verify that the parsed object has the expected values for
     *      its members.
     * 
     * </pre>
     */
    public void testApplicationCanBeCreatedFromJson() {
        String json = new StringBuilder("{")
                .append("app_id:1,")
                .append("config:{")
                .append(" allow_attachments:true,")
                .append(" allow_comments:true,")
                .append(" allow_edit:true,")
                .append(" allow_create:true,")
                .append(" allow_tags:true,")
                .append(" approved:true,")
                .append(" disable_notifications:true,")
                .append(" show_app_item_id:true,")
                .append(" silent_creates:true,")
                .append(" silent_edits:true,")
                .append(" description:'DESCRIPTION',")
                .append(" external_id:'EXTERNALID',")
                .append(" fivestar:true,")
                .append(" fivestar_label:'FIVESTARLABEL',")
                .append(" icon:'ICON',")
                .append(" item_name:'ITEMNAME',")
                .append(" name:'NAME',")
                .append(" rsvp:true,")
                .append(" rsvp_label:'RSVPLABEL',")
                .append(" thumbs:true,")
                .append(" thumbs_label:'THUMBSLABEL',")
                .append(" type:'meeting',")
                .append(" yesno:true,")
                .append(" yesno_label:'YESNOLABEL',")
                .append(" app_item_id_padding:1,")
                .append(" app_item_id_prefix:'PREFIX',")
                .append(" usage:'USAGE',")
                .append(" default_view:'badge'")
                .append("},")
                .append("fields:[],")
                .append("link:'LINK',")
                .append("link_add:'LINKADD',")
                .append("owner:{},")
                .append("rights:['view'],")
                .append("space_id:1,")
                .append("status:'inactive',")
                .append("url:'URL',")
                .append("url_add:'URLADD',")
                .append("url_label:'URLLABEL',")
                .append("pinned:true,")
                .append("subscribed:true,")
                .append("current_revision:1,")
                .append("default_view_id:1,")
                .append("original:1,")
                .append("original_revision:1,")
                .append("mailbox:'MAILBOX',")
                .append("token:'TOKEN'")
                .append("}")
                .toString();

        Gson gson = new Gson();
        Application application = gson.fromJson(json, Application.class);

        assertNotNull(application);
        assertEquals(1, application.getAppId());

        Application.Configuration config = application.getConfiguration();
        assertNotNull(config);
        assertEquals(true, config.allowsAttachments());
        assertEquals(true, config.allowsComments());
        assertEquals(true, config.allowsEdit());
        assertEquals(true, config.isApproved());
        assertEquals(true, config.hasFiveStarRating());
        assertEquals(true, config.hasRsvpState());
        assertEquals(true, config.hasThumbsVoting());
        assertEquals(true, config.hasYesNoVoting());
        assertEquals("DESCRIPTION", config.getDescription());
        assertEquals("EXTERNALID", config.getExternalId());
        assertEquals("FIVESTARLABEL", config.getFiveStarLabel());
        assertEquals("ICON", config.getIconName());
        assertEquals("ITEMNAME", config.getItemName());
        assertEquals("NAME", config.getName());
        assertEquals("RSVPLABEL", config.getRsvpLabel());
        assertEquals("THUMBSLABEL", config.getThumbsLabel());
        assertEquals("YESNOLABEL", config.getYesNoLabel());
        assertEquals(Application.Type.meeting, config.getType());
        assertEquals(true, config.allowsCreate());
        assertEquals(true, config.allowsTags());
        assertEquals(true, config.hasDisabledNotifications());
        assertEquals(true, config.showAppItemId());
        assertEquals(true, config.hasSilentCreates());
        assertEquals(true, config.hasSilentEdits());
        assertEquals(1, config.getAppItemIdPaddingCount());
        assertEquals("PREFIX", config.getAppItemIdPrefix());
        assertEquals("USAGE", config.getUsageInfo());
        assertEquals(Application.View.badge, config.getDefaultViewType());

        List<Field> fields = application.getFields();
        assertNotNull(application.getFields());
        assertEquals(0, fields.size());

        assertEquals("LINK", application.getLink());
        assertEquals("LINKADD", application.getAddLink());
        assertNotNull(application.getOwner());
        assertEquals(true, application.hasRights(Right.view));
        assertEquals(false, application.hasRights(Right.delete));
        assertEquals(1, application.getSpaceId());
        assertEquals(Application.Status.inactive, application.getStatus());
        assertEquals("URL", application.getUrl());
        assertEquals("URLADD", application.getAddUrl());
        assertEquals("URLLABEL", application.getUrlLabel());
        assertEquals(true, application.isPinned());
        assertEquals(true, application.isSubscribed());
        assertEquals(1, application.getCurrentRevisionId());
        assertEquals(1, application.getDefaultViewId());
        assertEquals(1, application.getOriginAppId());
        assertEquals(1, application.getOriginAppRevisionId());
        assertEquals("MAILBOX", application.getMailbox());
        assertEquals("TOKEN", application.getToken());
    }

    /**
     * Verifies that the members of the {@link Application} class can be
     * initialized to default by instantiation.
     * 
     * <pre>
     * 
     * 1. Create a new Application object instance.
     * 
     * 3. Verify that the members have the default values.
     * 
     * </pre>
     */
    public void testApplicationCanBeCreatedFromInstantiation() {
        Application application = Application.newInstance();

        assertNotNull(application);
        assertEquals(-1, application.getAppId());

        Application.Configuration config = application.getConfiguration();
        assertNotNull(config);
        assertEquals(false, config.allowsAttachments());
        assertEquals(false, config.allowsComments());
        assertEquals(false, config.allowsEdit());
        assertEquals(false, config.isApproved());
        assertEquals(false, config.hasFiveStarRating());
        assertEquals(false, config.hasRsvpState());
        assertEquals(false, config.hasThumbsVoting());
        assertEquals(false, config.hasYesNoVoting());
        assertEquals(null, config.getDescription());
        assertEquals(null, config.getExternalId());
        assertEquals(null, config.getFiveStarLabel());
        assertEquals(null, config.getIconName());
        assertEquals(null, config.getItemName());
        assertEquals(null, config.getName());
        assertEquals(null, config.getRsvpLabel());
        assertEquals(null, config.getThumbsLabel());
        assertEquals(null, config.getYesNoLabel());
        assertEquals(Application.Type.undefined, config.getType());
        assertEquals(false, config.allowsCreate());
        assertEquals(false, config.allowsTags());
        assertEquals(false, config.hasDisabledNotifications());
        assertEquals(false, config.showAppItemId());
        assertEquals(false, config.hasSilentCreates());
        assertEquals(false, config.hasSilentEdits());
        assertEquals(0, config.getAppItemIdPaddingCount());
        assertEquals(null, config.getAppItemIdPrefix());
        assertEquals(null, config.getUsageInfo());
        assertEquals(Application.View.undefined, config.getDefaultViewType());

        List<Field> fields = application.getFields();
        assertNotNull(application.getFields());
        assertEquals(0, fields.size());

        assertEquals(null, application.getLink());
        assertEquals(null, application.getAddLink());
        assertEquals(null, application.getOwner());
        assertEquals(false, application.hasRights(Right.view));
        assertEquals(-1, application.getSpaceId());
        assertEquals(Application.Status.undefined, application.getStatus());
        assertEquals(null, application.getUrl());
        assertEquals(null, application.getAddUrl());
        assertEquals(null, application.getUrlLabel());
        assertEquals(false, application.isPinned());
        assertEquals(false, application.isSubscribed());
        assertEquals(-1, application.getCurrentRevisionId());
        assertEquals(-1, application.getDefaultViewId());
        assertEquals(-1, application.getOriginAppId());
        assertEquals(-1, application.getOriginAppRevisionId());
        assertEquals(null, application.getMailbox());
        assertEquals(null, application.getToken());
    }

    /**
     * Verifies that the {@link Application#getConfiguration()} method doesn't
     * return null, even if the JSON didn't specify the 'config' attribute.
     * 
     * <pre>
     * 
     * 1. Describe an Application in JSON without a 'config' attribute.
     * 
     * 2. Parse the JSON into a new Application object.
     * 
     * 3. Verify that a non-null object is returned when asking for the
     *      application configuration.
     * 
     * </pre>
     */
    public void testConfigurationNeverNull() {
        Gson gson = new Gson();
        String json = "{}";
        Application application = gson.fromJson(json, Application.class);

        assertNotNull(application.getConfiguration());
    }

    /**
     * Verifies that the {@link Application#getFields()} method doesn't return
     * null, even if the JSON didn't specify the 'fields' attribute.
     * 
     * <pre>
     * 
     * 1. Describe an Application in JSON without a 'fields' attribute.
     * 
     * 2. Parse the JSON into a new Application object.
     * 
     * 3. Verify that a non-null object is returned when asking for the
     *      application fields.
     * 
     * </pre>
     */
    public void testFieldsNeverNull() {
        Gson gson = new Gson();
        String json = "{}";
        Application application = gson.fromJson(json, Application.class);

        List<Field> fields = application.getFields();
        assertNotNull(fields);
        assertEquals(0, fields.size());
    }

    /**
     * Verifies that the {@link Application#hasRights(Right...)} method defaults
     * to false if no rights are defined in JSON.
     * 
     * <pre>
     * 
     * 1. Describe an Application in JSON without a 'rights' attribute.
     * 
     * 2. Parse the JSON into a new Application object.
     * 
     * 3. Verify that false is returned when asking for an arbitrary right.
     * 
     * </pre>
     */
    public void testHasRightsDefaultsToFalse() {
        Gson gson = new Gson();
        String json = "{}";
        Application application = gson.fromJson(json, Application.class);

        assertEquals(false, application.hasRights(Right.view));
    }

    /**
     * Verifies that a {@link Application.Status} enum can be parsed from a JSON
     * string, using the Gson library.
     * 
     * <pre>
     * 
     * 1. Describe a simple Application object as a JSON string. Make sure it
     *      has a 'status' attribute.
     * 
     * 2. Parse the JSON string to an Application instance.
     * 
     * 3. Verify that the 'status' attribute has been parsed successfully as an
     *      enum value.
     * 
     * </pre>
     */
    public void testStatusEnumCanBeParsedFromJson() {
        Gson gson = new Gson();

        String json1 = "{status:'active'}";
        Application application1 = gson.fromJson(json1, Application.class);

        assertNotNull(application1);
        assertEquals(Application.Status.active, application1.getStatus());

        String json2 = "{}";
        Application application2 = gson.fromJson(json2, Application.class);

        assertNotNull(application2);
        assertEquals(Application.Status.undefined, application2.getStatus());

        String json3 = "{status:'fdafadsfads'}";
        Application application3 = gson.fromJson(json3, Application.class);

        assertNotNull(application3);
        assertEquals(Application.Status.undefined, application3.getStatus());
    }

    /**
     * Verifies that a {@link Application.Type} enum can be parsed from a JSON
     * string, using the Gson library.
     * 
     * <pre>
     * 
     * 1. Describe a simple Configuration object as a JSON string. Make sure it
     *      has a 'type' attribute.
     * 
     * 2. Parse the JSON string to a Configuration instance.
     * 
     * 3. Verify that the 'type' attribute has been parsed successfully as an
     *      enum value.
     * 
     * </pre>
     */
    public void testTypeEnumCanBeParsedFromJson() {
        Gson gson = new Gson();

        String json1 = "{type:'standard'}";
        Application.Configuration configuration1 = gson.fromJson(json1, Application.Configuration.class);

        assertNotNull(configuration1);
        assertEquals(Application.Type.standard, configuration1.getType());

        String json2 = "{}";
        Application.Configuration configuration2 = gson.fromJson(json2, Application.Configuration.class);

        assertNotNull(configuration2);
        assertEquals(Application.Type.undefined, configuration2.getType());

        String json3 = "{type: 'dasfasfdsafads'}";
        Application.Configuration configuration3 = gson.fromJson(json3, Application.Configuration.class);

        assertNotNull(configuration3);
        assertEquals(Application.Type.undefined, configuration3.getType());
    }

    /**
     * Verifies that a {@link Application.View} enum can be parsed from a JSON
     * string, using the Gson library.
     * 
     * <pre>
     * 
     * 1. Describe a simple Application Configuration object as a JSON string.
     *      Make sure it has a 'default_view' attribute.
     * 
     * 2. Parse the JSON string to an Application Configuration instance.
     * 
     * 3. Verify that the 'default_view' attribute has been parsed successfully as an
     *      enum value.
     * 
     * </pre>
     */
    public void testViewEnumCanBeParsedFromJson() {
        Gson gson = new Gson();

        String json1 = "{default_view:'badge'}";
        Application.Configuration configuration1 = gson.fromJson(json1, Application.Configuration.class);

        assertNotNull(configuration1);
        assertEquals(Application.View.badge, configuration1.getDefaultViewType());

        String json2 = "{}";
        Application.Configuration configuration2 = gson.fromJson(json2, Application.Configuration.class);

        assertNotNull(configuration2);
        assertEquals(Application.View.undefined, configuration2.getDefaultViewType());

        String json3 = "{status:'fdafadsfads'}";
        Application.Configuration configuration3 = gson.fromJson(json3, Application.Configuration.class);

        assertNotNull(configuration3);
        assertEquals(Application.View.undefined, configuration3.getDefaultViewType());
    }

    /**
     * Verifies that the {@link Application.Status} enum returns the correct
     * value.
     * 
     * <pre>
     * 
     * 1. Just do it.
     * 
     * </pre>
     */
    public void testValueOfStatus() {
        assertEquals(Application.Status.active, Application.Status.valueOf("active"));
        assertEquals(Application.Status.deleted, Application.Status.valueOf("deleted"));
        assertEquals(Application.Status.inactive, Application.Status.valueOf("inactive"));
        assertEquals(Application.Status.undefined, Application.Status.valueOf("undefined"));

        assertEquals(Application.Status.active, Enum.valueOf(Application.Status.class, "active"));
        assertEquals(Application.Status.deleted, Enum.valueOf(Application.Status.class, "deleted"));
        assertEquals(Application.Status.inactive, Enum.valueOf(Application.Status.class, "inactive"));
        assertEquals(Application.Status.undefined, Enum.valueOf(Application.Status.class, "undefined"));
    }

    /**
     * Verifies that the {@link Application.Type} enum returns the correct
     * value.
     * 
     * <pre>
     * 
     * 1. Just do it.
     * 
     * </pre>
     */
    public void testValueOfType() {
        assertEquals(Application.Type.standard, Application.Type.valueOf("standard"));
        assertEquals(Application.Type.meeting, Application.Type.valueOf("meeting"));
        assertEquals(Application.Type.undefined, Application.Type.valueOf("undefined"));

        assertEquals(Application.Type.standard, Enum.valueOf(Application.Type.class, "standard"));
        assertEquals(Application.Type.meeting, Enum.valueOf(Application.Type.class, "meeting"));
        assertEquals(Application.Type.undefined, Enum.valueOf(Application.Type.class, "undefined"));
    }

    /**
     * Verifies that the {@link Application.View} enum returns the correct
     * value.
     * 
     * <pre>
     * 
     * 1. Just do it.
     * 
     * </pre>
     */
    public void testValueOfView() {
        assertEquals(Application.View.badge, Application.View.valueOf("badge"));
        assertEquals(Application.View.calendar, Application.View.valueOf("calendar"));
        assertEquals(Application.View.card, Application.View.valueOf("card"));
        assertEquals(Application.View.stream, Application.View.valueOf("stream"));
        assertEquals(Application.View.table, Application.View.valueOf("table"));
        assertEquals(Application.View.undefined, Application.View.valueOf("undefined"));

        assertEquals(Application.View.badge, Enum.valueOf(Application.View.class, "badge"));
        assertEquals(Application.View.calendar, Enum.valueOf(Application.View.class, "calendar"));
        assertEquals(Application.View.card, Enum.valueOf(Application.View.class, "card"));
        assertEquals(Application.View.stream, Enum.valueOf(Application.View.class, "stream"));
        assertEquals(Application.View.table, Enum.valueOf(Application.View.class, "table"));
        assertEquals(Application.View.undefined, Enum.valueOf(Application.View.class, "undefined"));
    }
}
