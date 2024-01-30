package ru.vtb.javaCourse.Task5.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="tpp_product_register")
@ToString
public class ProductRegister {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long	id;
    @Getter @Setter
    @ManyToOne @JoinColumn(name = "product_id")
    private Product product;
    @Getter @Setter
    @ManyToOne @JoinColumn(name = "type", referencedColumnName = "value")
    private ProductRegisterType type;
    @Getter @Setter
    @ManyToOne @JoinColumn(columnDefinition = "account_id")
    private AccountsPool account;
    @Getter @Setter
    private String currencyCode;
    @Getter @Setter
    @Enumerated(EnumType.STRING)
    private State state;
    @Getter @Setter
    private String accountNumber;

    public ProductRegister() {
    }

    public ProductRegister(Product product, ProductRegisterType type, AccountsPool account, String currencyCode, State state, String accountNumber) {
        this.product = product;
        this.type = type;
        this.account = account;
        this.currencyCode = currencyCode;
        this.state = state;
        this.accountNumber = accountNumber;
    }
}
