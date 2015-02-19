package com.podio.sdk.domain;

/**
 * Represents the domain object for the data of a {@link Reference}
 * TODO Figure out what kind of DTO domain object this actually is.
 * @author rabie
 */
public class Data {
    private Excerpt excerpt;

    public Excerpt getExcerpt() {
        return excerpt;
    }

    public static class Excerpt {
        private String text;
        private String label;

        public String getText() {
            return text;
        }

        public String getLabel() {
            return label;
        }
    }
}
