package be.fkgent.election.controllers.messages;

import be.fkgent.election.domain.models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendeeScan {
    private AttendeeData attendee;
    private boolean duplicate;
}
