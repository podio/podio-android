package com.podio.sdk.domain;

/**
 * Created by ianhenry on 6/28/16.
 */
public class Label {
    private final Long label_id = null;
    private final String text = null;
    private final String color = null;

    public static class CreateData {
        @SuppressWarnings("unused")
        private final String text;
        @SuppressWarnings("unused")
        private String color;

        public CreateData(String text) {
            this.text = text;
        }

        public void setColor(String color){
            this.color = color;
        }
    }

    public Long getLabelId() {
        return label_id;
    }

    public String getText() {
        return text;
    }

    public String getColor() {
        return color;
    }
}