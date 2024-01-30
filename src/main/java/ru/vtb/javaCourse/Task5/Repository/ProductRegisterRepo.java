package ru.vtb.javaCourse.Task5.Repository;

import org.springframework.data.repository.CrudRepository;
import ru.vtb.javaCourse.Task5.Entity.ProductRegister;


import java.util.Optional;
import java.util.Set;

public interface ProductRegisterRepo extends CrudRepository<ProductRegister, Long> {
    public Optional<ProductRegister> findByProduct_idAndType_value(Long product_id, String type_value);
    public Set<ProductRegister> findByProduct_id(Long product_id);
}
