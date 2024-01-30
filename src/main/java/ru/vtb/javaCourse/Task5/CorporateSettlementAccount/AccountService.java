package ru.vtb.javaCourse.Task5.CorporateSettlementAccount;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.javaCourse.Task5.Entity.*;
import ru.vtb.javaCourse.Task5.Exceptions.HTTPNotFoundException;
import ru.vtb.javaCourse.Task5.Repository.AccountPoolRepo;
import ru.vtb.javaCourse.Task5.Repository.ProductRegisterRepo;
import ru.vtb.javaCourse.Task5.Repository.ProductRegisterTypeRepo;
import ru.vtb.javaCourse.Task5.Repository.ProductRepo;
import ru.vtb.javaCourse.Task5.Utils;

import java.util.List;
import java.util.function.Consumer;

@Service
@PropertySource(value = "classpath:application.properties")
public class AccountService {
    private final ProductRegisterRepo registerRepo;
    private final ProductRepo productRepo;
    private final ProductRegisterTypeRepo registerTypeRepo;
    private final AccountPoolRepo accountPoolRepo;

    @Autowired
    public AccountService(ProductRegisterRepo registerRepo, ProductRepo productRepo, ProductRegisterTypeRepo registerTypeRepo, AccountPoolRepo accountPoolRepo) {
        this.registerRepo = registerRepo;
        this.productRepo = productRepo;
        this.registerTypeRepo = registerTypeRepo;
        this.accountPoolRepo = accountPoolRepo;
    }


    private List<Consumer> checks;

    @Autowired @Qualifier("accountCheck")
    public void setChecks(List<Consumer> checks) {
        this.checks = checks;
    }

    private ProductRegister createProductRegistr(AccountRequest accountRequest){
        if(accountRequest.getRegistryTypeCode() == null){
            throw new ValidationException("Не указан Тип регистра");
        }
        Long productID = accountRequest.getInstanceId();
        Product product = productRepo.findById(productID).orElseThrow(() -> new ValidationException(String.format("Не найден продукт id=%s", productID)));
        ProductRegister productRegister = new ProductRegister();
        final AccountsPool account = getAccountsPool(
                accountRequest.getBranchCode()
                ,accountRequest.getCurrencyCode()
                ,accountRequest.getMdmCode()
                ,accountRequest.getPriorityCode()
                ,accountRequest.getRegistryTypeCode()
                );
        productRegister.setProduct(product);
        productRegister.setType(
                registerTypeRepo.findByValue(accountRequest.getRegistryTypeCode()).orElseThrow());
        productRegister.setAccount(account);
        productRegister.setCurrencyCode(accountRequest.getCurrencyCode());
        productRegister.setState(State.REGISTRED);
        productRegister.setAccountNumber(account.getFirstAccount());
        registerRepo.save(productRegister);
        return productRegister;
    }

    public AccountsPool getAccountsPool(String branchCode, String currencyCode, String mdmCode, String priorityCode, String registryTypeCode) {
        AccountsPool account = accountPoolRepo.findByAccountPoolKey(new AccountPoolKey(
                branchCode,
                currencyCode,
                mdmCode,
                priorityCode,
                registryTypeCode
        )).orElseThrow(()->new ValidationException(String.format("Не найдены счета в пуле счетов [branchCode=%s, currencyCode=%s, mdmCode=%s, priorityCode=%s, registryTypeCode=%s]",
                branchCode,
                currencyCode,
                mdmCode,
                priorityCode,
                registryTypeCode
                )));
        return account;
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
}

