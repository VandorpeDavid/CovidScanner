package be.winagent.wish.controllers.forms.models;

import be.winagent.wish.validators.ContactDetailsRequired;
import be.winagent.wish.validators.HasContactDetails;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@ContactDetailsRequired
public class ExternalAttendeeForm implements HasContactDetails {
    @Email
    private String email;

    private String phone;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;
}
