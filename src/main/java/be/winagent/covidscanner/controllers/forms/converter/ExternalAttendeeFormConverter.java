package be.winagent.covidscanner.controllers.forms.converter;

import be.winagent.covidscanner.controllers.forms.models.ExternalAttendeeForm;
import be.winagent.covidscanner.domain.models.ExternalAttendee;
import org.springframework.stereotype.Component;

@Component
public class ExternalAttendeeFormConverter implements Converter<ExternalAttendee, ExternalAttendeeForm> {
    @Override
    public ExternalAttendee update(ExternalAttendee externalAttendee, ExternalAttendeeForm externalAttendeeForm) {
        externalAttendee.setEmail(externalAttendeeForm.getEmail());
        externalAttendee.setFirstName(externalAttendeeForm.getFirstName());
        externalAttendee.setLastName(externalAttendeeForm.getLastName());
        externalAttendee.setPhone(externalAttendeeForm.getPhone());
        return externalAttendee;
    }

    @Override
    public ExternalAttendee build(ExternalAttendeeForm externalAttendeeForm) {
        return update(new ExternalAttendee(), externalAttendeeForm);
    }
}
