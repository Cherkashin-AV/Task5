package ru.vtb.javaCourse.Task5.UnitTest;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vtb.javaCourse.Task5.CorporateSettlementAccount.AccountCheck;
import ru.vtb.javaCourse.Task5.CorporateSettlementAccount.AccountRequest;
import ru.vtb.javaCourse.Task5.CorporateSettlementAccount.AccountResponse;
import ru.vtb.javaCourse.Task5.Entity.*;
import ru.vtb.javaCourse.Task5.Repository.ProductRegisterRepo;
import ru.vtb.javaCourse.Task5.Service.*;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование модуля AccountService")
public class AccountServiceTest {
    @InjectMocks
    private AccountServiceImpl accountService;
    @Mock
    private ProductRegisterRepo registerRepo;
    @Mock
    private InstanceServiceImpl productService;
    @Mock
    private ProductRegisterTypeServiceImpl registerTypeService;
    @Mock
    private AccountPoolServiceImpl accountPoolService;

    private AccountRequest getBaseAccountRequest() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setInstanceId(1L);
        accountRequest.setRegistryTypeCode("03.012.002_4753 3_ComSoLd");
        accountRequest.setAccountType("Клиентский");
        accountRequest.setCurrencyCode("800");
        accountRequest.setBranchCode("0022");
        accountRequest.setPriorityCode("00");
        accountRequest.setMdmCode("15");
        accountRequest.setClientCode("РЖД");
        accountRequest.setTrainRegion("РЖД");
        accountRequest.setCounter("РЖД");
        accountRequest.setSalesCode("123");
        return accountRequest;
    }

    @Test
    @DisplayName("Тестирование функции createProductRegister")
    public  void createProductRegister(){
        //Проверяем вызов проверок
        accountService.setChecks(List.of((AccountCheck) (p)->{if (p==null) throw new ValidationException("");}));
        Assertions.assertThrows(ValidationException.class, ()-> accountService.createProductRegister(null));

        //Проверяем создание продукта
        AccountsPool accountsPool = new AccountsPool(1L, new AccountPoolKey("0022", "800", "15", "00", "type"), new Accounts(List.of("123")));
        ProductRegister register = new ProductRegister();
        register.setId(1L);
        AccountRequest accountRequest = getBaseAccountRequest();
        when(productService.findById(Mockito.anyLong())).then((p)-> new Product());
        when(accountPoolService.getAccountsPool(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).then((p)-> accountsPool);
        when(registerTypeService.findByValue(Mockito.any())).then((p)-> new ProductRegisterType());
        when(registerRepo.save(Mockito.any(ProductRegister.class))).then((p)->{
            ProductRegister r = p.getArgument(0);
            r.setId(1L);
            return r;});
        AccountResponse accountResponse = accountService.createProductRegister(accountRequest);
        Assertions.assertEquals("1", accountResponse.getData().getAccountId());
    }

    @Test
    @DisplayName("Тестирование функции createProductRegister (3 параметра)")
    public  void createProductRegister3Param() {
        Product product = new Product();
        ProductRegisterType productRegisterType = new ProductRegisterType();
        AccountsPool accountsPool = new AccountsPool(1L, new AccountPoolKey("0022", "800", "15", "00", "type"), new Accounts(List.of("123")));
        when(registerRepo.save(Mockito.any(ProductRegister.class))).then((p)->{
            ProductRegister r = p.getArgument(0);
            r.setId(1L);
            return r;});
        ProductRegister productRegister = accountService.createProductRegister(product, productRegisterType, accountsPool);
        Assertions.assertEquals(1L, productRegister.getId());
        Assertions.assertTrue(productRegister.getProduct() == product);
        Assertions.assertTrue(productRegister.getAccount() == accountsPool);
        Assertions.assertTrue(productRegister.getCurrencyCode() == "800");
        Assertions.assertTrue(productRegister.getAccountNumber() == "123");
        Assertions.assertTrue(productRegister.getType() == productRegisterType);
        Assertions.assertTrue(productRegister.getCurrencyCode() == "800");
        Assertions.assertTrue(productRegister.getState() == State.OPEN);

    }
}
