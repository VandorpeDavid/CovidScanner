package be.winagent.wish.controllers.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendeeScan {
    private AttendeeData attendee;
    private boolean duplicate;
}
