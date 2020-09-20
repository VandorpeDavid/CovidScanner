package be.fkgent.election.services.implementation;

import be.fkgent.election.domain.models.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;


public class FKUserDetails extends org.springframework.security.core.userdetails.User {
    private final User user;
    private static final String noPassword = "We don't do passwords here";

    public FKUserDetails() {
        this("Anonymous");
    }

    public FKUserDetails(String username) {
        super(username, noPassword, new ArrayList<>());
        user = null;
    }

    public FKUserDetails(User user, Collection<GrantedAuthority> authorities) {
        super(
                user.getEmail(),
                noPassword,
                authorities
        );
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
