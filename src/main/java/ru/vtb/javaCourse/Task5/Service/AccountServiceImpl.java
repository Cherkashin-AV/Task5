package ru.vtb.javaCourse.Task5.Service;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.javaCourse.Task5.CorporateSettlementAccount.AccountCheck;
import ru.vtb.javaCourse.Task5.CorporateSettlementAccount.AccountRequest;
import ru.vtb.javaCourse.Task5.CorporateSettlementAccount.AccountResponse;
import ru.vtb.javaCourse.Task5.Entity.*;
import ru.vtb.javaCourse.Task5.Repository.ProductRegisterRepo;

import java.util.List;
import java.util.function.Consumer;

@Service
@PropertySource(value = "classpath:application.properties")
public class AccountServiceImpl implements AccountService {
    private final ProductRegisterRepo registerRepo;
    private InstanceService productService;
    private final ProductRegisterTypeService registerTypeService;
    private final AccountPoolService accountPoolService;

    @Autowired
    public AccountServiceImpl(ProductRegisterRepo registerRepo, @Lazy InstanceService productService, ProductRegisterTypeService registerTypeService, AccountPoolServiceImpl accountPoolService) {
        this.registerRepo = registerRepo;
        this.productService = productService;
        this.registerTypeService = registerTypeService;
        this.accountPoolService = accountPoolService;
    }

    private List<AccountCheck> checks;

    @Autowired
    public void setChecks(List<AccountCheck> checks) {
        this.checks = checks;
    }

    @Transactional
    public AccountResponse createProductRegister(AccountRequest accountRequest){
        //Проверки
        for(Consumer check : checks){
            check.accept(accountRequest);
        }
        ProductRegister productRegister = createProductRegistr(accountRequest);
        return AccountResponse.createAccountResponceBody(productRegister.getId().toString());
    }

    public ProductRegister createProductRegister(Product product, ProductRegisterType productRegisterType, AccountsPool accountsPool){
        ProductRegister register = new ProductRegister(
                product,
                productRegisterType,
                accountsPool,
                accountsPool.getAccountPoolKey().getCurrencyCode(),
                State.OPEN,
                accountsPool.getFirstAccount()
        );
        registerRepo.save(register);
        return register;
    }

    public ProductRegister createProductRegistr(AccountRequest accountRequest) {
        if (accountRequest.getRegistryTypeCode() == null) {
            throw new ValidationException("Не указан Тип регистра");
        }
        Long productID = accountRequest.getInstanceId();
        Product product = productService.findById(productID);
        ProductRegister productRegister = new ProductRegister();
        final AccountsPool account = accountPoolService.getAccountsPool(
                accountRequest.getBranchCode()
                , accountRequest.getCurrencyCode()
                , accountRequest.getMdmCode()
                , accountRequest.getPriorityCode()
                , accountRequest.getRegistryTypeCode()
        );
        productRegister.setProduct(product);
        productRegister.setType(
                registerTypeService.findByValue(accountRequest.getRegistryTypeCode()));
        productRegister.setAccount(account);
        productRegister.setCurrencyCode(accountRequest.getCurrencyCode());
        productRegister.setState(State.REGISTRED);
        productRegister.setAccountNumber(account.getFirstAccount());
        registerRepo.save(productRegister);
        return productRegister;
    }

}

