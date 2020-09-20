package be.fkgent.election.domain.repositories;

import be.fkgent.election.domain.models.UGentAttendee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendeeRepository extends CrudRepository<UGentAttendee, Long> {
}
