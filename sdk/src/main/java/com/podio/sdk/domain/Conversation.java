
package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

import java.util.Date;

/**
 * A Java representation of the ConversationDTO API domain object.
 *
 */
public class Conversation {

    /**
     * Type definition of a conversation.
     */
    public static enum Type {
        direct, group, unknown
    }

    /**
     * The data structure describing the "reply to conversation" content as the API expects it.
     */
    public static class Reply {
        @SuppressWarnings("unused")
        private final String text;

        @SuppressWarnings("unused")
        private final String embed_url;

        @SuppressWarnings("unused")
        private final long[] file_ids;

        public Reply(String text, String embedUrl, long[] fileIds) {
            this.text = text;
            this.embed_url = embedUrl;
            this.file_ids = fileIds;
        }
    }

    /**
     * The data structure describing the "new conversation" data as the API expects it.
     */
    public static class Create {
        @SuppressWarnings("unused")
        private final String subject;

        @SuppressWarnings("unused")
        private final String text;

        @SuppressWarnings("unused")
        private final String embed_url;

        @SuppressWarnings("unused")
        private final long[] file_ids;

        @SuppressWarnings("unused")
        private final long[] participants;

        public Create(String subject, String text, String link, long[] participants, long[] fileIds) {
            this.subject = subject;
            this.text = text;
            this.embed_url = link;
            this.file_ids = Utils.notEmpty(fileIds) ? fileIds : new long[0];
            this.participants = Utils.notEmpty(participants) ? participants : new long[0];
        }
    }

    /**
     * This class describes the conversation event data.
     */
    // TODO: split this into multiple classes based on the event type
    public static class Data {
        // message.data
        private final Long message_id = null;
        private final File[] files = null;
        private final String text = null;
        private final String created_on = null;
        // embed?

        // participant_add / participant_leave
        private final Long user_id = null;
        private final String name = null;
        private final Long space_id = null;
        private final Long profile_id = null;
        private final Long org_id = null;
        private final String last_seen_on = null;
        private final String link = null;
        // avatar
        // private final String type = null;
        // image

        // TODO: add fields for other event types
        // ...

        public File[] getFiles() {
            return files.clone();
        }

        public String text() {
            return text;
        }

        public Date getCreatedOnDateTime() {
            return Utils.parseDateTimeUtc(created_on);
        }

        public String getCreatedOnDateTimeString() {
            return created_on;
        }

        public long getMessageId() {
            return Utils.getNative(message_id, -1L);
        }

        // data for participant_add/leave
        public Long getUserId() {
            return user_id;
        }
        public String getName() {
            return name;
        }
        public Long getSpaceId() {
            return space_id;
        }
        public Long getProfileId() {
            return profile_id;
        }
        public Long getOrgId() {
            return org_id;
        }
        public Date getLastSeenOn() {
            return Utils.parseDateTimeUtc(last_seen_on);
        }
        public String getLink() {
            return link;
        }
    }

    /**
     * This class describes the conversation event meta data.
     */
    public static class Event {
        private final Long event_id = null;
        private final Byline created_by = null;
        private final String created_on = null;
        private final String action = null;
        private final Conversation.Data data = null;

        public long getEventId() {
            return Utils.getNative(event_id, -1L);
        }

        public Byline getCreatedBy() {
            return created_by;
        }

        public long getCreatedById() {
            return created_by != null ? created_by.getId() : -1L;
        }

        public Date getCreatedDate() {
            return Utils.parseDateTimeUtc(created_on);
        }

        public String getCreatedDateString() {
            return created_on;
        }

        public String getAction() {
            return action;
        }

        public long getMessageId() {
            return data != null ? Utils.getNative(data.message_id, -1L) : -1L;
        }

        public File[] getMessageFiles() {
            return data != null && data.files != null ? data.files.clone() : new File[0];
        }

        public String getMessageText() {
            return data != null ? data.text : null;
        }

        public Date getMessageCreatedDate() {
            return data != null ? Utils.parseDateTimeUtc(data.created_on) : null;
        }

        public String getMessageCreatedDateString() {
            return data != null ? data.created_on : null;
        }

        public Data getData() {
            return data;
        }
    }

    /**
     * This class describes a push event sent by the API when a new event has been created in a
     * conversation.
     */
    public static class PushNewEvent extends PushEvent {

        private static class Settings {
            private final Boolean sound = null;
            private final Boolean popup = null;
        }

        private static class Data {
            private final Long conversation_id = null;
            private final Long event_id = null;
            private final String action = null;
            private final String text = null;
            private final Settings settings = null;
            private final Conversation.Data data = null;
            private final String created_on = null;
            private final Byline created_by = null;
            private final Integer unread_count = null;
            private final Integer total_unread_count = null;
        }

        private final PushNewEvent.Data data = null;

        public String action() {
            return data != null ? data.action : null;
        }

        public Byline byline() {
            return data != null ? data.created_by : null;
        }

        public long conversationId() {
            return data != null ? Utils.getNative(data.conversation_id, -1L) : -1L;
        }

        public Date createdOnDateTime() {
            return data != null ? Utils.parseDateTimeUtc(data.created_on) : null;
        }

        public String createdOnDateTimeString() {
            return data != null ? data.created_on : null;
        }

        public Conversation.Data data() {
            return data != null ? data.data : null;
        }

        public boolean doPlaySound() {
            return data != null && data.settings != null && Utils.getNative(data.settings.sound, false);
        }

        public boolean doShowPopup() {
            return data != null && data.settings != null && Utils.getNative(data.settings.popup, false);
        }

        public long eventId() {
            return data != null ? Utils.getNative(data.event_id, -1L) : -1L;
        }

