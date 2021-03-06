package be.winagent.wish.domain.repositories;

import be.winagent.wish.domain.models.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
    public Stream<Event> findAllByCreatedIsBefore(LocalDateTime before);
}
