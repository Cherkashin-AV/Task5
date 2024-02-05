package ru.vtb.javaCourse.Task5.Service;

import ru.vtb.javaCourse.Task5.Entity.ProductRegisterType;

public interface ProductRegisterTypeService {
    ProductRegisterType findByValue(String registryTypeCode);
}
