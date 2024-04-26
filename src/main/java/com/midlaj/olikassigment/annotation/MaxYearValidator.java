package com.midlaj.olikassigment.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;

public class MaxYearValidator implements ConstraintValidator<MaxYear, Integer> {


    @Override
    public void initialize(MaxYear constraintAnnotation) {
    }

    /**
     * Checking give value with current year
     * @param year
     * @param context
     * @return boolean
     */
    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext context) {
        if (year == null) {
            return true;
        }
        int currentYear = Year.now().getValue();
        return year <= currentYear;
    }
}