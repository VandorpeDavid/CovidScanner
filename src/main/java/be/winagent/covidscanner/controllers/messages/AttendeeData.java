package be.fkgent.election.controllers.messages;

import be.fkgent.election.domain.models.Attendee;
import be.fkgent.election.domain.models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendeeData {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String username;

    public AttendeeData(Attendee attendee) {
        this.firstName = attendee.getFirstName();
        this.lastName = attendee.getLastName();
        this.email = attendee.getEmail();
        this.phone = attendee.getPhone();
        this.username = attendee.getCasName();
    }

    public AttendeeData(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }
}
