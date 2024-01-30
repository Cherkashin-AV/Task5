package ru.vtb.javaCourse.Task5.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "account_pool")
public class AccountsPool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private AccountPoolKey accountPoolKey;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "accounts")
    @JsonProperty("accounts")
    private Accounts accounts;

    public String getFirstAccount(){
        return accounts.accounts.size()>0? accounts.accounts.getFirst():null;
    }

}
