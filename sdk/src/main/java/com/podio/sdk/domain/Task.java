package com.podio.sdk.domain;

import com.google.gson.annotations.SerializedName;
import com.podio.sdk.domain.data.Data;
import com.podio.sdk.internal.Utils;
import com.podio.sdk.provider.TaskProvider.GetTaskFilter.Grouping;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 */
public class Task implements Data {

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

    /**
     * A class representing the new task the client wants to create.
     */
    public static class CreateData {

        public enum ResponsibleType {
            user,
            space,
            mail
        }

        public static class Responsible {

            @SuppressWarnings("unused")
            private ResponsibleType type;

            public Responsible(ResponsibleType type) {
                this.type = type;
            }
        }

        public static class DefaultResponsible extends Responsible {

            @SuppressWarnings("unused")
            private long id;

            /**
             * @param type
             *         user or space. If you wish to send in mail you have to use MailResponsible.
             * @param id
             */
            public DefaultResponsible(ResponsibleType type, long id) {
                super(type);
                this.id = id;
            }

        }

        public static class MailResponsible extends Responsible {

            @SuppressWarnings("unused")
            private String id;

            public MailResponsible(String mail) {
                super(ResponsibleType.mail);
                this.id = mail;
            }
        }

        @SuppressWarnings("unused")
        private String text;
        @SuppressWarnings("unused")
        private String description;
        @SuppressWarnings("unused")
        private String due_on;
        @SuppressWarnings("unused")
        private List<Responsible> responsible;
        @SuppressWarnings("unused")
        @SerializedName("private")
        private Boolean isPrivate;
        @SuppressWarnings("unused")
        private ReferenceType ref_type;
        @SuppressWarnings("unused")
        private Long refId;
        @SuppressWarnings("unused")
        private List<Long> file_ids;
        @SuppressWarnings("unused")
        private Reminder reminder;

        public CreateData(String text) {
            this.text = text;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setDueOn(Date dueOn, boolean hasTime) {
            if (hasTime) {
                this.due_on = dueOn != null ? Utils.formatDateTimeUtc(dueOn) : null;
            } else {
                this.due_on = dueOn != null ? Utils.formatDateDefault(dueOn) : null;
            }
        }

        public void setResponsible(List<Responsible> responsible) {
            this.responsible = responsible;
        }

        public void setIsPrivate(boolean isPrivate) {
            this.isPrivate = isPrivate;
        }

        /**
         * This is only applicable when updating a task. When creating new new task with a reference
         * the reference is part of the task create url path.
         *
         * @param refType
         * @param refId
         */
        public void setRefeference(ReferenceType refType, long refId) {
            this.ref_type = refType;
            this.refId = refId;
        }

        public void setFileIds(List<Long> file_ids) {
            this.file_ids = file_ids;
        }

        public void setReminder(Reminder reminder) {
            this.reminder = reminder;
        }
    }

    public long getTaskId() {
        return Utils.getNative(task_id, -1L);
    }

    /**
     * @return returns a timezone independent date component formatted as 2014-11-25
     */
    public String getDueDate() {
        return due_date;
    }

    /**
     * @return returns the raw string given from the API, representing time formatted as 16:04:00 in
     * the timezone of the user's web account
     */
    public String getDueTime() {
        return due_time;
    }

    /**
     * @return returns the {@link Reminder} object or null if no reminder was set
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
     * @return Depending on the given {@link Grouping}, this method will return the raw JSON string
     * of which group this task is task is part of. If for example you requested tasks DUE_DATE
     * grouping the this method would return a simple JSON structure of a string which either is
     * 'overdue', 'today', 'tomorrow', 'upcoming' or 'later'. Consult Podio's developers page for
     * full details.
     */
    public String getGroup() {
        return group;
    }

    /**
     * @return returns the due date of this task in the phone's local time. It accounts for the fact
     * that due on may or may not have a usable time component as described by hasTime().
     */
    public Date getDueOn() {
        if (hasDueTime()) {
            return Utils.parseDateTimeUtc(due_on);
        } else {
            return Utils.parseDateUtc(due_on);
        }
    }

    /**
     * @return returns true if you can rely on having a time component in the in the UTC due date
     * (i.e. due on) Date object, otherwise false. This is needed as tasks are allowed to have a
     * date component set, with no specific time, which means that the time component of the Date
     * object returned by getDueOn() is NOT valid
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
