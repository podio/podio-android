
package com.podio.sdk.domain;

import android.test.AndroidTestCase;

import com.google.gson.Gson;

public class ApplicationConfigurationTest extends AndroidTestCase {

    public void testApplicationConfigurationCanBeCreatedFromJson() {
        String json = new StringBuilder("{")
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
                .append("}").toString();

        Gson gson = new Gson();
        Application.Configuration configuration = gson.fromJson(json, Application.Configuration.class);

        assertNotNull(configuration);
        assertEquals(true, configuration.allowsAttachments());
        assertEquals(true, configuration.allowsComments());
        assertEquals(true, configuration.allowsEdit());
        assertEquals(true, configuration.isApproved());
        assertEquals(true, configuration.hasFiveStarRating());
        assertEquals(true, configuration.hasRsvpState());
        assertEquals(true, configuration.hasThumbsVoting());
        assertEquals(true, configuration.hasYesNoVoting());
        assertEquals("DESCRIPTION", configuration.getDescription());
        assertEquals("EXTERNALID", configuration.getExternalId());
        assertEquals("FIVESTARLABEL", configuration.getFiveStarLabel());
        assertEquals("ICON", configuration.getIconName());
        assertEquals("ITEMNAME", configuration.getItemName());
        assertEquals("NAME", configuration.getName());
        assertEquals("RSVPLABEL", configuration.getRsvpLabel());
        assertEquals("THUMBSLABEL", configuration.getThumbsLabel());
        assertEquals("YESNOLABEL", configuration.getYesNoLabel());
        assertEquals(Application.Type.meeting, configuration.getType());
        assertEquals(true, configuration.allowsCreate());
        assertEquals(true, configuration.allowsTags());
        assertEquals(true, configuration.hasDisabledNotifications());
        assertEquals(true, configuration.showAppItemId());
        assertEquals(true, configuration.hasSilentCreates());
        assertEquals(true, configuration.hasSilentEdits());
        assertEquals(1, configuration.getAppItemIdPaddingCount());
        assertEquals("PREFIX", configuration.getAppItemIdPrefix());
        assertEquals("USAGE", configuration.getUsageInfo());
        assertEquals(Application.View.badge, configuration.getDefaultViewType());
    }

    public void testApplicationConfigurationCanBeCreatedFromInstantiation() {
        Application application = Application.newInstance();
        Application.Configuration configuration = application.getConfiguration();
        assertNotNull(configuration);
        assertEquals(false, configuration.allowsAttachments());
        assertEquals(false, configuration.allowsComments());
        assertEquals(false, configuration.allowsEdit());
        assertEquals(false, configuration.isApproved());
        assertEquals(false, configuration.hasFiveStarRating());
        assertEquals(false, configuration.hasRsvpState());
        assertEquals(false, configuration.hasThumbsVoting());
        assertEquals(false, configuration.hasYesNoVoting());
        assertEquals(null, configuration.getDescription());
        assertEquals(null, configuration.getExternalId());
        assertEquals(null, configuration.getFiveStarLabel());
        assertEquals(null, configuration.getIconName());
        assertEquals(null, configuration.getItemName());
        assertEquals(null, configuration.getName());
        assertEquals(null, configuration.getRsvpLabel());
        assertEquals(null, configuration.getThumbsLabel());
        assertEquals(null, configuration.getYesNoLabel());
        assertEquals(Application.Type.undefined, configuration.getType());
        assertEquals(false, configuration.allowsCreate());
        assertEquals(false, configuration.allowsTags());
        assertEquals(false, configuration.hasDisabledNotifications());
        assertEquals(false, configuration.showAppItemId());
        assertEquals(false, configuration.hasSilentCreates());
        assertEquals(false, configuration.hasSilentEdits());
        assertEquals(0, configuration.getAppItemIdPaddingCount());
        assertEquals(null, configuration.getAppItemIdPrefix());
        assertEquals(null, configuration.getUsageInfo());
        assertEquals(Application.View.undefined, configuration.getDefaultViewType());
    }
}
