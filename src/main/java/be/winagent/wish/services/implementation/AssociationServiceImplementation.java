package be.winagent.wish.services.implementation;

import be.winagent.wish.domain.models.Association;
import be.winagent.wish.domain.repositories.AssociationRepository;
import be.winagent.wish.services.AssociationService;
import be.winagent.wish.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AssociationServiceImplementation implements AssociationService {
    private final AssociationRepository associationRepository;
    private final UserService userService;

    @Override
    public List<Association> all() {
        return associationRepository.findAll();
    }

    @Override
    public Optional<Association> find(long id) {
        return associationRepository.findById(id);
    }

    @Override
    public Optional<Association> findByAbbreviation(String abbreviation) {
        return associationRepository.findByAbbreviation(abbreviation);
    }

    @Override
    public Association create(Association association) {
        return associationRepository.save(association);
    }

    @Override
    public Association update(Association association) {
        return associationRepository.save(association);
    }

    @Override
    public boolean addAdmin(Association association, String username) {
        return userService.findOrCreateUserByUsername(username)
                .map((admin) -> {
                    association.getAdmins().add(admin);
                    associationRepository.save(association);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public boolean removeAdmin(Association association, String username) {
        boolean changed = association.getAdmins().removeIf((admin) -> admin.getUsername().equals(username));
        if (changed) {
            associationRepository.save(association);
        }
        return changed;
    }
}
