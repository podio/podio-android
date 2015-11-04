
package com.podio.sdk.domain;

/**
 * A Java representation of the ItemParticipationDTO API domain object.
 *
 * @author Tobias Lindberg
 */
public class ItemParticipation {

    public enum MeetingParticipantStatus {
        invited,
        accepted,
        declined,
        tentative
    }

    private final String status;

    public ItemParticipation(MeetingParticipantStatus meetingParticipantStatus) {
        this.status = meetingParticipantStatus.name();
    }

    public MeetingParticipantStatus getStatus() {
        try {
            return MeetingParticipantStatus.valueOf(status);
        } catch (NullPointerException e) {
            return MeetingParticipantStatus.invited;
        } catch (IllegalArgumentException e) {
            return MeetingParticipantStatus.invited;
        }
    }
}
