package ru.vtb.javaCourse.Task5.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tpp_ref_account_type")
public class AccountType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long internalId;
    @Getter @Setter
    @Column(name = "value")
    private String value;

    public AccountType() {
    }

    public AccountType(String value) {
        this.value = value;
    }
}
