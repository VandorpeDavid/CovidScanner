package be.winagent.covidscanner.domain.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
public class UGentAttendee extends Attendee {
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Override
    public String getLastName() {
        return user.getLastName();
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getPhone() {
        return null;
    }

    @Override
    public String getCasName() {
        return user.getUsername();
    }

    @Override
    public String getFirstName() {
        return user.getFirstName();
    }

    @Override
    public String getContactDetails() {
        return String.format("Email: %s, Cas: %s", getEmail(), getUser().getUsername());
    }
}
