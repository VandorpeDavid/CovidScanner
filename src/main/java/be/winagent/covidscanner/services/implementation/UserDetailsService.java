package be.fkgent.election.services.implementation;


import be.fkgent.election.domain.models.User;
import be.fkgent.election.services.UserService;
import lombok.AllArgsConstructor;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserDetailsService extends AbstractCasAssertionUserDetailsService {
    private final UserService userService;


    @Override
    protected UserDetails loadUserDetails(Assertion assertion) throws UsernameNotFoundException {
        AttributePrincipal principal = assertion.getPrincipal();
        String username = principal.getName();
        if (!StringUtils.hasText(username)) {
            throw new UsernameNotFoundException("Unable to retrieve username from CAS assertion");
        }

        List<GrantedAuthority> applicationGrantedAuthorities = new ArrayList<>();

        User user = userService.findOrCreateUserByUsername(username).orElseThrow();
        if(user.isAdmin()) {
            applicationGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        applicationGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new FKUserDetails(user, applicationGrantedAuthorities);
    }
}