package ru.vtb.javaCourse.Task5.CorporateSettlementInstance;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import ru.vtb.javaCourse.Task5.Entity.ProductRegisterType;
import ru.vtb.javaCourse.Task5.Exceptions.HTTPNotFoundException;
import ru.vtb.javaCourse.Task5.Repository.ProductRegisterTypeRepo;
import ru.vtb.javaCourse.Task5.Utils;

import java.util.Set;
import java.util.function.Consumer;

@Component
@Qualifier("instanceCheck")
@Order(3)
public class InstanceCheckRegisterType implements Consumer<InstanceRequest> {
    private final ProductRegisterTypeRepo productRegisterTypeRepo;
    private final HttpSession httpSession;

    @Autowired
    public InstanceCheckRegisterType(ProductRegisterTypeRepo productRegisterTypeRepo, HttpSession httpSession) {
        this.productRegisterTypeRepo = productRegisterTypeRepo;
        this.httpSession = httpSession;
    }


    @Override
    public void accept(InstanceRequest request) {
        if (request.getInstanceId()!=null)
            return;
        String productCode = request.getProductCode();
        String accountType = "Клиентский";
        Set productRegisterTypes = productRegisterTypeRepo.findByProductClassAndAccountType(productCode, accountType);
        if (productRegisterTypes.size() == 0) {
            new HTTPNotFoundException(
                    String.format(
                            "КодПродукта %s не найдено в Каталоге продуктов %s.%s>"
                            , productCode
                            , Utils.getSchemaDB()
                            , Utils.getTableNameFromEntity(ProductRegisterType.class)));
        }
        httpSession.setAttribute("productRegisterTypes", productRegisterTypes);
    }
}
