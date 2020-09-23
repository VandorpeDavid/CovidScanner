package be.winagent.wish.domain.repositories;

import be.winagent.wish.domain.models.ExternalAttendee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalAttendeeRepository extends CrudRepository<ExternalAttendee, Long> {
}
