package ru.vtb.javaCourse.Task5.Service;

import jakarta.validation.ValidationException;
import ru.vtb.javaCourse.Task5.CorporateSettlementInstance.InstanceRequest;
import ru.vtb.javaCourse.Task5.CorporateSettlementInstance.InstanceResponse;
import ru.vtb.javaCourse.Task5.Entity.*;
import ru.vtb.javaCourse.Task5.Stubs;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public interface InstanceService {
    public InstanceResponse performRequest(InstanceRequest request);
    Product findById(Long productID);
}
