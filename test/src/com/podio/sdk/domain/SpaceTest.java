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

import java.util.Calendar;
import java.util.TimeZone;

import android.test.AndroidTestCase;

import com.google.gson.Gson;

/**
 * @author László Urszuly
 */
public class SpaceTest extends AndroidTestCase {

    /**
     * Verifies that the members of the {@link Space} class can be initialized
     * by parsing JSON.
     * 
     * <pre>
     * 
     * 1. Describe a Space domain object as a JSON string.
     * 
     * 2. Create an instance from the JSON string.
     * 
     * 3. Verify that the parsed object has the expected values for
     *      its members.
     * 
     * </pre>
     */
    public void testSpaceCanBeCreatedFromJson() {
        String json = new StringBuilder("{")
                .append("auto_join:true,")
                .append("post_on_new_app:true,")
                .append("post_on_new_member:true,")
                .append("subscribed:true,")
                .append("premium:true,")
                .append("rank:1,")
                .append("space_id:1,")
                .append("rights:['view'],")
                .append("org:{},")
                .append("privacy:'closed',")
                .append("role:'light',")
                .append("created_on:'2014-07-10 10:27:37',")
                .append("description:'DESCRIPTION',")
                .append("name:'NAME',")
                .append("url:'URL',")
                .append("url_label:'URLLABEL',")
                .append("video:'VIDEO',")
                .append("type:'emp_network',")
                .append("created_by:{}")
                .append("}").toString();

        Gson gson = new Gson();
        Space space = gson.fromJson(json, Space.class);

        assertNotNull(space);
        assertEquals(true, space.doAutoJoin());
        assertEquals(true, space.doPostOnNewApp());
        assertEquals(true, space.doPostOnNewMember());
        assertEquals(true, space.isSubscribed());
        assertEquals(true, space.isPremium());
        assertEquals(1, space.getRank());
        assertEquals(1, space.getId());
        assertEquals(true, space.hasRights(Right.view));
        assertEquals(false, space.hasRights(Right.comment));
        assertNotNull(space.getOrganization());
        assertEquals(Space.Privacy.closed, space.getPrivacy());
        assertEquals(Space.Role.light, space.getRole());
        assertEquals("2014-07-10 10:27:37", space.getCreatedDateString());

        Calendar createdCalendar = Calendar.getInstance();
        createdCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        createdCalendar.setTime(space.getCreatedDate());

        assertEquals(2014, createdCalendar.get(Calendar.YEAR));
        assertEquals(Calendar.JULY, createdCalendar.get(Calendar.MONTH));
        assertEquals(10, createdCalendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(10, createdCalendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(27, createdCalendar.get(Calendar.MINUTE));
        assertEquals(37, createdCalendar.get(Calendar.SECOND));

        assertEquals("DESCRIPTION", space.getDescription());
        assertEquals("NAME", space.getName());
        assertEquals("URL", space.getUrl());
        assertEquals("URLLABEL", space.getUrlLabel());
        assertEquals("VIDEO", space.getVideoId());
        assertEquals(Space.Type.emp_network, space.getType());
        assertNotNull(space.getCreatedByUser());
    }

    /**
     * Verifies that the members of the {@link Space} class can be initialized
     * to default by instantiation.
     * 
     * <pre>
     * 
     * 1. Create a new Space object instance.
     * 
     * 3. Verify that the members have the default values.
     * 
     * </pre>
     */
    public void testSpaceCanBeCreatedFromInstantiation() {
        Space space = Space.newInstance();

        assertNotNull(space);
        assertEquals(false, space.doAutoJoin());
        assertEquals(false, space.doPostOnNewApp());
        assertEquals(false, space.doPostOnNewMember());
        assertEquals(false, space.isSubscribed());
        assertEquals(false, space.isPremium());
        assertEquals(0, space.getRank());
        assertEquals(-1, space.getId());
        assertEquals(false, space.hasRights(Right.view));
        assertEquals(null, space.getOrganization());
        assertEquals(Space.Privacy.undefined, space.getPrivacy());
        assertEquals(Space.Role.undefined, space.getRole());
        assertEquals(null, space.getCreatedDateString());
        assertEquals(null, space.getCreatedDate());

        assertEquals(null, space.getDescription());
        assertEquals(null, space.getName());
        assertEquals(null, space.getUrl());
        assertEquals(null, space.getUrlLabel());
        assertEquals(null, space.getVideoId());
        assertEquals(Space.Type.undefined, space.getType());
        assertEquals(null, space.getCreatedByUser());
    }

    /**
     * Verifies that a {@link Space.Privacy} enum can be parsed from a JSON
     * string.
     * 
     * <pre>
     * 
     * 1. Describe a simple Space object as a JSON string. Make sure it
     *      has a 'privacy' attribute.
     * 
     * 2. Parse the JSON string to a Space instance.
     * 
     * 3. Verify that the 'privacy' attribute has been parsed successfully as an
     *      enum value.
     * 
     * </pre>
     */
    public void testPrivacyEnumCanBeParsedFromJson() {
        Gson gson = new Gson();
        Space space;

        space = gson.fromJson("{privacy:'open'}", Space.class);
        assertNotNull(space);
        assertEquals(Space.Privacy.open, space.getPrivacy());

        space = gson.fromJson("{}", Space.class);
        assertNotNull(space);
        assertEquals(Space.Privacy.undefined, space.getPrivacy());

        space = gson.fromJson("{status:'fdafadsfads'}", Space.class);
        assertNotNull(space);
        assertEquals(Space.Privacy.undefined, space.getPrivacy());
    }

    /**
     * Verifies that a {@link Space.Role} enum can be parsed from a JSON string.
     * 
     * <pre>
     * 
     * 1. Describe a simple Space object as a JSON string. Make sure it
     *      has a 'role' attribute.
     * 
     * 2. Parse the JSON string to a Space instance.
     * 
     * 3. Verify that the 'role' attribute has been parsed successfully as an
     *      enum value.
     * 
     * </pre>
     */
    public void testRoleEnumCanBeParsedFromJson() {
        Gson gson = new Gson();
        Space space;

        space = gson.fromJson("{role:'admin'}", Space.class);
        assertNotNull(space);
        assertEquals(Space.Role.admin, space.getRole());

        space = gson.fromJson("{}", Space.class);
        assertNotNull(space);
        assertEquals(Space.Role.undefined, space.getRole());

        space = gson.fromJson("{role:'fdafadsfads'}", Space.class);
        assertNotNull(space);
        assertEquals(Space.Role.undefined, space.getRole());
    }

    /**
     * Verifies that a {@link Space.Type} enum can be parsed from a JSON string.
     * 
     * <pre>
     * 
     * 1. Describe a simple Space object as a JSON string. Make sure it
     *      has a 'type' attribute.
     * 
     * 2. Parse the JSON string to a Space instance.
     * 
     * 3. Verify that the 'type' attribute has been parsed successfully as an
     *      enum value.
     * 
     * </pre>
     */
    public void testTypeEnumCanBeParsedFromJson() {
        Gson gson = new Gson();
        Space space;

        space = gson.fromJson("{type:'regular'}", Space.class);
        assertNotNull(space);
        assertEquals(Space.Type.regular, space.getType());

        space = gson.fromJson("{}", Space.class);
        assertNotNull(space);
        assertEquals(Space.Type.undefined, space.getType());

        space = gson.fromJson("{type:'fdafadsfads'}", Space.class);
        assertNotNull(space);
        assertEquals(Space.Type.undefined, space.getType());
    }

    /**
     * Verifies that the {@link Space.Privacy} enum returns the correct value.
     * 
     * <pre>
     * 
     * 1. Just do it.
     * 
     * </pre>
     */
    public void testValueOfPrivacy() {
        assertEquals(Space.Privacy.closed, Space.Privacy.valueOf("closed"));
        assertEquals(Space.Privacy.open, Space.Privacy.valueOf("open"));
        assertEquals(Space.Privacy.undefined, Space.Privacy.valueOf("undefined"));

        assertEquals(Space.Privacy.closed, Enum.valueOf(Space.Privacy.class, "closed"));
        assertEquals(Space.Privacy.open, Enum.valueOf(Space.Privacy.class, "open"));
        assertEquals(Space.Privacy.undefined, Enum.valueOf(Space.Privacy.class, "undefined"));
    }

    /**
     * Verifies that the {@link Space.Role} enum returns the correct value.
     * 
     * <pre>
     * 
     * 1. Just do it.
     * 
     * </pre>
     */
    public void testValueOfRole() {
        assertEquals(Space.Role.admin, Space.Role.valueOf("admin"));
        assertEquals(Space.Role.light, Space.Role.valueOf("light"));
        assertEquals(Space.Role.regular, Space.Role.valueOf("regular"));
        assertEquals(Space.Role.undefined, Space.Role.valueOf("undefined"));

        assertEquals(Space.Role.admin, Enum.valueOf(Space.Role.class, "admin"));
        assertEquals(Space.Role.light, Enum.valueOf(Space.Role.class, "light"));
        assertEquals(Space.Role.regular, Enum.valueOf(Space.Role.class, "regular"));
        assertEquals(Space.Role.undefined, Enum.valueOf(Space.Role.class, "undefined"));
    }

    /**
     * Verifies that the {@link Space.Type} enum returns the correct value.
     * 
     * <pre>
     * 
     * 1. Just do it.
     * 
     * </pre>
     */
    public void testValueOfType() {
        assertEquals(Space.Type.demo, Space.Type.valueOf("demo"));
        assertEquals(Space.Type.emp_network, Space.Type.valueOf("emp_network"));
        assertEquals(Space.Type.regular, Space.Type.valueOf("regular"));
        assertEquals(Space.Type.undefined, Space.Type.valueOf("undefined"));

        assertEquals(Space.Type.demo, Enum.valueOf(Space.Type.class, "demo"));
        assertEquals(Space.Type.emp_network, Enum.valueOf(Space.Type.class, "emp_network"));
        assertEquals(Space.Type.regular, Enum.valueOf(Space.Type.class, "regular"));
        assertEquals(Space.Type.undefined, Enum.valueOf(Space.Type.class, "undefined"));
    }

}
