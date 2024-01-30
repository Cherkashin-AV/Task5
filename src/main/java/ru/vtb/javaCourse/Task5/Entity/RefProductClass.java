package ru.vtb.javaCourse.Task5.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="tpp_ref_product_class")

public class RefProductClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Column(name="internalId")
    private Long internalId;
    @Getter @Setter
    @Column(name = "value")
    private String  value;
    @Getter @Setter
    @Column(name="gbl_code")
    private String gblCode;
    @Getter @Setter
    @Column(name="gbl_name")
    private String gblName;
    @Getter @Setter
    @Column(name="product_row_code")
    private String productRowCode;
    @Getter @Setter
    @Column(name="product_row_name")
    private String productRowName;
    @Getter @Setter
    @Column(name="subclass_code")
    private String subclassCode;
    @Getter @Setter
    @Column(name="subclass_name")
    private String subclassName;
}
