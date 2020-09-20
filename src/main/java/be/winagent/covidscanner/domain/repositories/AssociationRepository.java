package be.winagent.covidscanner.domain.repositories;

import be.winagent.covidscanner.domain.models.Association;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssociationRepository extends CrudRepository<Association, Long> {
    @NonNull
    List<Association> findAll();
    Optional<Association> findByAbbreviation(String abbreviation);
    Optional<Association> findById(long id);
}
