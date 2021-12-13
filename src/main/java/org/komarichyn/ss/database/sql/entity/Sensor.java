package org.komarichyn.ss.database.sql.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Include;

@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "sensor", schema = "sensors")
public class Sensor {

  @Getter
  @Setter
  @Include
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  @Getter
  @Setter
  @Include
  @Column(name = "name", unique = true)
  private String name;
  @Getter
  @Setter
  @Include
  @Column(name = "income")
  private String income;
  @Getter
  @Setter
  @Include
  @Column(name = "outcome", unique = true)
  private String outcome;
  @Getter
  @Setter
  @OneToMany(mappedBy = "sensor")
  private List<SensorData> sensorData = new java.util.ArrayList<>();



}
