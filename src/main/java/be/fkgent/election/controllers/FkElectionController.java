package be.fkgent.election.controllers;

import be.fkgent.election.controllers.exceptions.NotFoundException;
import be.fkgent.election.domain.models.Association;
import be.fkgent.election.domain.models.Event;
import be.fkgent.election.domain.models.User;
import be.fkgent.election.services.AssociationService;
import be.fkgent.election.services.AuthenticationService;
import be.fkgent.election.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;
import static java.util.function.Predicate.not;

public abstract class FkElectionController {
    @Autowired
    private AssociationService associationService;

    @Autowired
    private EventService eventService;

    @Autowired
    private AuthenticationService authenticationService;

    @ModelAttribute(binding = false)
    public Association addAssociation(@RequestParam(required = false, name = "association") String abbreviation) {
        return Optional.ofNullable(abbreviation)
                .map(
                        (abbrev) -> associationService
                                .findByAbbreviation(abbrev)
                                .orElseThrow(NotFoundException::new)
                )
                .orElse(null);
    }

    @ModelAttribute(binding = false)
    public Event addEvent(@RequestParam(required = false, name = "event") Long eventId) {
        return Optional.ofNullable(eventId)
                .map(
                        (id) -> eventService
                                .find(id)
                                .filter(not(Event::isDeleted))
                                .orElseThrow(NotFoundException::new)
                )
                .orElse(null);
    }

    @ModelAttribute(binding = false, name = "currentUser")
    public User addCurrentUser(Authentication authentication) {
        return authenticationService.getUser(authentication).orElse(null);
    }
}
