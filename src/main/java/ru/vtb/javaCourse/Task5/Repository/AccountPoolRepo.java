package ru.vtb.javaCourse.Task5.Repository;

import org.springframework.data.repository.CrudRepository;
import ru.vtb.javaCourse.Task5.Entity.AccountsPool;
import ru.vtb.javaCourse.Task5.Entity.AccountPoolKey;

import java.util.Optional;

public interface AccountPoolRepo extends CrudRepository<AccountsPool, Long> {
    public Optional<AccountsPool> findByAccountPoolKey(AccountPoolKey accountPoolKey);
}
