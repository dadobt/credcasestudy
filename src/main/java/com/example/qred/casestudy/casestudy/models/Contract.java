package com.example.qred.casestudy.casestudy.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "contract")
public class Contract implements Serializable {

    @Id
    @GeneratedValue
    @JsonProperty
    private Long id;

    @JsonProperty
    private String organizationName;

    @JsonProperty
    private String organizationNumber;

    @JsonProperty
    private String organizationType;

    @JsonProperty
    private BigDecimal amount;

    @JsonProperty
    private BigInteger term;

    @JsonProperty
    private BigDecimal interest;

    @JsonProperty
    private BigDecimal totalCommission;

    @JsonProperty
    private BigDecimal totalAmount;

    @CreatedDate
    @JsonProperty
    private Timestamp  dateOfSignature;

    @JsonProperty
    private String loanApplicant;

}
