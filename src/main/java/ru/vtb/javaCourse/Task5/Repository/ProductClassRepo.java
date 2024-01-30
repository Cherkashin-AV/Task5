package ru.vtb.javaCourse.Task5.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.vtb.javaCourse.Task5.Entity.RefProductClass;

import java.util.Optional;

@Repository
public interface ProductClassRepo extends CrudRepository<RefProductClass, Long> {
    public Optional<RefProductClass> findByValue(String value);
}
