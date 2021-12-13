package org.komarichyn.ss.database.sql;

import org.komarichyn.ss.database.sql.entity.Sensor;
import org.komarichyn.ss.database.sql.entity.SensorData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISensorDataRepository extends PagingAndSortingRepository<SensorData, Long> {

  Page<SensorData> findAllBySensorOrderByCreatedDesc(Sensor sensor, Pageable pageable);
  Page<SensorData> findAllBySensorAndType(Sensor sensor, Integer type, Pageable pageable);

}
