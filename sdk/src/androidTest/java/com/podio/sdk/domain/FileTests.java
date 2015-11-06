
package com.podio.sdk.domain;

import android.test.AndroidTestCase;

import com.google.gson.Gson;

public class FileTests extends AndroidTestCase {

    public void testEmbedCanBeCreatedFromJson() {
        String json = new StringBuilder("{")
                .append("file_id:1,")
                .append("size:1,")
                .append("description:'DESCRIPTION',")
                .append("hosted_by:'HOSTEDBY',")
                .append("hosted_by_humanized_name:'HOSTEDBYHUMANIZEDNAME',")
                .append("link:'LINK',")
                .append("link_target:'LINKTARGET',")
                .append("mimetype:'MIMETYPE',")
                .append("name:'NAME',")
                .append("perma_link:'PERMALINK',")
                .append("thumbnail_link:'THUMBNAILLINK'")
                .append("}").toString();

        Gson gson = new Gson();
        File file = gson.fromJson(json, File.class);

        assertNotNull(file);
        assertEquals(1, file.getId());
        assertEquals(1, file.getSize());
        assertEquals("DESCRIPTION", file.getDescription());
        assertEquals("HOSTEDBY", file.getHostName());
        assertEquals("HOSTEDBYHUMANIZEDNAME", file.getHumanizedHostName());
        assertEquals("LINK", file.getLink());
        assertEquals("LINKTARGET", file.getLinkTarget());
        assertEquals("MIMETYPE", file.getMimeType());
        assertEquals("NAME", file.getName());
        assertEquals("PERMALINK", file.getPermaLink());
        assertEquals("THUMBNAILLINK", file.getThumbnailLink());
    }


    public void testEmbedCanBeCreatedFromInstantiation() {
        File file = new File(1);

        assertNotNull(file);
        assertEquals(1, file.getId());
        assertEquals(0, file.getSize());
        assertEquals(null, file.getDescription());
        assertEquals(null, file.getHostName());
        assertEquals(null, file.getHumanizedHostName());
        assertEquals(null, file.getLink());
        assertEquals(null, file.getLinkTarget());
        assertEquals(null, file.getMimeType());
        assertEquals(null, file.getName());
        assertEquals(null, file.getPermaLink());
        assertEquals(null, file.getThumbnailLink());
    }

}
