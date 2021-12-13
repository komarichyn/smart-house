package org.komarichyn.ss.database.sql;


import org.komarichyn.ss.database.sql.entity.Sensor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISensorRepository extends PagingAndSortingRepository<Sensor, Long> {

}
