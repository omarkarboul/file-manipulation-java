package fr.gopartner.liquibase.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application.file.generate")
public class FileProperties {
    private String nameFileLowLevel;
    private String nameFileHighLevel;
}
