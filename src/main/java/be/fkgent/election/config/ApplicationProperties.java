package be.fkgent.election.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("application")
@Configuration
@Data
public class ApplicationProperties {
    private String baseUrl;
}
