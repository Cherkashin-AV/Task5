package ru.vtb.javaCourse.Task5.CorporateSettlementInstance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InstanceResponse {
    InstanceResponceBodyData data;

    public static InstanceResponse createAccountResponceBody(String accountId, Set<String> registerId, Set<String> supplementaryAgreementId) {
        return new InstanceResponse(new InstanceResponceBodyData(accountId, registerId, supplementaryAgreementId));
    }

    @AllArgsConstructor
    @Data
    public static class InstanceResponceBodyData {
        private String instanceId;
        private Set<String> registerId;
        private Set<String> supplementaryAgreementId;
    }
}

