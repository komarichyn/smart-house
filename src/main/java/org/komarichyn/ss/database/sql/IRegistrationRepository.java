package org.komarichyn.ss.database.sql;

import javax.validation.constraints.NotNull;
import org.komarichyn.ss.database.sql.entity.RegistrationInfo;
import org.springframework.data.repository.CrudRepository;

public interface IRegistrationRepository extends CrudRepository<RegistrationInfo, Long> {

  RegistrationInfo findByCode(@NotNull String code);

}
