
package com.podio.sdk.domain;

import android.test.AndroidTestCase;

import com.google.gson.Gson;

public class EmbedTest extends AndroidTestCase {

    public void testEmbedCanBeCreatedFromJson() {
        String json = new StringBuilder("{")
                .append("embed_height:1,")
                .append("embed_id:1,")
                .append("embed_width:1,")
                .append("description:'DESCRIPTION',")
                .append("embed_html:'EMBEDHTML',")
                .append("hostname:'HOSTNAME',")
                .append("original_url:'ORIGINALURL',")
                .append("resolved_url:'RESOLVEDURL',")
                .append("title:'TITLE',")
                .append("url:'URL'")
                .append("}").toString();

        Gson gson = new Gson();
        Embed embed = gson.fromJson(json, Embed.class);

        assertNotNull(embed);
        assertEquals(1, embed.getHeight());
        assertEquals(1, embed.getId());
        assertEquals(1, embed.getWidth());
        assertEquals("DESCRIPTION", embed.getDescription());
        assertEquals("EMBEDHTML", embed.getHtml());
        assertEquals("HOSTNAME", embed.getHostName());
        assertEquals("ORIGINALURL", embed.getOriginalUrl());
        assertEquals("RESOLVEDURL", embed.getResolvedUrl());
        assertEquals("TITLE", embed.getTitle());
        assertEquals("URL", embed.getUrl());
    }


    public void testEmbedCanBeCreatedFromInstantiation() {
        Embed embed = new Embed("URL");
        assertNotNull(embed);
        assertEquals(0, embed.getHeight());
        assertEquals(-1, embed.getId());
        assertEquals(0, embed.getWidth());
        assertEquals(null, embed.getDescription());
        assertEquals(null, embed.getHtml());
        assertEquals(null, embed.getHostName());
        assertEquals(null, embed.getOriginalUrl());
        assertEquals(null, embed.getResolvedUrl());
        assertEquals(null, embed.getTitle());
        assertEquals("URL", embed.getUrl());
    }

    public void testTypeEnumCanBeParsedFromJson() {
        Gson gson = new Gson();
        Embed embed;

        embed = gson.fromJson("{type:'link'}", Embed.class);
        assertEquals(Embed.Type.link, embed.getType());

        embed = gson.fromJson("{}", Embed.class);
        assertEquals(Embed.Type.undefined, embed.getType());

        embed = gson.fromJson("{type:'fdafadsfads'}", Embed.class);
        assertEquals(Embed.Type.undefined, embed.getType());
    }

    public void testValueOfType() {
        assertEquals(Embed.Type.link, Embed.Type.valueOf("link"));
        assertEquals(Embed.Type.undefined, Embed.Type.valueOf("undefined"));

        assertEquals(Embed.Type.link, Enum.valueOf(Embed.Type.class, "link"));
        assertEquals(Embed.Type.undefined, Enum.valueOf(Embed.Type.class, "undefined"));
    }

}
