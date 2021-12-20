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
import org.komarichyn.ss.api.dto.IPassiveDevice;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "sensor", schema = "sensors")
public class Sensor  {

  @Include
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;
  @Include
  @Column(name = "name", unique = true)
  private String name;
  @Include
  @Column(name = "income")
  private String income;
  @Include
  @Column(name = "outcome", unique = true)
  private String outcome;
  @OneToMany(mappedBy = "sensor")
  private List<SensorData> sensorData = new java.util.ArrayList<>();



}
