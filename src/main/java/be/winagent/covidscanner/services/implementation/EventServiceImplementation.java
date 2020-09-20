package be.winagent.covidscanner.services.implementation;


import be.winagent.covidscanner.domain.models.UGentAttendee;
import be.winagent.covidscanner.domain.models.Event;
import be.winagent.covidscanner.domain.models.ExternalAttendee;
import be.winagent.covidscanner.domain.models.User;
import be.winagent.covidscanner.domain.repositories.AttendeeRepository;
import be.winagent.covidscanner.domain.repositories.EventRepository;
import be.winagent.covidscanner.domain.repositories.ExternalAttendeeRepository;
import be.winagent.covidscanner.domain.repositories.UGentAttendeeRepository;
import be.winagent.covidscanner.services.EventService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventServiceImplementation implements EventService {
    private final EventRepository eventRepository;
    private final AttendeeRepository attendeeRepository;
    private final ExternalAttendeeRepository externalAttendeeRepository;
    private final UGentAttendeeRepository ugentAttendeeRepository;

    @Override
    public Optional<Event> find(long id) {
        return eventRepository.findById(id);
    }

    @Override
    public Event create(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event update(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public boolean register(Event event, User attendee) {
        if(ugentAttendeeRepository.existsByUserAndEvent(attendee, event)) {
            return false;
        }
        UGentAttendee UGentAttendeeModel = new UGentAttendee();
        UGentAttendeeModel.setEvent(event);
        UGentAttendeeModel.setUser(attendee);
        attendeeRepository.save(UGentAttendeeModel);
        return true;
    }

    @Override
    public ExternalAttendee register(Event event, ExternalAttendee attendee) {
        attendee.setEvent(event);
        return externalAttendeeRepository.save(attendee);
    }

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24) // Daily
    @Transactional
    public void removeOldEvents() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(15);
        eventRepository.findAllByCreatedIsBeforeAndDeletedIsFalse(cutoff)
                .filter((event) -> event.getAttendees().stream().allMatch((attendee) -> attendee.getEntered().isBefore(cutoff)))
                .peek((event) -> event.setDeleted(true))
                .forEach(eventRepository::save);
    }
}
