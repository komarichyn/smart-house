package org.komarichyn.ss;

import org.komarichyn.mqtt.MqttClientConfiguration;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "org.komarichyn.ss.database.sql.repository")
@Import(value = {MqttClientConfiguration.class})
public class SensorServiceConfiguration {

  @Bean
  public ModelMapper getModelMapper(){
    return new ModelMapper();
  }
}
