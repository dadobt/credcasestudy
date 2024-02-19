package com.example.qred.casestudy.casestudy.models;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;


@Entity
@Data
@Table(name = "offer")
@EntityListeners(AuditingEntityListener.class)
public class Offer {

    @Id
    @GeneratedValue
    private Long id;

    private BigDecimal amount;

    //term is calculated in days
    private BigInteger term;

    private BigDecimal interest;

    private BigDecimal totalCommission;

    private BigDecimal totalAmount;

    private Timestamp dayOfExpiration;

    @CreatedDate
    private Timestamp createdDate;

    @LastModifiedDate
    private Timestamp modifiedDate;

    private String userId;

    @OneToOne
    private CreditApplication creditApplication;

    public Offer() {
    }
}
