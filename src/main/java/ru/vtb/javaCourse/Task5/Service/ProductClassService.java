package ru.vtb.javaCourse.Task5.Service;

import org.springframework.stereotype.Service;
import ru.vtb.javaCourse.Task5.CorporateSettlementInstance.InstanceRequest;
import ru.vtb.javaCourse.Task5.CorporateSettlementInstance.InstanceResponse;
import ru.vtb.javaCourse.Task5.Entity.RefProductClass;
import ru.vtb.javaCourse.Task5.Repository.ProductClassRepo;

@Service
public interface ProductClassService {
    RefProductClass findByValue(String productCode);
}
