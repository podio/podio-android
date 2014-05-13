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

import android.test.AndroidTestCase;

import com.google.gson.Gson;

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
        .append(" icon_id:1,") //
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
        .append("space:{},") //
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
        assertEquals(Long.valueOf(1), application.app_id);
        assertNotNull(application.config);
        assertEquals(Boolean.TRUE, application.config.allow_attachments);
        assertEquals(Boolean.TRUE, application.config.allow_comments);
        assertEquals(Boolean.TRUE, application.config.allow_edit);
        assertEquals(Boolean.TRUE, application.config.approved);
        assertEquals("DESCRIPTION", application.config.description);
        assertEquals("EXTERNALID", application.config.external_id);
        assertEquals(Boolean.TRUE, application.config.fivestar);
        assertEquals("FIVESTARLABEL", application.config.fivestar_label);
        assertEquals("ICON", application.config.icon);
        assertEquals(Integer.valueOf(1), application.config.icon_id);
        assertEquals("ITEMNAME", application.config.item_name);
        assertEquals("NAME", application.config.name);
        assertEquals(Boolean.TRUE, application.config.rsvp);
        assertEquals("RSVPLABEL", application.config.rsvp_label);
        assertEquals(Boolean.TRUE, application.config.thumbs);
        assertEquals("THUMBSLABEL", application.config.thumbs_label);
        assertEquals(Application.Configuration.Type.meeting, application.config.type);
        assertEquals(Boolean.TRUE, application.config.yesno);
        assertEquals("YESNOLABEL", application.config.yesno_label);
        assertNotNull(application.fields);
        assertEquals(0, application.fields.length);
        assertEquals("LINK", application.link);
        assertEquals("LINKADD", application.link_add);
        assertNotNull(application.owner);
        assertNotNull(application.rights);
        assertEquals(0, application.rights.length);
        assertNotNull(application.space);
        assertEquals(Status.inactive, application.status);
        assertEquals("URL", application.url);
        assertEquals("URLADD", application.url_add);
        assertEquals("URLLABEL", application.url_label);
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
        assertEquals(Application.Configuration.Type.standard, configuration1.type);

        String json2 = "{type:'meeting'}";
        Application.Configuration configuration2 = gson.fromJson(json2, Application.Configuration.class);

        assertNotNull(configuration2);
        assertEquals(Application.Configuration.Type.meeting, configuration2.type);
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
        assertEquals(Application.Configuration.Type.standard, Application.Configuration.Type.valueOf("standard"));
        assertEquals(Application.Configuration.Type.meeting, Application.Configuration.Type.valueOf("meeting"));
    }
}
