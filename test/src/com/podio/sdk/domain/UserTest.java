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
import java.util.Date;
import java.util.List;

import android.test.AndroidTestCase;

import com.google.gson.Gson;

/**
 * @author László Urszuly
 */
public class UserTest extends AndroidTestCase {

    /**
     * Verifies that the members of the {@link User} class can be initialized by
     * parsing JSON.
     * 
     * <pre>
     * 
     * 1. Describe a User domain object as a JSON string.
     * 
     * 2. Create an instance from the JSON string.
     * 
     * 3. Verify that the parsed object has the expected values for
     *      its members.
     * 
     * </pre>
     */
    public void testUserCanBeCreatedFromJson() {
        String json = new StringBuilder("{")
                .append("user_id:1,")
                .append("flags:['god'],")
                .append("mails:[{}],")
                .append("status:'active',")
                .append("activated_on:'2014-07-10 13:45:16',")
                .append("created_on:'2014-07-10 13:45:57',")
                .append("locale:'LOCALE',")
                .append("mail:'MAIL',")
                .append("timezone:'TIMEZONE'")
                .append("}").toString();

        Gson gson = new Gson();
        User user = gson.fromJson(json, User.class);

        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals(true, user.hasFlags(User.Flag.god));
        assertEquals(false, user.hasFlags(User.Flag.undefined));

        List<User.Email> emails = user.getEmails();
        assertNotNull(emails);
        assertEquals(1, emails.size());
        assertEquals(User.Status.active, user.getStatus());

        assertEquals("2014-07-10 13:45:16", user.getActivatedDateString());
        Date activatedDate = user.getActivatedDate();
        Calendar activatedCalendar = Calendar.getInstance();
        activatedCalendar.setTime(activatedDate);
        assertNotNull(activatedDate);
        assertEquals(2014, activatedCalendar.get(Calendar.YEAR));
        assertEquals(Calendar.JULY, activatedCalendar.get(Calendar.MONTH));
        assertEquals(10, activatedCalendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(13, activatedCalendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(45, activatedCalendar.get(Calendar.MINUTE));
        assertEquals(16, activatedCalendar.get(Calendar.SECOND));

        assertEquals("2014-07-10 13:45:57", user.getCreatedDateString());

        Date createdDate = user.getCreatedDate();
        Calendar createdCalendar = Calendar.getInstance();
        createdCalendar.setTime(createdDate);
        assertNotNull(createdDate);
        assertEquals(2014, createdCalendar.get(Calendar.YEAR));
        assertEquals(Calendar.JULY, createdCalendar.get(Calendar.MONTH));
        assertEquals(10, createdCalendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(13, createdCalendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(45, createdCalendar.get(Calendar.MINUTE));
        assertEquals(57, createdCalendar.get(Calendar.SECOND));

        assertEquals("LOCALE", user.getLocale());
        assertEquals("MAIL", user.getEmailAddress());
        assertEquals("TIMEZONE", user.getTimezone());
    }

    /**
     * Verifies that the members of the {@link User} class can be initialized to
     * default by instantiation.
     * 
     * <pre>
     * 
     * 1. Create a new User object instance.
     * 
     * 3. Verify that the members have the default values.
     * 
     * </pre>
     */
    public void testUserCanBeCreatedFromInstantiation() {
        User user = User.newInstance();
        assertNotNull(user);
        assertEquals(-1, user.getId());
        assertEquals(false, user.hasFlags(User.Flag.god));
        assertEquals(false, user.hasFlags(User.Flag.undefined));

        List<User.Email> emails = user.getEmails();
        assertNotNull(emails);
        assertEquals(0, emails.size());
        assertEquals(User.Status.undefined, user.getStatus());
        assertEquals(null, user.getActivatedDateString());
        assertEquals(null, user.getActivatedDate());
        assertEquals(null, user.getCreatedDateString());
        assertEquals(null, user.getCreatedDate());
        assertEquals(null, user.getLocale());
        assertEquals(null, user.getEmailAddress());
        assertEquals(null, user.getTimezone());
    }

    /**
     * Verifies that a {@link User.Flag} enum can be parsed from a JSON string.
     * 
     * <pre>
     * 
     * 1. Describe a simple User object as a JSON string. Make sure it
     *      has a 'flags' attribute.
     * 
     * 2. Parse the JSON string to a User instance.
     * 
     * 3. Verify that the 'flats' attribute has been parsed successfully.
     * 
     * </pre>
     */
    public void testFlagEnumCanBeParsedFromJson() {
        Gson gson = new Gson();
        User user;

        user = gson.fromJson("{flags:['god']}", User.class);
        assertNotNull(user);
        assertEquals(true, user.hasFlags(User.Flag.god));
        assertEquals(false, user.hasFlags(User.Flag.out_of_office));

        user = gson.fromJson("{flags:['god', 'out_of_office']}", User.class);
        assertNotNull(user);
        assertEquals(true, user.hasFlags(User.Flag.god));
        assertEquals(true, user.hasFlags(User.Flag.out_of_office));
        assertEquals(true, user.hasFlags(User.Flag.god, User.Flag.out_of_office));
        assertEquals(false, user.hasFlags(User.Flag.api_manager, User.Flag.out_of_office));
        assertEquals(false, user.hasFlags(User.Flag.api_manager, User.Flag.bulletin_author));

        user = gson.fromJson("{}", User.class);
        assertNotNull(user);
        assertEquals(false, user.hasFlags(User.Flag.god));
        assertEquals(false, user.hasFlags(User.Flag.undefined));

        user = gson.fromJson("{flags:['fdafadsfads']}", User.class);
        assertNotNull(user);
        assertEquals(false, user.hasFlags(User.Flag.god));
        assertEquals(false, user.hasFlags(User.Flag.undefined));
    }

    /**
     * Verifies that a {@link User.Status} enum can be parsed from a JSON
     * string.
     * 
     * <pre>
     * 
     * 1. Describe a simple User object as a JSON string. Make sure it
     *      has a 'status' attribute.
     * 
     * 2. Parse the JSON string to a User instance.
     * 
     * 3. Verify that the 'status' attribute has been parsed successfully as an
     *      enum value.
     * 
     * </pre>
     */
    public void testStatusEnumCanBeParsedFromJson() {
        Gson gson = new Gson();
        User user;

        user = gson.fromJson("{status:'blocked'}", User.class);
        assertNotNull(user);
        assertEquals(User.Status.blocked, user.getStatus());

        user = gson.fromJson("{}", User.class);
        assertNotNull(user);
        assertEquals(User.Status.undefined, user.getStatus());

        user = gson.fromJson("{status:'fdafadsfads'}", User.class);
        assertNotNull(user);
        assertEquals(User.Status.undefined, user.getStatus());
    }

    /**
     * Verifies that the {@link User.Flag} enum returns the correct value.
     * 
     * <pre>
     * 
     * 1. Just do it.
     * 
     * </pre>
     */
    public void testValueOfFlag() {
        assertEquals(User.Flag.god, User.Flag.valueOf("god"));
        assertEquals(User.Flag.bulletin_author, User.Flag.valueOf("bulletin_author"));
        assertEquals(User.Flag.app_store_manager, User.Flag.valueOf("app_store_manager"));
        assertEquals(User.Flag.experiment_manager, User.Flag.valueOf("experiment_manager"));
        assertEquals(User.Flag.org_viewer, User.Flag.valueOf("org_viewer"));
        assertEquals(User.Flag.org_manager, User.Flag.valueOf("org_manager"));
        assertEquals(User.Flag.api_manager, User.Flag.valueOf("api_manager"));
        assertEquals(User.Flag.extension_manager, User.Flag.valueOf("extension_manager"));
        assertEquals(User.Flag.tnol_manager, User.Flag.valueOf("tnol_manager"));
        assertEquals(User.Flag.seo_manager, User.Flag.valueOf("seo_manager"));
        assertEquals(User.Flag.out_of_office, User.Flag.valueOf("out_of_office"));
        assertEquals(User.Flag.sales, User.Flag.valueOf("sales"));
        assertEquals(User.Flag.sales_default, User.Flag.valueOf("sales_default"));
        assertEquals(User.Flag.sales_large, User.Flag.valueOf("sales_large"));
        assertEquals(User.Flag.sales_contract, User.Flag.valueOf("sales_contract"));
        assertEquals(User.Flag.undefined, User.Flag.valueOf("undefined"));

        assertEquals(User.Flag.god, Enum.valueOf(User.Flag.class, "god"));
        assertEquals(User.Flag.bulletin_author, Enum.valueOf(User.Flag.class, "bulletin_author"));
        assertEquals(User.Flag.app_store_manager, Enum.valueOf(User.Flag.class, "app_store_manager"));
        assertEquals(User.Flag.experiment_manager, Enum.valueOf(User.Flag.class, "experiment_manager"));
        assertEquals(User.Flag.org_viewer, Enum.valueOf(User.Flag.class, "org_viewer"));
        assertEquals(User.Flag.org_manager, Enum.valueOf(User.Flag.class, "org_manager"));
        assertEquals(User.Flag.api_manager, Enum.valueOf(User.Flag.class, "api_manager"));
        assertEquals(User.Flag.extension_manager, Enum.valueOf(User.Flag.class, "extension_manager"));
        assertEquals(User.Flag.tnol_manager, Enum.valueOf(User.Flag.class, "tnol_manager"));
        assertEquals(User.Flag.seo_manager, Enum.valueOf(User.Flag.class, "seo_manager"));
        assertEquals(User.Flag.out_of_office, Enum.valueOf(User.Flag.class, "out_of_office"));
        assertEquals(User.Flag.sales, Enum.valueOf(User.Flag.class, "sales"));
        assertEquals(User.Flag.sales_default, Enum.valueOf(User.Flag.class, "sales_default"));
        assertEquals(User.Flag.sales_large, Enum.valueOf(User.Flag.class, "sales_large"));
        assertEquals(User.Flag.sales_contract, Enum.valueOf(User.Flag.class, "sales_contract"));
        assertEquals(User.Flag.undefined, Enum.valueOf(User.Flag.class, "undefined"));
    }

    /**
     * Verifies that the {@link User.Status} enum returns the correct value.
     * 
     * <pre>
     * 
     * 1. Just do it.
     * 
     * </pre>
     */
    public void testValueOfStatus() {
        assertEquals(User.Status.inactive, User.Status.valueOf("inactive"));
        assertEquals(User.Status.active, User.Status.valueOf("active"));
        assertEquals(User.Status.deleted, User.Status.valueOf("deleted"));
        assertEquals(User.Status.blocked, User.Status.valueOf("blocked"));
        assertEquals(User.Status.blacklisted, User.Status.valueOf("blacklisted"));
        assertEquals(User.Status.undefined, User.Status.valueOf("undefined"));

        assertEquals(User.Status.inactive, Enum.valueOf(User.Status.class, "inactive"));
        assertEquals(User.Status.active, Enum.valueOf(User.Status.class, "active"));
        assertEquals(User.Status.deleted, Enum.valueOf(User.Status.class, "deleted"));
        assertEquals(User.Status.blocked, Enum.valueOf(User.Status.class, "blocked"));
        assertEquals(User.Status.blacklisted, Enum.valueOf(User.Status.class, "blacklisted"));
        assertEquals(User.Status.undefined, Enum.valueOf(User.Status.class, "undefined"));
    }

}
