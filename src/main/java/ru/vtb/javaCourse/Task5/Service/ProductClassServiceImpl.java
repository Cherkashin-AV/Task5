package ru.vtb.javaCourse.Task5.Service;


import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vtb.javaCourse.Task5.Entity.RefProductClass;
import ru.vtb.javaCourse.Task5.Repository.ProductClassRepo;

@Service
public class ProductClassServiceImpl implements ProductClassService {
    private ProductClassRepo productClassRepo;

    @Autowired
    public ProductClassServiceImpl(ProductClassRepo productClassRepo) {
        this.productClassRepo = productClassRepo;
    }

    public RefProductClass findByValue(String productCode){
        return productClassRepo.findByValue(productCode)
                .orElseThrow(
                        () -> new ValidationException("Не найдена запись в справочнике класса продукта с кодом " + productCode)
                );
    }
}
