package ru.vtb.javaCourse.Task5.CorporateSettlementAccount;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.vtb.javaCourse.Task5.Repository.ProductRegisterRepo;

import java.util.function.Consumer;

@Component
@Order(1)
public class AccountCheckDouble implements AccountCheck {

    private final ProductRegisterRepo registerRepo;

    @Autowired
    public AccountCheckDouble(ProductRegisterRepo registerRepo) {
        this.registerRepo = registerRepo;
    }

    @Override
    public void accept(AccountRequest accountRequest) {
        Long productID = accountRequest.getInstanceId();
        String productRegisterType = accountRequest.getRegistryTypeCode();

        registerRepo.findByProduct_idAndType_value(productID, productRegisterType)
                .ifPresent(p-> {throw new ValidationException(
                                String.format(
                                        "Параметр registryTypeCode тип регистра %s уже существует для ЭП с ИД  %d"
                                        ,productRegisterType
                                        ,productID));});
    }
}
