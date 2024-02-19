package com.example.qred.casestudy.casestudy.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;


@Entity
@Data
@Table(name = "offer")
@EntityListeners(AuditingEntityListener.class)
public class Offer implements Serializable {

    @Id
    @GeneratedValue
    @JsonProperty
    private Long id;

    @JsonProperty
    private BigDecimal amount;

    //term is calculated in days
    @JsonProperty
    private BigInteger term;

    @JsonProperty
    private BigDecimal interest;

    @JsonProperty
    private BigDecimal totalCommission;
    @JsonProperty
    private BigDecimal totalAmount;
    @JsonProperty
    private Timestamp dayOfExpiration;

    @CreatedDate
    @JsonProperty
    private Timestamp createdDate;

    @LastModifiedDate
    @JsonProperty
    private Timestamp modifiedDate;

    @JsonProperty
    private String userId;

    @OneToOne
    @JsonProperty
    private CreditApplication creditApplication;

    public Offer() {
    }
}
