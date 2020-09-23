package be.winagent.wish.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * @author davdorpe
 * @since 0.0.1
 */
@Configuration
@ConfigurationProperties(prefix="ugent.ldap")
@Getter
@Setter
public class LDAPConfiguration {
    private String url;

    private String userDn;

    private String password;

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(url);
        contextSource.setUserDn(userDn);
        contextSource.setPassword(password);
        return contextSource;
    }
}
