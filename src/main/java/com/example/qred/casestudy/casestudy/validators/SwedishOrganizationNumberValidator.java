package com.example.qred.casestudy.casestudy.validators;



import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SwedishOrganizationNumberValidator implements ConstraintValidator<SwedishOrganizationNumberValidation, String> {

    @Override
    public void initialize(SwedishOrganizationNumberValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String input, ConstraintValidatorContext constraintValidatorContext) {
        Pattern regexPattern = Pattern.compile("^(\\d{2}){0,1}(\\d{2})(\\d{2})(\\d{2})([-]?)?(\\d{4})$");
        Matcher matches = regexPattern.matcher(input);
        return matches.find();
    }
}
