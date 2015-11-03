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
import com.podio.sdk.domain.Organization.Segment;

public class OrganizationTest extends AndroidTestCase {

    /**
     * Verifies that the {@link Organization#getDomains()} method doesn't return
     * null, even if the JSON didn't specify the 'domains' attribute.
     * 
     * <pre>
     * 
     * 1. Describe an Organization in JSON without a 'domains' attribute.
     * 
     * 2. Parse the JSON into a new Organization object.
     * 
     * 3. Verify that a non-null object is returned when asking for the domains.
     * 
     * </pre>
     */
    public void testDomainsNeverNull() {
        Gson gson = new Gson();
        String json = "{}";
        Organization organization = gson.fromJson(json, Organization.class);

        List<String> domains = organization.getDomains();
        assertNotNull(domains);
        assertEquals(0, domains.size());
    }

    /**
     * Verifies that the {@link Organization#getSpaces()} method doesn't return
     * null, even if the JSON didn't specify the 'spaces' attribute.
     * 
     * <pre>
     * 
     * 1. Describe an Organization in JSON without a 'spaces' attribute.
     * 
     * 2. Parse the JSON into a new Organization object.
     * 
     * 3. Verify that a non-null object is returned when asking for the spaces.
     * 
     * </pre>
     */
    public void testSpacesNeverNull() {
        Gson gson = new Gson();
        String json = "{}";
        Organization organization = gson.fromJson(json, Organization.class);

        List<Space> spaces = organization.getSpaces();
        assertNotNull(spaces);
        assertEquals(0, spaces.size());
    }

    /**
     * Verifies that the {@link Organization#hasRights(Right...)} method
     * defaults to false if no rights are defined in JSON.
     * 
     * <pre>
     * 
     * 1. Describe an Organization in JSON without a 'rights' attribute.
     * 
     * 2. Parse the JSON into a new Organization object.
     * 
     * 3. Verify that false is returned when asking for an arbitrary right.
     * 
     * </pre>
     */
    public void testHasRightsDefaultsToFalse() {
        Gson gson = new Gson();
        String json = "{}";
        Organization organization = gson.fromJson(json, Organization.class);

        assertEquals(false, organization.hasRights(Right.view));
    }

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
                .append("org_id:2,")
                .append("rank:3,")
                .append("rights:['view'],")
                .append("role:'regular',")
                .append("spaces:[],")
                .append("status:'inactive',")
                .append("type:'premium',")
                .append("url:'URL',")
                .append("url_label:'URLLABEL',")
                .append("sales_agent_id:4,")
                .append("user_limit:5,")
                .append("domains:[],")
                .append("segment:'education',")
                .append("created_on:'2014-07-08 13:23:37',")
                .append("created_by:{name:'me'}")
                .append("}").toString();

        Gson gson = new Gson();
        Organization organization = gson.fromJson(json, Organization.class);

        assertNotNull(organization);
        assertEquals(1, organization.getLogoId());
        assertEquals("NAME", organization.getName());
        assertEquals(2, organization.getId());
        assertEquals(3, organization.getRank());
        assertTrue(organization.hasRights(Right.view));
        assertFalse(organization.hasRights(Right.add_file));
        assertEquals(Organization.Role.regular, organization.getRole());
        List<Space> spaces = organization.getSpaces();
        assertNotNull(spaces);
        assertEquals(0, spaces.size());
        assertEquals(Organization.Status.inactive, organization.getStatus());
        assertEquals(Organization.Type.premium, organization.getType());
        assertEquals("URL", organization.getUrl());
        assertEquals("URLLABEL", organization.getUrlLabel());
        assertEquals(4, organization.getSalesAgentId());
        assertEquals(5, organization.getUserLimit());
        List<String> domains = organization.getDomains();
        assertNotNull(domains);
        assertEquals(0, domains.size());
        assertEquals(Segment.education, organization.getSegment());
        assertEquals("2014-07-08 13:23:37", organization.getCreatedDateString());
        assertNotNull(organization.getCreatedDate());
        User user = organization.getCreatedByUser();
        assertNotNull(user);
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

        String json2 = "{}";
        Organization organization2 = gson.fromJson(json2, Organization.class);

        assertNotNull(organization2);
        assertEquals(Organization.Role.undefined, organization2.getRole());

        String json3 = "{role:'dasfsafadsadfda'}";
        Organization organization3 = gson.fromJson(json3, Organization.class);

