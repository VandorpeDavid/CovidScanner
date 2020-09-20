package be.fkgent.election.controllers.forms.converter;

import be.fkgent.election.controllers.forms.models.EventForm;
import be.fkgent.election.domain.models.Event;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventConverter implements BidirectionalConverter<Event, EventForm> {
    @Override
    public Event update(Event event, EventForm eventForm) {
        event.setName(eventForm.getName());
        return event;
    }

    @Override
    public Event build(EventForm eventForm) {
        return update(new Event(), eventForm);
    }

    @Override
    public EventForm reverseUpdate(EventForm target, Event source) {
        target.setName(source.getName());
        return target;
    }

    @Override
    public EventForm reverseBuild(Event source) {
        return reverseUpdate(new EventForm(), source);
    }
}