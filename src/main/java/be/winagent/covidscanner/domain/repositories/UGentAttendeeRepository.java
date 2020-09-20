package be.winagent.covidscanner.domain.repositories;

import be.winagent.covidscanner.domain.models.Event;
import be.winagent.covidscanner.domain.models.UGentAttendee;
import be.winagent.covidscanner.domain.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UGentAttendeeRepository extends CrudRepository<UGentAttendee, Long> {
    default boolean existsByUserAndEvent(User user, Event event) {
        return existsByUserIdAndEventId(user.getId(), event.getId());
    }

    boolean existsByUserIdAndEventId(long userId, long eventId);
}
