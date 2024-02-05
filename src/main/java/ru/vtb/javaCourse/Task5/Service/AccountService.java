package ru.vtb.javaCourse.Task5.Service;

import jakarta.validation.ValidationException;
import ru.vtb.javaCourse.Task5.CorporateSettlementAccount.AccountRequest;
import ru.vtb.javaCourse.Task5.CorporateSettlementAccount.AccountResponse;
import ru.vtb.javaCourse.Task5.Entity.*;

public interface AccountService {
    AccountResponse createProductRegister(AccountRequest accountRequest);
    ProductRegister createProductRegister(Product product, ProductRegisterType productRegisterType, AccountsPool accountsPool);
}
