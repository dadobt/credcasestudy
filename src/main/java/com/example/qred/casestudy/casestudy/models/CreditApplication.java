package com.example.qred.casestudy.casestudy.models;


import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;



@Entity
@Data
@Table(name = "credit_application")
@EntityListeners(AuditingEntityListener.class)
public class CreditApplication {

    @Id
    @GeneratedValue
    private Long id;

    private BigDecimal amountApplied;

    private String applicationStatus;
    private String email;
    private String phoneNumber;
    private String organizationNumber;

    @CreatedDate
    private Timestamp createdDate;

    @LastModifiedDate
    private Timestamp modifiedDate;

    private String loanApplicant;

    public CreditApplication() {
    }
}
