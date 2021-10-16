package com.ask.springvalidator.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = { })
@ReportAsSingleViolation
@Min(0)
@Max(100)
public @interface Between {

  String message() default "size must be between {min} and {max}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  @OverridesAttribute(constraint = Min.class, name = "value")
  long min() default 0;

  @OverridesAttribute(constraint = Max.class, name = "value")
  long max() default 100;
}
