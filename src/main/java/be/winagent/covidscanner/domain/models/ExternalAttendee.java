package be.winagent.covidscanner.domain.models;

import be.winagent.covidscanner.validators.ContactDetailsRequired;
import be.winagent.covidscanner.validators.HasContactDetails;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@ContactDetailsRequired
public class ExternalAttendee extends Attendee implements HasContactDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Email
    private String email;

    private String phone;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @Override
    public String getCasName() {
        return null;
    }

    @Override
    public String getContactDetails() {
        if (StringUtils.isEmpty(getEmail())) {
            return String.format("Tel: %s", getPhone());
        }

        if (StringUtils.isEmpty(getPhone())) {
            return String.format("Email: %s", getEmail());
        }

        return String.format("Email: %s, Tel: %s", getEmail(), getPhone());
    }
}
