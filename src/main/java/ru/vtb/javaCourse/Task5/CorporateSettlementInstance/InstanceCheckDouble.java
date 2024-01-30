package ru.vtb.javaCourse.Task5.CorporateSettlementInstance;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.vtb.javaCourse.Task5.Entity.Product;
import ru.vtb.javaCourse.Task5.Repository.ProductRepo;

import java.util.Optional;
import java.util.function.Consumer;

@Component
@Qualifier("instanceCheck")
@Order(1)
public class InstanceCheckDouble implements Consumer<InstanceRequest> {
    private final ProductRepo productRepo;

    @Autowired
    public InstanceCheckDouble(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public void accept(InstanceRequest request) {
        if (request.getInstanceId()!=null)
            return;
        String contractNumber = request.getContractNumber();
        Optional<Product> productInDB = productRepo.findByNumber(contractNumber);
        productInDB.ifPresent(p ->
        {
            throw new ValidationException(String.format("Параметр ContractNumber № договора %s уже существует для ЭП с ИД  %d."
                    , p.getNumber()
                    , p.getId()));
        });
    }
}
