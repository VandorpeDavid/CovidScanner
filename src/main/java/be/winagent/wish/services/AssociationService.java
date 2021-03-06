package be.winagent.wish.services;

import be.winagent.wish.domain.models.Association;

import java.util.List;
import java.util.Optional;

public interface AssociationService {
    List<Association> all();
    Optional<Association> find(long id);
    Optional<Association> findByAbbreviation(String abbreviation);
    Association create(Association association);
    Association update(Association association);
    boolean addAdmin(Association association, String username);
    boolean removeAdmin(Association association, String username);
}
