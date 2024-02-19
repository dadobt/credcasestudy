package com.example.qred.casestudy.casestudy.dtos;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CreditApplicationStatus {
    PROCESSED("PROCESSED"),
    PENDING("PENDING"),
    CANCELLED("CANCELLED"),
    SIGNED("SIGNED");

    private String value;

    CreditApplicationStatus(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }
}
