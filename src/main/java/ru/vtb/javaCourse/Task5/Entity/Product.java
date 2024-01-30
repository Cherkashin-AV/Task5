package ru.vtb.javaCourse.Task5.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name="tpp_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long	id;
    @Setter
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "agreements",
            joinColumns = {@JoinColumn(name = "id")},
            inverseJoinColumns = {@JoinColumn(name = "agreement_id")})
    private Set<Product> agreements = new HashSet<>();

    public Set<Product> getAgreements() {
        return Set.copyOf(agreements);
    }
    public void addAgreement(Product agreement){
        agreements.add(agreement);
    }

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name="product_code_id", referencedColumnName="internalId")
    private RefProductClass productClass;
    @Getter @Setter
    private Long clientId;
    @Getter @Setter
    private String	type;
    @Getter @Setter
    private String	number;
    @Getter @Setter
    private Integer	priority;
    @Getter @Setter
    private LocalDate dateOfConclusion;
    @Getter @Setter
    private LocalDate startDateTime;
    @Getter @Setter
    private LocalDate endDateTime;
    @Getter @Setter
    private Integer	days;
    @Getter @Setter
    private Float penaltyRate;
    @Getter @Setter
    private Float	nso;
    @Getter @Setter
    private Double thresholdAmount;
    @Getter @Setter
    private String requisiteType;
    @Getter @Setter
    private String interestRateType;
    @Getter @Setter
    private Float taxRate;
    @Getter @Setter
    private String reasonClose;
    @Getter @Setter
    @Enumerated(EnumType.STRING)
    private State	state;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Getter @Setter
    private Set<ProductRegister> productRegisters = new HashSet<>();

//    public Set<ProductRegister> getProductRegister(){
//        return Set.copyOf(productRegisters);
//    }
}
