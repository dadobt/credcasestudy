package com.example.qred.casestudy.casestudy.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;



@Entity
@Data
@Table(name = "credit_application")
@EntityListeners(AuditingEntityListener.class)
public class CreditApplication implements Serializable {

    @Id
    @GeneratedValue
    @JsonProperty
    private Long id;

    @JsonProperty
    private BigDecimal amountApplied;

    @JsonProperty
    private String applicationStatus;
    @JsonProperty
    private String email;
    @JsonProperty
    private String phoneNumber;
    @JsonProperty
    private String organizationNumber;


    @CreatedDate
    @JsonProperty
    private Timestamp createdDate;

    @LastModifiedDate
    @JsonProperty
    private Timestamp modifiedDate;

    @JsonProperty
    private String loanApplicant;

    public CreditApplication() {
    }
}
