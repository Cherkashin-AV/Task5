package ru.vtb.javaCourse.Task5.CorporateSettlementAccount;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.vtb.javaCourse.Task5.Entity.Product;
import ru.vtb.javaCourse.Task5.Entity.ProductRegisterType;
import ru.vtb.javaCourse.Task5.Exceptions.HTTPNotFoundException;
import ru.vtb.javaCourse.Task5.Repository.ProductRegisterTypeRepo;
import ru.vtb.javaCourse.Task5.Repository.ProductRepo;
import ru.vtb.javaCourse.Task5.Utils;

import java.util.function.Consumer;

@Component
@Qualifier("accountCheck")
@Order(2)
public class AccountCheckCatalog implements Consumer<AccountRequest> {

    private final ProductRepo productRepo;
    private final ProductRegisterTypeRepo registerTypeRepo;

    @Autowired
    public AccountCheckCatalog(ProductRepo productRepo, ProductRegisterTypeRepo registerTypeRepo) {
        this.productRepo = productRepo;
        this.registerTypeRepo = registerTypeRepo;
    }

    @Override
    public void accept(AccountRequest accountRequest) {
        Long productID = accountRequest.getInstanceId();
        String productRegisterTypeReq = accountRequest.getRegistryTypeCode();
        Product product = productRepo.findById(productID).orElseThrow(() -> new ValidationException(String.format("Не найден продукт id=%s", productID)));
        String productClassCode = product.getProductClass().getValue();

        registerTypeRepo.findByProductClassAndValue(productClassCode, productRegisterTypeReq).orElseThrow(
                () -> new HTTPNotFoundException(
                        String.format(
                                "КодПродукта %s не найдено в Каталоге продуктов %s.%s для данного типа Регистра"
                                , productRegisterTypeReq
                                , Utils.getSchemaDB()
                                , Utils.getTableNameFromEntity(ProductRegisterType.class))));
    }
}
