package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.volley.VolleyProvider;

/**
 * @author rabie
 */
public class TaskProvider extends VolleyProvider {

    static class Path extends Filter {

        Path() {
            super("task");
        }

    }

    /**
     * This enum is used to query the Podio API for a set of tasks ordered based
     * on a certain grouping
     * 
     * @author rabie
     */
    public static enum Grouping {

        DUE_DATE("due_date"),
        CREATED_BY("created_by"),
        RESPONSIBLE("responsible"),
        APP("app"),
        SPACE("space"),
        ORG("org");

        private String group = null;

        private Grouping(String group) {
            this.group = group;
        }

        public String getGroupValue() {
            return this.group;
        }
    }

}
