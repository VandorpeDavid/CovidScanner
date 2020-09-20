package be.fkgent.election.services.implementation;

import be.fkgent.election.domain.models.User;
import be.fkgent.election.services.AuthenticationService;
import be.fkgent.election.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationServiceImplementation implements AuthenticationService {
    private final UserService userService;
    @Override
    public Optional<User> getUser(Authentication authentication) {
        return Optional.ofNullable(authentication)
                .map(Authentication::getPrincipal)
                .filter((x) -> x instanceof FKUserDetails)
                .map((x) -> (FKUserDetails) x)
                .map(FKUserDetails::getUser)
                .map(User::getId)
                .flatMap(userService::find); // Update with latest data
    }
}
