package be.winagent.covidscanner.controllers.forms.models;

import be.winagent.covidscanner.validators.ContactDetailsRequired;
import be.winagent.covidscanner.validators.HasContactDetails;
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
