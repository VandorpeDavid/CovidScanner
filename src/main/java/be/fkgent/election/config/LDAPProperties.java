package be.fkgent.election.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author davdorpe
 * @see LDAPConfiguration
 * @since 0.0.1
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix="ugent.ldap")
public class LDAPProperties {
    private String url;
    private String userDn;
    private String password;
}
