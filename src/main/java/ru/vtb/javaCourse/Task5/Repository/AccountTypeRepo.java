package ru.vtb.javaCourse.Task5.Repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.vtb.javaCourse.Task5.Entity.AccountType;


@Repository
public interface AccountTypeRepo extends CrudRepository<AccountType, Long> {
}
