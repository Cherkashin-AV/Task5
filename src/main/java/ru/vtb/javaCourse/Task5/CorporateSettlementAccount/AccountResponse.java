package ru.vtb.javaCourse.Task5.CorporateSettlementAccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountResponse implements Serializable {
    private AccountResponceBodyData data;
    public static AccountResponse createAccountResponceBody(String accountId){
        return new AccountResponse(new AccountResponceBodyData(accountId));
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class AccountResponceBodyData{
        private String accountId;
    }
}
