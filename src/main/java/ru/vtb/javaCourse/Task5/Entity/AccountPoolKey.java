package ru.vtb.javaCourse.Task5.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AccountPoolKey implements Serializable {
    @Column(name = "branchcode")
    private String branchCode;
    @Column(name = "currencycode")
    private String currencyCode;
    @Column(name = "mdmcode")
    private String mdmCode;
    @Column(name = "prioritycode")
    private String priorityCode;
    @Column(name = "registrytypecode")
    private String registryTypeCode;
}