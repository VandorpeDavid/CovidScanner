package be.winagent.covidscanner.domain.repositories;

import be.winagent.covidscanner.domain.models.UGentAttendee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendeeRepository extends CrudRepository<UGentAttendee, Long> {
}
