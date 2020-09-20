package be.winagent.covidscanner.services;

import be.winagent.covidscanner.domain.models.User;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface AuthenticationService {
    Optional<User> getUser(Authentication authentication);
}
