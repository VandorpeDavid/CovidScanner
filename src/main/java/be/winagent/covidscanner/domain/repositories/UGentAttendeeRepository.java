package be.fkgent.election.domain.repositories;

import be.fkgent.election.domain.models.Event;
import be.fkgent.election.domain.models.UGentAttendee;
import be.fkgent.election.domain.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UGentAttendeeRepository extends CrudRepository<UGentAttendee, Long> {
    default boolean existsByUserAndEvent(User user, Event event) {
        return existsByUserIdAndEventId(user.getId(), event.getId());
    }

    boolean existsByUserIdAndEventId(long userId, long eventId);
}
