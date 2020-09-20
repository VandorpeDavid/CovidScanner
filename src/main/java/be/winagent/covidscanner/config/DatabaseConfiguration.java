package be.winagent.covidscanner.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

@Configuration
@ConfigurationProperties(prefix="spring.datasource")
@Getter
@Setter
public class DatabaseConfiguration {
    private String url;
    private String username;
    private String password;

    @Value("${secret-dbPassword}")
    private String azurePassword;

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(url);
        dataSourceBuilder.username(username);
        if(StringUtils.isEmpty(password)) {
            dataSourceBuilder.password(azurePassword);
        } else {
            dataSourceBuilder.password(password);
        }
        return dataSourceBuilder.build();
    }

}