        public String excerpt() {
            return data != null ? data.text : null;
        }

        public int unreadMessagesCountInConversation() {
            return data != null ? Utils.getNative(data.unread_count, -1) : -1;
        }

        public int unreadMessagesCountInTotal() {
            return data != null ? Utils.getNative(data.total_unread_count, -1) : -1;
        }

    }

    /**
     * This class describes a push event sent by the API when a conversation has been marked as
     * read.
     */
    public static class PushRead extends PushEvent {

        private static class Data {
            /**
             * The id of the conversation that was changed.
             */
            private final Long conversation_id = null;

            /**
             * The number of unread events on the conversation.
             */
            private final Integer unread_count = null;

            /**
             * The total number of unread events.
             */
            private final Integer total_unread_count = null;
        }

        private final PushRead.Data data = null;

        public long conversationId() {
            return data != null ? Utils.getNative(data.conversation_id, -1L) : -1L;
        }

        public int unreadMessagesCountInConversation() {
            return data != null ? Utils.getNative(data.unread_count, -1) : -1;
        }

        public int unreadMessagesCountInTotal() {
            return data != null ? Utils.getNative(data.total_unread_count, -1) : -1;
        }
    }

    /**
     * This class describes a push event sent by the API when all conversations has been marked as
     * read.
     */
    public static class PushReadAll extends PushEvent {
    }

    /**
     * This class describes a push event sent by the API when a conversation has been starred.
     */
    public static class PushStarred extends PushEvent {

        private static class Data {
            /**
             * The id of the conversation that was changed.
             */
            private final Long conversation_id = null;
        }

        private final PushStarred.Data data = null;

        public long conversationId() {
            return data != null ? Utils.getNative(data.conversation_id, -1L) : -1L;
        }
    }

    /**
     * This class describes a push event sent by the API when the number of starred conversation has
     * changed.
     */
    public static class PushStarredCount extends PushEvent {

        private static class Data {
            /**
             * The number of starred conversation events.
             */
            private final Integer count = null;
        }

        private final PushStarredCount.Data data = null;

        public int count() {
            return data != null ? Utils.getNative(data.count, -1) : -1;
        }
    }

    /**
     * This class describes a push event sent by the API when a conversation has been marked as
     * unread.
     */
    public static class PushUnread extends PushEvent {

        private static class Data {
            /**
             * The id of the conversation that was changed.
             */
            private final Long conversation_id = null;

            /**
             * The number of unread events on the conversation.
             */
            private final Integer unread_count = null;

            /**
             * The total number of unread events.
             */
            private final Integer total_unread_count = null;
        }

        private final PushUnread.Data data = null;

        public long conversationId() {
            return data != null ? Utils.getNative(data.conversation_id, -1L) : -1L;
        }

        public int unreadMessagesCountInConversation() {
            return data != null ? Utils.getNative(data.unread_count, -1) : -1;
        }

        public int unreadMessagesCountInTotal() {
            return data != null ? Utils.getNative(data.total_unread_count, -1) : -1;
        }
    }

    /**
     * This class describes a push event sent by the API when the number of unread conversations has
     * changed.
     */
    public static class PushUnreadCount extends PushEvent {

        private static class Data {
            /**
             * The number of unread conversation events.
             */
            private final Integer count = null;
        }

        private final PushUnreadCount.Data data = null;

        public int count() {
            return data != null ? Utils.getNative(data.count, -1) : -1;
        }
    }

    /**
     * This class describes a push event sent by the API when a conversation has been unstarred.
     */
    public static class PushUnstarred extends PushEvent {

        private static class Data {
            /**
             * The id of the conversation that was changed.
             */
            private final Long conversation_id = null;
        }

        private final PushUnstarred.Data data = null;

        public long conversationId() {
            return data != null ? Utils.getNative(data.conversation_id, -1L) : -1L;
        }
    }

    private final Long conversation_id = null;
    private final Boolean pinned = null;
    private final Boolean starred = null;
    private final Boolean unread = null;
    private final Integer unread_count = null;
    private final Byline created_by = null;
    private final Profile[] participants = null;
    private final String created_on = null;
    private final String last_event_on = null;
    private final String link = null;
    private final String type = null; // See "Type" enum.
    private final String subject = null;
    private final String excerpt = null;
    private final Presence presence = null;
    private final Push push = null;

    public long getConversationId() {
        return Utils.getNative(conversation_id, -1L);
    }

    public Date getCreatedDate() {
        return Utils.parseDateTimeUtc(created_on);
    }

    public String getCreatedDateString() {
        return created_on;
    }

    public Byline getCreator() {
        return created_by;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public boolean hasUnreadMessages() {
        return Utils.getNative(unread, false);
    }

    public boolean isPinned() {
        return Utils.getNative(pinned, false);
    }

    public boolean isStarred() {
        return Utils.getNative(starred, false);
    }

    public Date getLastEventDate() {
        return Utils.parseDateTimeUtc(last_event_on);
    }

    public String getLastEventDateString() {
        return last_event_on;
    }

    public String getLink() {
        return link;
    }

    public Profile[] getParticipants() {
        return participants != null ? participants.clone() : new Profile[0];
    }

    public Presence getPresenceMetaData() {
        return presence;
    }

    public Push getPushMetaData() {
        return push;
    }

    public String getSubject() {
        return subject;
    }

    public Type getType() {
        try {
            return Type.valueOf(type);
        } catch (NullPointerException e) {
            return Type.unknown;
        } catch (IllegalArgumentException e) {
            return Type.unknown;
        }
    }

    public int getUnreadMessagesCount() {
        return Utils.getNative(unread_count, -1);
    }

}
