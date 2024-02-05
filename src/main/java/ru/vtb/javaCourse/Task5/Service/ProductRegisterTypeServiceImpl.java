package ru.vtb.javaCourse.Task5.Service;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vtb.javaCourse.Task5.Entity.ProductRegisterType;
import ru.vtb.javaCourse.Task5.Repository.ProductRegisterTypeRepo;

@Service
public class ProductRegisterTypeServiceImpl implements ProductRegisterTypeService {
    private ProductRegisterTypeRepo registerTypeRepo;

    @Autowired
    public ProductRegisterTypeServiceImpl(ProductRegisterTypeRepo registerTypeRepo) {
        this.registerTypeRepo = registerTypeRepo;
    }

    @Override
    public ProductRegisterType findByValue(String registryTypeCode) {
        return registerTypeRepo.findByValue(registryTypeCode).orElseThrow(()->new ValidationException("Не найден тип продуктового регистра с типом "+registryTypeCode));
    }
}
