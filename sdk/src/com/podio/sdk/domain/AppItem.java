package com.podio.sdk.domain;

public class AppItem {

    public static final class Config {
        public String type;
        public String name;
        public String item_name;
        public String description;
        public String usage;
        public String external_id;
        public String icon;
    }

    public long app_id;
    public long space_id;
    public String status;
    public Config config;
    public String[] rights;

    private AppItem() {
    }
}
