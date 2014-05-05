package com.podio.sdk.domain;

import com.podio.sdk.domain.helper.UserInfo;


public final class Application {

    public static final class Configuration {
        public final String type = null;
        public final String name = null;
        public final String item_name = null;
        public final String description = null;
        public final String usage = null;
        public final String external_id = null;
        public final String icon = null;
        public final Integer icon_id = null;
    }

    public static final class IntegrationInfo {
        private IntegrationInfo() {
            // hide the constructor.
        }
    }

    public final Long app_id = null;
    public final Configuration config = null;
    public final IntegrationInfo integration = null;
    public final String link = null;
    public final String link_add = null;
    public final Long original = null;
    public final Integer original_revision = null;
    public final UserInfo owner = null;
    public final Boolean pinned = null;
    public final String[] rights = null;
    public final Space space = null;
    public final Long space_id = null;
    public final String status = null;
    public final Boolean subscribed = null;
    public final String url = null;
    public final String url_add = null;
    public final String url_label = null;

    private Application() {
        // Hide the constructor.
    }
}