        assertNotNull(organization3);
        assertEquals(Organization.Role.undefined, organization3.getRole());
    }

    /**
     * Verifies that a {@link Organization.Segment} enum can be parsed from a
     * JSON string, using the Gson library.
     * 
     * <pre>
     * 
     * 1. Describe a simple Organization object as a JSON string. Make sure it
     *      has a 'segment' attribute.
     * 
     * 2. Parse the JSON string to an Organization instance.
     * 
     * 3. Verify that the 'segment' attribute has been parsed successfully as an
     *      enum value.
     * 
     * </pre>
     */
    public void testSegmentEnumCanBeParsedFromJson() {
        Gson gson = new Gson();

        String json1 = "{segment:'education'}";
        Organization organization1 = gson.fromJson(json1, Organization.class);

        assertNotNull(organization1);
        assertEquals(Organization.Segment.education, organization1.getSegment());

        String json2 = "{}";
        Organization organization2 = gson.fromJson(json2, Organization.class);

        assertNotNull(organization2);
        assertEquals(Organization.Segment.undefined, organization2.getSegment());

        String json3 = "{segment:'fdafadsfads'}";
        Organization organization3 = gson.fromJson(json3, Organization.class);

        assertNotNull(organization3);
        assertEquals(Organization.Segment.undefined, organization3.getSegment());
    }

    /**
     * Verifies that a {@link Organization.Status} enum can be parsed from a
     * JSON string, using the Gson library.
     * 
     * <pre>
     * 
     * 1. Describe a simple Organization object as a JSON string. Make sure it
     *      has a 'status' attribute.
     * 
     * 2. Parse the JSON string to an Organization instance.
     * 
     * 3. Verify that the 'status' attribute has been parsed successfully as an
     *      enum value.
     * 
     * </pre>
     */
    public void testStatusEnumCanBeParsedFromJson() {
        Gson gson = new Gson();

        String json1 = "{status:'active'}";
        Organization organization1 = gson.fromJson(json1, Organization.class);

        assertNotNull(organization1);
        assertEquals(Organization.Status.active, organization1.getStatus());

        String json2 = "{}";
        Organization organization2 = gson.fromJson(json2, Organization.class);

        assertNotNull(organization2);
        assertEquals(Organization.Status.undefined, organization2.getStatus());

        String json3 = "{status:'fdafadsfads'}";
        Organization organization3 = gson.fromJson(json3, Organization.class);

        assertNotNull(organization3);
        assertEquals(Organization.Status.undefined, organization3.getStatus());
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

        String json2 = "{}";
        Organization organization2 = gson.fromJson(json2, Organization.class);

        assertNotNull(organization2);
        assertEquals(Organization.Type.undefined, organization2.getType());

        String json3 = "{type:'fdafadsfads'}";
        Organization organization3 = gson.fromJson(json3, Organization.class);

        assertNotNull(organization3);
        assertEquals(Organization.Type.undefined, organization3.getType());
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
    public void testValueOfRole() {
        assertEquals(Organization.Role.admin, Organization.Role.valueOf("admin"));
        assertEquals(Organization.Role.light, Organization.Role.valueOf("light"));
        assertEquals(Organization.Role.regular, Organization.Role.valueOf("regular"));
        assertEquals(Organization.Role.undefined, Organization.Role.valueOf("undefined"));

        assertEquals(Organization.Role.admin, Enum.valueOf(Organization.Role.class, "admin"));
        assertEquals(Organization.Role.light, Enum.valueOf(Organization.Role.class, "light"));
        assertEquals(Organization.Role.regular, Enum.valueOf(Organization.Role.class, "regular"));
        assertEquals(Organization.Role.undefined, Enum.valueOf(Organization.Role.class, "undefined"));
    }

    /**
     * Verifies that the {@link Organization.Segment} enum returns the correct
     * value.
     * 
     * <pre>
     * 
     * 1. Just do it.
     * 
     * </pre>
     */
    public void testValueOfSegment() {
        assertEquals(Organization.Segment.education, Organization.Segment.valueOf("education"));
        assertEquals(Organization.Segment.undefined, Organization.Segment.valueOf("undefined"));

        assertEquals(Organization.Segment.education, Enum.valueOf(Organization.Segment.class, "education"));
        assertEquals(Organization.Segment.undefined, Enum.valueOf(Organization.Segment.class, "undefined"));
    }

    /**
     * Verifies that the {@link Organization.Status} enum returns the correct
     * value.
     * 
     * <pre>
     * 
     * 1. Just do it.
     * 
     * </pre>
     */
    public void testValueOfStatus() {
        assertEquals(Organization.Status.active, Organization.Status.valueOf("active"));
        assertEquals(Organization.Status.deleted, Organization.Status.valueOf("deleted"));
        assertEquals(Organization.Status.inactive, Organization.Status.valueOf("inactive"));
        assertEquals(Organization.Status.undefined, Organization.Status.valueOf("undefined"));

        assertEquals(Organization.Status.active, Enum.valueOf(Organization.Status.class, "active"));
        assertEquals(Organization.Status.deleted, Enum.valueOf(Organization.Status.class, "deleted"));
        assertEquals(Organization.Status.inactive, Enum.valueOf(Organization.Status.class, "inactive"));
        assertEquals(Organization.Status.undefined, Enum.valueOf(Organization.Status.class, "undefined"));
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
    public void testValueOfType() {
        assertEquals(Organization.Type.free, Organization.Type.valueOf("free"));
        assertEquals(Organization.Type.sponsored, Organization.Type.valueOf("sponsored"));
        assertEquals(Organization.Type.premium, Organization.Type.valueOf("premium"));
    }
}
