package com.ask.springvalidator.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = { })
@ReportAsSingleViolation
@Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$")
public @interface Phone {

  String message() default "Invalid phone number";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
