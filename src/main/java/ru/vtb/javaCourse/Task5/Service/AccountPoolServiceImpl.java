package ru.vtb.javaCourse.Task5.Service;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vtb.javaCourse.Task5.Entity.AccountPoolKey;
import ru.vtb.javaCourse.Task5.Entity.AccountsPool;
import ru.vtb.javaCourse.Task5.Repository.AccountPoolRepo;

@Service
public class AccountPoolServiceImpl implements AccountPoolService {
    private AccountPoolRepo accountPoolRepo;

    @Autowired
    public AccountPoolServiceImpl(AccountPoolRepo accountPoolRepo) {
        this.accountPoolRepo = accountPoolRepo;
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

    public  AccountsPool findByAccountPoolKey(AccountPoolKey accountPoolKey ){
        return accountPoolRepo.findByAccountPoolKey(accountPoolKey).orElseThrow(()->new ValidationException("Не найден пул счетов "+accountPoolKey));
    }
}
