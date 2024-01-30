package ru.vtb.javaCourse.Task5.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.vtb.javaCourse.Task5.Entity.Product;

import java.util.Optional;

public interface ProductRepo extends CrudRepository<Product, Long> {
    public Optional<Product> findByNumber(String number);
    @Query("SELECT a FROM Product p JOIN p.agreements a WHERE p.id=:productId AND a.number=:arrangementNumber")
    public Optional<Product> findArrangementsByNumber(Long productId, String arrangementNumber);
}
