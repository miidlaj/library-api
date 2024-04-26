package com.midlaj.olikassigment.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MaxYearValidator.class)
public @interface MaxYear {
    String message() default "Year cannot give a value that is more than current year";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}