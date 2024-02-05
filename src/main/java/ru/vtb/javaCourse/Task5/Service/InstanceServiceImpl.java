package ru.vtb.javaCourse.Task5.Service;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.javaCourse.Task5.CorporateSettlementInstance.InstanceCheck;
import ru.vtb.javaCourse.Task5.CorporateSettlementInstance.InstanceRequest;
import ru.vtb.javaCourse.Task5.CorporateSettlementInstance.InstanceResponse;
import ru.vtb.javaCourse.Task5.Entity.*;
import ru.vtb.javaCourse.Task5.Repository.ProductClassRepo;
import ru.vtb.javaCourse.Task5.Repository.ProductRepo;
import ru.vtb.javaCourse.Task5.Stubs;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class InstanceServiceImpl implements InstanceService {

    private final ProductRepo productRepo;
    private final ProductClassService productClassService;
    private AccountService accountService;
    private final AccountPoolService accountPoolService;


    @Autowired
    private List<InstanceCheck> checks;
    private HttpSession httpSession;

    @Autowired
    public InstanceServiceImpl(ProductRepo productRepo, AccountService accountService, ProductClassService productClassService, HttpSession httpSession, AccountPoolServiceImpl accountPoolService) {
        this.productRepo = productRepo;
        this.accountService = accountService;
        this.productClassService = productClassService;
        this.httpSession = httpSession;
        this.accountPoolService = accountPoolService;
    }

    @Transactional
    public InstanceResponse performRequest(InstanceRequest request){
        Product product;
        //Проверки
        for(InstanceCheck check : checks){
            check.accept(request);
        }

        //Создаем ЭП
        if (request.getInstanceId() == null) {
            product = saveProduct(request);
            saveRegisters(product, request);
        }
        //Изменяем изменяем состав ДС
        else{
            product = addArragements(request);
        }
        //Формирование ответа
        return generateAnswer(product);
    }

    private InstanceResponse generateAnswer(Product product) {
        Set<String> supplementaryAgreements = new HashSet<>();
        for (var agreement : product.getAgreements()) {
            supplementaryAgreements.add(agreement.getId().toString());
        }

        Set<String> registers = new HashSet<>();
        for (var register : product.getProductRegisters()) {
            registers.add(register.getId().toString());
        }
        return new InstanceResponse(new InstanceResponse.InstanceResponceBodyData(product.getId().toString(), registers, supplementaryAgreements));
    }

    private Product addArragements(InstanceRequest request) {
        //Находим договор
        Product instance = productRepo.findById(request.getInstanceId())
                .orElseThrow(() -> new ValidationException("Не найден экзкмпляр продукта id = " + request.getInstanceId()));
        for (var arrangement : request.getArrangements()) {
            Product product = new Product();
            product.setProductClass(productClassService.findByValue(request.getProductCode()));
            product.setClientId(Stubs.getClientByMDMCode(request.getMdmCode()));
            product.setType(arrangement.getArrangementType());
            product.setNumber(arrangement.getNumber());
            product.setStartDateTime(arrangement.getOpeningDate());
            product.setEndDateTime(arrangement.getClosingDate());
            product.setDays(arrangement.getValidityDuration());
            product.setReasonClose(arrangement.getCancellationReason());
            product.setState(Enum.valueOf(State.class, arrangement.getStatus()));
            instance.addAgreement(product);
            productRepo.save(product);
        }
        return instance;
    }

    private void saveRegisters(Product product, InstanceRequest request) {
        AccountsPool accountsPool = accountPoolService.getAccountsPool(request.getBranchCode(), request.getIsoCurrencyCode(), request.getMdmCode(), "00", request.getRegisterType());
        Set<ProductRegisterType> productRegisterTypes = (Set) httpSession.getAttribute("productRegisterTypes");
        for (ProductRegisterType productRegisterType : productRegisterTypes) {
            ProductRegister productRegister = accountService.createProductRegister(product, productRegisterType, accountsPool);
            product.getProductRegisters().add(productRegister);
        }
    }

    private Product saveProduct(InstanceRequest request) {
        Product product = new Product();
        product.setProductClass(productClassService.findByValue(request.getProductCode()));
        product.setClientId(Stubs.getClientByMDMCode(request.getMdmCode()));
        product.setType(request.getProductType());
        product.setNumber(request.getContractNumber());
        product.setPriority(request.getPriority());
        product.setDateOfConclusion(request.getContractDate());
        product.setStartDateTime(LocalDate.now());
        product.setEndDateTime(null);
        product.setDays(null);
        product.setPenaltyRate(request.getInterestRatePenalty());
        product.setNso(request.getMinimalBalance());
        product.setThresholdAmount(product.getThresholdAmount());
        product.setRequisiteType(request.getAccountingDetails());
        product.setInterestRateType(request.getRateType());
        product.setTaxRate(request.getTaxPercentageRate());
        product.setReasonClose(null);
        product.setState(State.REGISTRED);
        productRepo.save(product);
        return product;
    }
    public Product findById(Long productID){
        return productRepo.findById(productID).orElseThrow(() -> new ValidationException(String.format("Не найден продукт id=%s", productID)));
    }


}
