
package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

/**
 * @author László Urszuly
 */
public class Embed {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Embed embed = (Embed) o;

        return !(embed_id != null ? !embed_id.equals(embed.embed_id) : embed.embed_id != null);

    }

    @Override
    public int hashCode() {
        return embed_id != null ? embed_id.hashCode() : 0;
    }

    public static enum Type {
        link, undefined
    }

    public static class Create {
        private final String url;

        public Create(String url) {
            this.url = url;
        }
    }

    private final Integer embed_height = null;
    private final Integer embed_width = null;
    private final Long embed_id = null;
    private final String description = null;
    private final String embed_html = null;
    private final String hostname = null;
    private final String original_url = null;
    private final String resolved_url = null;
    private final String title = null;
    private final String url;
    private final Type type = null;

    public Embed(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public int getHeight() {
        return Utils.getNative(embed_height, 0);
    }

    public String getHtml() {
        return embed_html;
    }

    public long getId() {
        return Utils.getNative(embed_id, -1L);
    }

    public int getWidth() {
        return Utils.getNative(embed_width, 0);
    }

    public String getHostName() {
        return hostname;
    }

    public String getOriginalUrl() {
        return original_url;
    }

    public String getResolvedUrl() {
        return resolved_url;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public Type getType() {
        return type != null ? type : Type.undefined;
    }
}
