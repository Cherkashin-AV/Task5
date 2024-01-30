package ru.vtb.javaCourse.Task5.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="tpp_ref_product_register_type")
@ToString
public class ProductRegisterType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Column(name = "internal_id")
    private Long internalId;
    @Getter @Setter
    private String	value;
    @Getter @Setter
    private String registerTypeName;
    @ManyToOne @JoinColumn(name = "product_class_code", referencedColumnName = "value")
    @Getter @Setter
    private RefProductClass productClass;
    @ManyToOne @JoinColumn(name = "account_type", referencedColumnName = "value")
    @Getter @Setter
    private AccountType accountType;

}
