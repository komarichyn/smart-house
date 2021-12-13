package org.komarichyn.ss;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "org.komarichyn.ss.database.sql.repository")
public class SensorServiceConfiguration {

}
