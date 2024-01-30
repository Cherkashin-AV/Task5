package ru.vtb.javaCourse.Task5.Repository;

import org.springframework.data.repository.CrudRepository;
import ru.vtb.javaCourse.Task5.Entity.Agreement;

public interface AgreementsRepo extends CrudRepository<Agreement, Long> {
}
