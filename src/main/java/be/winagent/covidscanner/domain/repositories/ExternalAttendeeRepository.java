package be.winagent.covidscanner.domain.repositories;

import be.winagent.covidscanner.domain.models.ExternalAttendee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalAttendeeRepository extends CrudRepository<ExternalAttendee, Long> {
}
