package com.example.qred.casestudy.casestudy.dtos;

import com.example.qred.casestudy.casestudy.contsants.Constants;
import com.example.qred.casestudy.casestudy.validators.SwedishOrganizationNumberValidation;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.io.Serializable;

@Data
public class CreditApplicationDTO implements Serializable {

    @NotNull
    @Max(value = Constants.MAX_AMOUNT_CREDIT,message ="Can be Maximum 250000")
    @Min(value = Constants.MIN_AMOUNT_CREDIT,message ="must be minimum 10000")
    private Long amountApplied;

    @NotEmpty
    @Email(message = "Email must be valid")
    private String email;

    @NotEmpty
    private String phoneNumber;

    @NonNull
    @SwedishOrganizationNumberValidation
    private String organizationNumber;

    public CreditApplicationDTO() {
    }

}
