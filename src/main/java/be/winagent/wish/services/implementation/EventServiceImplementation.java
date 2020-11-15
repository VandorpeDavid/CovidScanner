package be.winagent.wish.services.implementation;


import be.winagent.wish.domain.models.UGentAttendee;
import be.winagent.wish.domain.models.Event;
import be.winagent.wish.domain.models.ExternalAttendee;
import be.winagent.wish.domain.models.User;
import be.winagent.wish.domain.repositories.EventRepository;
import be.winagent.wish.domain.repositories.ExternalAttendeeRepository;
import be.winagent.wish.domain.repositories.UGentAttendeeRepository;
import be.winagent.wish.services.EventService;
import be.winagent.wish.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@Service
@AllArgsConstructor
public class EventServiceImplementation implements EventService {
    private final EventRepository eventRepository;
    private final ExternalAttendeeRepository externalAttendeeRepository;
    private final UGentAttendeeRepository ugentAttendeeRepository;
    private final UserService userService;
    private final EntityManager entityManager;

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
        if (ugentAttendeeRepository.existsByUserAndEvent(attendee, event)) {
            return false;
        }
        UGentAttendee UGentAttendeeModel = new UGentAttendee();
        UGentAttendeeModel.setEvent(event);
        UGentAttendeeModel.setUser(attendee);
        ugentAttendeeRepository.save(UGentAttendeeModel);
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
        LocalDateTime cutoff = LocalDateTime.now().minusDays(14);
        List<User> users = eventRepository.findAllByCreatedIsBefore(cutoff)
                .filter((event) -> event.getAttendees().stream().allMatch((attendee) -> attendee.getEntered().isBefore(cutoff)))
                .map(this::removeEvent)
                .flatMap(Collection::stream)
                .filter(not(User::isAdmin)) // Never remove admins
                .distinct()
                .collect(Collectors.toList());

        entityManager.flush();

        userService.deleteAll(
                users.stream()
                        .filter((user) -> user.getAttended().isEmpty())
                        .filter((user) -> user.getAssociations().isEmpty())
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    protected List<User> removeEvent(Event event) {
        List<User> users = event.getAttendees().stream()
                .filter(UGentAttendee.class::isInstance)
                .map(UGentAttendee.class::cast)
                .map(UGentAttendee::getUser)
                .collect(Collectors.toList());

        eventRepository.delete(event);
        return users;
    }
}
