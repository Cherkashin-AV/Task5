package ru.vtb.javaCourse.Task5.CorporateSettlementInstance;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@Data
public class InstanceRequest {
    private Long	instanceId;
    @NotEmpty
    private String	productType;
    @NotEmpty
    private String	productCode;
    @NotEmpty
    private String	registerType;
    @NotEmpty
    private String	mdmCode;
    @NotEmpty
    private String	contractNumber;
    @NotNull
    private LocalDate contractDate;
    @NotNull
    private Integer	priority;
    private Float	interestRatePenalty;
    private Float	minimalBalance;
    private Float	thresholdAmount ;
    private String	accountingDetails;
    private String	rateType;
    private Float	taxPercentageRate ;
    private Float	technicalOverdraftLimitAmount;
    @NotNull
    private Integer	contractId;
    @NotEmpty
    private String	branchCode;
    @NotEmpty
    private String	IsoCurrencyCode;
    @NotEmpty
    private String	urgencyCode;
    private Integer	referenceCode;
    private Set<Data> properties;
    @Valid
    private Set<InstanceArrangement> arrangements;

    @NoArgsConstructor
    @lombok.Data
    public static class InstanceArrangement {
        private String generalAgreementId;
        private String supplementaryAgreementId;
        private String arrangementType;
        private Integer shedulerJobId;
        @NotEmpty
        private String number;
        @NotNull
        private LocalDate openingDate;
        private LocalDate closingDate;
        private LocalDate cancelDate;
        private Integer validityDuration;
        private String cancellationReason;
        private String status;
        private LocalDate interestCalculationDate;
        private Float interestRate;
        private Float coefficient;
        private String coefficientAction;
        private Float minimumInterestRate;
        private String minimumInterestRateCoefficient;
        private String minimumInterestRateCoefficientAction;
        private Float maximalnterestRate;
        private Float maximalnterestRateCoefficient;
        private String maximalnterestRateCoefficientAction;
    }

    @NoArgsConstructor
    @lombok.Data
    public static class Data{
        DataKeyEnum key;
        String value;
    }

    public enum DataKeyEnum{
        RailwayRegionOwn("Регион принадлежности железной дороги"),
        counter("Счетчик");

        public final String name;
        private DataKeyEnum(String name){
            this.name = name;
        }
    }
}
