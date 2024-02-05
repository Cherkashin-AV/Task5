package ru.vtb.javaCourse.Task5.Service;

import ru.vtb.javaCourse.Task5.Entity.AccountPoolKey;
import ru.vtb.javaCourse.Task5.Entity.AccountsPool;

public interface AccountPoolService {
    AccountsPool getAccountsPool(String branchCode, String currencyCode, String mdmCode, String priorityCode, String registryTypeCode);
    AccountsPool findByAccountPoolKey(AccountPoolKey accountPoolKey );

}
