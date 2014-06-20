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
     * Verifies that the fields of the {@link Application} class can be
     * populated by the Gson JSON parser.
     * 
     * <pre>
     * 
     * 1. Describe an Application domain object as a JSON string.
     * 
     * 2. Create an instance from the JSON with the Gson parser.
     * 
     * 3. Verify that the parsed object has the expected values for
     *      its fields.
     * 
     * </pre>
     */
    public void testApplicationCanBePopulatedByGson() {
        String json = new StringBuilder("{") //
        .append("app_id:1,") //
        .append("config:{") //
        .append(" allow_attachments:true,") //
        .append(" allow_comments:true,") //
        .append(" allow_edit:true,") //
        .append(" approved:true,") //
        .append(" description:'DESCRIPTION',") //
        .append(" external_id:'EXTERNALID',") //
        .append(" fivestar:true,") //
        .append(" fivestar_label:'FIVESTARLABEL',") //
        .append(" icon:'ICON',") //
        .append(" item_name:'ITEMNAME',") //
        .append(" name:'NAME',") //
        .append(" rsvp:true,") //
        .append(" rsvp_label:'RSVPLABEL',") //
        .append(" thumbs:true,") //
        .append(" thumbs_label:'THUMBSLABEL',") //
        .append(" type:'meeting',") //
        .append(" yesno:true,") //
        .append(" yesno_label:'YESNOLABEL'") //
        .append("},") //
        .append("fields:[],") //
        .append("link:'LINK',") //
        .append("link_add:'LINKADD',") //
        .append("owner:{},") //
        .append("rights:[],") //
        .append("space_id:1,") //
        .append("status:'inactive',") //
        .append("url:'URL',") //
        .append("url_add:'URLADD',") //
        .append("url_label:'URLLABEL'") //
        .append("}") //
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

        assertEquals("LINK", application.getLink());
        assertEquals("LINKADD", application.getAddLink());
        assertEquals("URL", application.getUrl());
        assertEquals("URLADD", application.getAddUrl());
        assertEquals("URLLABEL", application.getUrlLabel());

        List<Field> fields = application.getFields();
        assertNotNull(application.getFields());
        assertEquals(0, fields.size());

        assertNotNull(application.getOwner());
        assertEquals(0, application.getNumberOfPermissions());
        assertEquals(1, application.getSpaceId());
        assertEquals(Application.Status.inactive, application.getStatus());
    }

    /**
     * Verifies that a {@link Application.Configuration.Type} enum can be parsed
     * from a JSON string, using the Gson library.
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

        String json2 = "{type:'meeting'}";
        Application.Configuration configuration2 = gson.fromJson(json2, Application.Configuration.class);

        assertNotNull(configuration2);
        assertEquals(Application.Type.meeting, configuration2.getType());
    }

    /**
     * Verifies that the {@link Application.Configuration.Type} enum returns the
     * correct value.
     * 
     * <pre>
     * 
     * 1. Just do it.
     * 
     * </pre>
     */
    public void testTypeEnumGivesExpectedValueOf() {
        assertEquals(Application.Type.standard, Application.Type.valueOf("standard"));
        assertEquals(Application.Type.meeting, Application.Type.valueOf("meeting"));
    }
}
