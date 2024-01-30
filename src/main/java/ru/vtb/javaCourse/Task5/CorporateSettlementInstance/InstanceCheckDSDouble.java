package ru.vtb.javaCourse.Task5.CorporateSettlementInstance;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.vtb.javaCourse.Task5.Entity.Product;
import ru.vtb.javaCourse.Task5.Repository.ProductRepo;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

@Component
@Qualifier("instanceCheck")
@Order(2)
public class InstanceCheckDSDouble implements Consumer<InstanceRequest>{
    private final ProductRepo productRepo;

    @Autowired
    public InstanceCheckDSDouble(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public void accept(InstanceRequest request) {
        if (request.getInstanceId()==null)
            return;
        Long productId = request.getInstanceId();
        Set<InstanceRequest.InstanceArrangement> arrangements = request.getArrangements();
        for (InstanceRequest.InstanceArrangement arrangement : arrangements) {
            Optional<Product> arrangementInDB = productRepo.findArrangementsByNumber(productId, arrangement.getNumber());
            arrangementInDB.ifPresent(p ->
            {
                throw new ValidationException(String.format("Параметр № Дополнительного соглашения (сделки) Number %s уже существует для ЭП с ИД  %d."
                        , p.getNumber()
                        , p.getId()));
            });
        }

    }
}
