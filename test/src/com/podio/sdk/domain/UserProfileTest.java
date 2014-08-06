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
import java.util.List;
import java.util.TimeZone;

import android.test.AndroidTestCase;

import com.google.gson.Gson;

/**
 * @author László Urszuly
 */
public class UserProfileTest extends AndroidTestCase {

    /**
     * Verifies that the members of the {@link User.Profile} class can be
     * initialized by parsing JSON.
     * 
     * <pre>
     * 
     * 1. Describe a User Profile domain object as a JSON string.
     * 
     * 2. Create an instance from the JSON string.
     * 
     * 3. Verify that the parsed object has the expected values for
     *      its members.
     * 
     * </pre>
     */
    public void testUserProfileCanBeCreatedFromJson() {
        // private final File image = null;
        // private final Integer avatar = null;
        // private final Integer org_id = null;
        // private final Integer profile_id = null;
        // private final Integer space_id = null;
        // private final Integer user_id = null;
        // private final List<String> location = null;
        // private final List<String> mail = null;
        // private final List<String> phone = null;
        // private final List<Right> rights = null;
        // private final List<String> title = null;
        // private final String about = null;
        // private final String external_id = null;
        // private final String link = null;
        // private final String last_seen_on = null;
        // private final String name = null;
        String json = new StringBuilder("{")
                .append("image:{},")
                .append("avatar:1,")
                .append("org_id:1,")
                .append("profile_id:1,")
                .append("space_id:1,")
                .append("user_id:1,")
                .append("location:['LOCATION'],")
                .append("mail:['MAIL'],")
                .append("phone:['PHONE'],")
                .append("rights:['view'],")
                .append("title:['TITLE'],")
                .append("about:'ABOUT',")
                .append("external_id:'EXTERNALID',")
                .append("link:'LINK',")
                .append("last_seen_on:'2014-07-10 14:13:39',")
                .append("name:'NAME'")
                .append("}").toString();

        Gson gson = new Gson();
        User.Profile profile = gson.fromJson(json, User.Profile.class);

        assertNotNull(profile);
        assertNotNull(profile.getImage());
        assertEquals(1, profile.getAvatar());
        assertEquals(1, profile.getOrganizationId());
        assertEquals(1, profile.getId());
        assertEquals(1, profile.getWorkspaceId());
        assertEquals(1, profile.getUserId());

        List<String> locations = profile.getLocations();
        assertNotNull(locations);
        assertEquals(1, locations.size());
        assertEquals("LOCATION", locations.get(0));

        List<String> emails = profile.getEmailAddresses();
        assertNotNull(emails);
        assertEquals(1, emails.size());
        assertEquals("MAIL", emails.get(0));

        List<String> phones = profile.getPhoneNumbers();
        assertNotNull(phones);
        assertEquals(1, phones.size());
        assertEquals("PHONE", phones.get(0));

        assertEquals(true, profile.hasRights(Right.view));
        assertEquals(false, profile.hasRights(Right.update));

        List<String> titles = profile.getTitles();
        assertNotNull(titles);
        assertEquals(1, titles.size());
        assertEquals("TITLE", titles.get(0));

        assertEquals("ABOUT", profile.getAbout());
        assertEquals("EXTERNALID", profile.getExternalId());
        assertEquals("LINK", profile.getLink());
        assertEquals("2014-07-10 14:13:39", profile.getLastSeenDateString());

        Calendar lastSeenCalendar = Calendar.getInstance();
        lastSeenCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        lastSeenCalendar.setTime(profile.getLastSeenDate());

        assertEquals(2014, lastSeenCalendar.get(Calendar.YEAR));
        assertEquals(Calendar.JULY, lastSeenCalendar.get(Calendar.MONTH));
        assertEquals(10, lastSeenCalendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(14, lastSeenCalendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(13, lastSeenCalendar.get(Calendar.MINUTE));
        assertEquals(39, lastSeenCalendar.get(Calendar.SECOND));

        assertEquals("NAME", profile.getName());
    }

    /**
     * Verifies that the {@link User.Profile#getLocations()},
     * {@link User.Profile#getEmailAddresses()},
     * {@link User.Profile#getPhoneNumbers()} methods doesn't return null, even
     * if the JSON didn't specify their corresponding attributes.
     * 
     * <pre>
     * 
     * 1. Describe a User Profile in JSON without any attributes.
     * 
     * 2. Parse the JSON into a new object.
     * 
     * 3. Verify that a non-null object is returned when asking for the
     *      application configuration.
     * 
     * </pre>
     */
    public void testListsNeverNull() {
        Gson gson = new Gson();
        User.Profile profile = gson.fromJson("{}", User.Profile.class);

        assertEquals(false, profile.hasRights(Right.view));
        assertEquals(0, profile.getLocations().size());
        assertEquals(0, profile.getEmailAddresses().size());
        assertEquals(0, profile.getPhoneNumbers().size());
        assertEquals(0, profile.getTitles().size());
    }
}
