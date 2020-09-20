package be.fkgent.election.domain.repositories;

import be.fkgent.election.domain.models.ExternalAttendee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalAttendeeRepository extends CrudRepository<ExternalAttendee, Long> {
}
