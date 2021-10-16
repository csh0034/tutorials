package com.ask.springvalidator.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.regex.Pattern;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = NoSpecialCharacter.NoSpecialCharacterValidator.class)
public @interface NoSpecialCharacter {

  String message() default "can't contain special character";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class NoSpecialCharacterValidator implements ConstraintValidator<NoSpecialCharacter, String> {

    private static final Pattern REGEX = Pattern.compile("^[a-zA-Z0-9\\s]*$");

    @Override
    public void initialize(NoSpecialCharacter constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
        return false;
      }

      return REGEX.matcher(value).matches();
    }

    /* Use of ConstraintValidatorContext
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      context.disableDefaultConstraintViolation();

      if (REGEX.matcher(value).matches()) {
        context.buildConstraintViolationWithTemplate("{exist.special.character}").addConstraintViolation();
        return false;
      }

      return true;
    }
    */
  }
}
