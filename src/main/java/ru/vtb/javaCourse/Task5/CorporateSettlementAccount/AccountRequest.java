package ru.vtb.javaCourse.Task5.CorporateSettlementAccount;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountRequest {
    @NotNull(message = "Имя обязательного параметра instanceId не заполнено")
    private Long instanceId;
    private String registryTypeCode;
    private String accountType;
    private String currencyCode;
    private String branchCode;
    private String priorityCode;
    private String mdmCode;
    private String clientCode;
    private String trainRegion;
    private String counter;
    private String salesCode;

    public AccountRequest(Long instanceId, String registryTypeCode, String accountType, String currencyCode, String branchCode, String priorityCode, String mdmCode) {
        this.instanceId = instanceId;
        this.registryTypeCode = registryTypeCode;
        this.accountType = accountType;
        this.currencyCode = currencyCode;
        this.branchCode = branchCode;
        this.priorityCode = priorityCode;
        this.mdmCode = mdmCode;
    }
}
