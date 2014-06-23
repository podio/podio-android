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

public class OrganizationTest extends AndroidTestCase {

    /**
     * Verifies that the fields of the {@link Organization} class can be
     * populated by the Gson JSON parser.
     * 
     * <pre>
     * 
     * 1. Describe an Organization domain object as a JSON string.
     * 
     * 2. Create an instance from the JSON with the Gson parser.
     * 
     * 3. Verify that the parsed object has the expected values for
     *      its fields.
     * 
     * </pre>
     */
    public void testOrganizationCanBePopulatedByGson() {
        String json = new StringBuilder("{")
                .append("logo:1,")
                .append("name:'NAME',")
                .append("org_id:1,")
                .append("rank:1,")
                .append("rights:['view'],")
                .append("role:'regular',")
                .append("spaces:[],")
                .append("status:'inactive',")
                .append("type:'premium',")
                .append("url:'URL',")
                .append("url_label:'URLLABEL'")
                .append("}").toString();
        Gson gson = new Gson();
        Organization organization = gson.fromJson(json, Organization.class);

        assertNotNull(organization);
        assertEquals(1, organization.getLogoId());
        assertEquals("NAME", organization.getName());
        assertEquals(1, organization.getId());
        assertEquals(1, organization.getRank());
        assertTrue(organization.hasRights(Right.view));
        assertFalse(organization.hasRights(Right.add_file));
        assertEquals(Organization.Role.regular, organization.getRole());
        List<Space> spaces = organization.getSpaces();
        assertNotNull(organization.getSpaces());
        assertEquals(0, spaces.size());
        assertEquals(Organization.Status.inactive, organization.getStatus());
        assertEquals(Organization.Type.premium, organization.getType());
        assertEquals("URL", organization.getUrl());
        assertEquals("URLLABEL", organization.getUrlLabel());
    }

    /**
     * Verifies that a {@link Organization.Role} enum can be parsed from a JSON
     * string, using the Gson library.
     * 
     * <pre>
     * 
     * 1. Describe a simple Organization object as a JSON string. Make sure it
     *      has a 'role' attribute.
     * 
     * 2. Parse the JSON string to an Organization instance.
     * 
     * 3. Verify that the 'role' attribute has been parsed successfully as an
     *      enum value.
     * 
     * </pre>
     */
    public void testRoleEnumCanBeParsedFromJson() {
        Gson gson = new Gson();

        String json1 = "{role:'admin'}";
        Organization organization1 = gson.fromJson(json1, Organization.class);

        assertNotNull(organization1);
        assertEquals(Organization.Role.admin, organization1.getRole());

        String json2 = "{role:'light'}";
        Organization organization2 = gson.fromJson(json2, Organization.class);

        assertNotNull(organization2);
        assertEquals(Organization.Role.light, organization2.getRole());

        String json3 = "{role:'regular'}";
        Organization organization3 = gson.fromJson(json3, Organization.class);

        assertNotNull(organization3);
        assertEquals(Organization.Role.regular, organization3.getRole());
    }

    /**
     * Verifies that a {@link Organization.Type} enum can be parsed from a JSON
     * string, using the Gson library.
     * 
     * <pre>
     * 
     * 1. Describe a simple Organization object as a JSON string. Make sure it
     *      has a 'type' attribute.
     * 
     * 2. Parse the JSON string to an Organization instance.
     * 
     * 3. Verify that the 'type' attribute has been parsed successfully as an
     *      enum value.
     * 
     * </pre>
     */
    public void testTypeEnumCanBeParsedFromJson() {
        Gson gson = new Gson();

        String json1 = "{type:'free'}";
        Organization organization1 = gson.fromJson(json1, Organization.class);

        assertNotNull(organization1);
        assertEquals(Organization.Type.free, organization1.getType());

        String json2 = "{type:'sponsored'}";
        Organization organization2 = gson.fromJson(json2, Organization.class);

        assertNotNull(organization2);
        assertEquals(Organization.Type.sponsored, organization2.getType());

        String json3 = "{type:'premium'}";
        Organization organization3 = gson.fromJson(json3, Organization.class);

        assertNotNull(organization3);
        assertEquals(Organization.Type.premium, organization3.getType());
    }

    /**
     * Verifies that the {@link Organization.Role} enum returns the correct
     * value.
     * 
     * <pre>
     * 
     * 1. Just do it.
     * 
     * </pre>
     */
    public void testRoleEnumGivesExpectedValueOf() {
        assertEquals(Organization.Role.admin, Organization.Role.valueOf("admin"));
        assertEquals(Organization.Role.light, Organization.Role.valueOf("light"));
        assertEquals(Organization.Role.regular, Organization.Role.valueOf("regular"));
    }

    /**
     * Verifies that the {@link Organization.Type} enum returns the correct
     * value.
     * 
     * <pre>
     * 
     * 1. Just do it.
     * 
     * </pre>
     */
    public void testTypeEnumGivesExpectedValueOf() {
        assertEquals(Organization.Type.free, Organization.Type.valueOf("free"));
        assertEquals(Organization.Type.sponsored, Organization.Type.valueOf("sponsored"));
        assertEquals(Organization.Type.premium, Organization.Type.valueOf("premium"));
    }
}
