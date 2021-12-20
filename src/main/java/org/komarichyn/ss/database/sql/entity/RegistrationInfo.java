package org.komarichyn.ss.database.sql.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Lazy;

@Getter
@Setter
@Entity
@Table(name = "registration_info")
public class RegistrationInfo {

  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;
  private String code;
  private boolean registered;
  private boolean active;
  @Column(name = "created", columnDefinition = "TIMESTAMP")
  private LocalDateTime created;

  @OneToOne
  @Lazy
  @JoinColumn(name = "sensor_id")
  private Sensor sensor;

}
