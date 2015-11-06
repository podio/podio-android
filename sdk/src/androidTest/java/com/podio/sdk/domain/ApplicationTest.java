
package com.podio.sdk.domain;

import android.test.AndroidTestCase;

import com.google.gson.Gson;
import com.podio.sdk.domain.field.Field;

public class ApplicationTest extends AndroidTestCase {

    public void testApplicationCanBeCreatedFromJson() {
        String json = new StringBuilder("{")
                .append("app_id:1,")
                .append("config:{},")
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

        Field[] fields = application.getTemplate();
        assertNotNull(fields);
        assertEquals(0, fields.length);

        assertEquals("LINK", application.getLink());
        assertEquals("LINKADD", application.getAddLink());
        assertNotNull(application.getOwner());
        assertEquals(true, application.hasAllRights(Right.view));
        assertEquals(false, application.hasAllRights(Right.delete));
        assertEquals(1L, application.getSpaceId());
        assertEquals(Application.Status.inactive, application.getStatus());
        assertEquals("URL", application.getUrl());
        assertEquals("URLADD", application.getAddUrl());
        assertEquals("URLLABEL", application.getUrlLabel());
        assertEquals(true, application.isPinned());
        assertEquals(true, application.isSubscribed());
        assertEquals(1L, application.getCurrentRevisionId());
        assertEquals(1L, application.getDefaultViewId());
        assertEquals(1L, application.getOriginAppId());
        assertEquals(1L, application.getOriginAppRevisionId());
        assertEquals("MAILBOX", application.getMailbox());
        assertEquals("TOKEN", application.getToken());
    }


    public void testApplicationCanBeCreatedFromInstantiation() {
        Application application = Application.newInstance();

        assertNotNull(application);
        assertEquals(-1, application.getAppId());

        Application.Configuration config = application.getConfiguration();
        assertNotNull(config);

        Field[] fields = application.getTemplate();
        assertNotNull(fields);
        assertEquals(0, fields.length);

        assertEquals(null, application.getLink());
        assertEquals(null, application.getAddLink());
        assertEquals(null, application.getOwner());
        assertEquals(false, application.hasAllRights(Right.view));
        assertEquals(-1L, application.getSpaceId());
        assertEquals(Application.Status.undefined, application.getStatus());
        assertEquals(null, application.getUrl());
        assertEquals(null, application.getAddUrl());
        assertEquals(null, application.getUrlLabel());
        assertEquals(false, application.isPinned());
        assertEquals(false, application.isSubscribed());
        assertEquals(-1L, application.getCurrentRevisionId());
        assertEquals(-1L, application.getDefaultViewId());
        assertEquals(-1L, application.getOriginAppId());
        assertEquals(-1L, application.getOriginAppRevisionId());
        assertEquals(null, application.getMailbox());
        assertEquals(null, application.getToken());
    }

    public void testConfigurationAndFieldsNeverNull() {
        Gson gson = new Gson();
        String json = "{}";
        Application application = gson.fromJson(json, Application.class);
        Field[] fields = application.getTemplate();
        assertNotNull(fields);
        assertEquals(0, fields.length);
        assertNotNull(application.getConfiguration());
    }

    public void testHasRightsDefaultsToFalse() {
        Gson gson = new Gson();
        String json = "{}";
        Application application = gson.fromJson(json, Application.class);
        assertEquals(false, application.hasAllRights(Right.view));
    }


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


    public void testValueOfType() {
        assertEquals(Application.Type.standard, Application.Type.valueOf("standard"));
        assertEquals(Application.Type.meeting, Application.Type.valueOf("meeting"));
        assertEquals(Application.Type.undefined, Application.Type.valueOf("undefined"));

        assertEquals(Application.Type.standard, Enum.valueOf(Application.Type.class, "standard"));
        assertEquals(Application.Type.meeting, Enum.valueOf(Application.Type.class, "meeting"));
        assertEquals(Application.Type.undefined, Enum.valueOf(Application.Type.class, "undefined"));
    }


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
