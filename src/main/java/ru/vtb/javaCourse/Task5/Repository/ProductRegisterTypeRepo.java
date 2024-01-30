package ru.vtb.javaCourse.Task5.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.vtb.javaCourse.Task5.Entity.ProductRegisterType;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRegisterTypeRepo extends CrudRepository<ProductRegisterType, Long> {
    public Optional<ProductRegisterType> findByValue(String value);

    @Query("Select p from ProductRegisterType p where p.productClass.value=:productClassValue and p.accountType.value=:accountTypeValue")
    public Set<ProductRegisterType> findByProductClassAndAccountType(String productClassValue, String accountTypeValue);

    @Query("Select p from ProductRegisterType p where p.productClass.value=:productClassValue and p.value=:value")
    public Optional<ProductRegisterType> findByProductClassAndValue(String productClassValue, String value);

}
