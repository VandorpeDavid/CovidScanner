package be.winagent.wish.services;

import be.winagent.wish.domain.models.*;
import java.util.Optional;

public interface EventService {
    Optional<Event> find(long id);
    Event create(Event event);
    Event update(Event event);
    boolean register(Event event, User attendee);
    ExternalAttendee register(Event event, ExternalAttendee attendee);
}
