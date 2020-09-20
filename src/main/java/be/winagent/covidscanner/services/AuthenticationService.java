package be.fkgent.election.services;

import be.fkgent.election.domain.models.User;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface AuthenticationService {
    Optional<User> getUser(Authentication authentication);
}
