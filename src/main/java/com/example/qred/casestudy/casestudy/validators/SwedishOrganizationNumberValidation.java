package com.example.qred.casestudy.casestudy.validators;


import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {SwedishOrganizationNumberValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SwedishOrganizationNumberValidation {

    String message() default "Swedish Organizatio Number Validation!";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
