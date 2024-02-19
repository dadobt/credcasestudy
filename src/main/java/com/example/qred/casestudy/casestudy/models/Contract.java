package com.example.qred.casestudy.casestudy.models;


import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "contract")
public class Contract {

    @Id
    @GeneratedValue
    private Long id;

    private String organizationName;

    private String organizationNumber;

    private String organizationType;

    private BigDecimal amount;

    private BigInteger term;

    private BigDecimal interest;

    private BigDecimal totalCommission;

    private BigDecimal totalAmount;

    @CreatedDate
    private Timestamp  dateOfSignature;

    private String loanApplicant;

}
