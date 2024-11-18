package com.cwa.springdatavalidation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;

import static java.util.Objects.isNull;

public class YearValidator implements ConstraintValidator<ValidYear, Integer> {

    private int minYear;

    @Override
    public void initialize(ValidYear constraintAnnotation) {
        this.minYear = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        if (isNull(value)) return false;

        return value >= minYear && value <= Year.now().getValue();
    }
}
