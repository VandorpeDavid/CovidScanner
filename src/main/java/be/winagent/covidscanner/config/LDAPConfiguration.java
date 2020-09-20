package be.fkgent.election.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * @author davdorpe
 * @since 0.0.1
 */
@Configuration
public class LDAPConfiguration {
    @Bean
    public LdapContextSource contextSource(LDAPProperties properties) {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(properties.getUrl());
        contextSource.setUserDn(properties.getUserDn());
        contextSource.setPassword(properties.getPassword());
        return contextSource;
    }
}
