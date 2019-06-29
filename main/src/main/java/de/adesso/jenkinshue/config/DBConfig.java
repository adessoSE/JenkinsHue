package de.adesso.jenkinshue.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan("de.adesso.jenkinshue.entity")
@EnableJpaRepositories("de.adesso.jenkinshue.repository")
public class DBConfig {

}
