package com.podio.sdk.domain;

import java.util.Date;

import com.google.gson.annotations.SerializedName;
import com.podio.sdk.internal.Utils;
import com.podio.sdk.provider.TaskProvider.Grouping;

/**
 * @author rabie
 */
public class Task {

    private final Long task_id = null;
    private final String due_date = null;
    private final String due_time = null;
    private final String due_on = null;
    private final Integer reminder = null;
    @SerializedName("private")
    private final Boolean is_private = null;
    private final String text = null;
    private final String description = null;
    private final String group = null;

    public long getTaskId() {
        return Utils.getNative(task_id, 0);
    }

    public String getDueDate() {
        return due_date;
    }

    // TODO is this description correct?
    /**
     * @return returns the raw string given from the API, denoting the local
     *         time based on the web
     */
    public String getDueTime() {
        return due_time;
    }

    public int getReminder() {
        return Utils.getNative(reminder, -1);
    }

    public boolean isPrivate() {
        return Utils.getNative(is_private, false);
    }

    public String getText() {
        return text;
    }

    public String getDescription() {
        return description;
    }

    /**
     * @return Depending on the given {@link Grouping}, this method will return
     *         the raw JSON string of which group this task is task is part of.
     *         If for example you requested tasks DUE_DATE grouping the this
     *         method would return a simple JSON structure of a string which
     *         either is 'overdue', 'today', 'tomorrow', 'upcoming' or 'later'.
     *         Consult Podio's developers page for full details.
     */
    public String getGroup() {
        return group;
    }

    /**
     * @return returns the due date of this task in UTC
     */
    public Date getDueOn() {
        return Utils.parseDateTime(due_on);
    }

    /**
     * @return returns true if you can rely on having a time component in the in
     *         the UTC due date (i.e. due on) Date object, otherwise false. This
     *         is needed as tasks are allowed to have a date component set, with
     *         no specific time, which means that the time component of the Date
     *         object returned by getDueOn() is NOT valid
     */
    public boolean hasDueTime() {
        return due_time != null ? true : false;
    }

    public String getDueOnString() {
        return due_on;
    }

}
