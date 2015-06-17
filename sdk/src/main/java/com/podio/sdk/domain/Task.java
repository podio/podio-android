package com.podio.sdk.domain;

import com.google.gson.annotations.SerializedName;
import com.podio.sdk.internal.Utils;
import com.podio.sdk.provider.TaskProvider.GetTaskFilter.Grouping;

import java.util.Collection;
import java.util.Date;

/**
 * @author rabie
 */
public class Task {

    private final Long task_id = null;
    private final String due_date = null;
    private final String due_time = null;
    private final String due_on = null;
    private final Reminder reminder = null;
    @SerializedName("private")
    private final Boolean is_private = null;
    private final String text = null;
    private final String description = null;
    private final String group = null;
    @SerializedName("ref")
    private final Reference reference = null;
    private final Profile responsible = null;
    private final Status status = null;
    private final Collection<Comment> comments = null;

    public static enum Status {
        completed,
        active,
        deleted
    }

    public long getTaskId() {
        return Utils.getNative(task_id, -1L);
    }

    /**
     * @return returns a timezone independent date component formatted as
     *         2014-11-25
     */
    public String getDueDate() {
        return due_date;
    }

    /**
     * @return returns the raw string given from the API, representing time
     *         formatted as 16:04:00 in the timezone of the user's web account
     */
    public String getDueTime() {
        return due_time;
    }

    /**
     * @return returns the {@link Reminder} object or null if no reminder was
     *         set
     */
    public Reminder getReminder() {
        return reminder;
    }

    public Profile getResponsible() {
        return responsible;
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
     * @return returns the due date of this task in the phone's local time. It
     *         accounts for the fact that due on may or may not have a usable
     *         time component as described by hasTime().
     */
    public Date getDueOn() {
        if (hasDueTime()) {
            return Utils.parseDateTimeUtc(due_on);
        } else {
            return Utils.parseDateUtc(due_on);
        }
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

    public Reference getReference() {
        return reference;
    }

    public Status getStatus() {
        return status;
    }

    public Collection<Comment> getComments() {
        return comments;
    }
}
