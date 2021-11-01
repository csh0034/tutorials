# spring validator (hibernate-validator)

개발환경
- IntelliJ IDEA 2021.2.3
- spring boot 2.5.6
- Java 8
- Maven

pom.xml
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

Java Config
```java
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final MessageSource messageSource;

  @Override
  public Validator getValidator() {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource);
    return bean;
  }
}
```

> MessageSource 는 MessageSourceAutoConfiguration 이용

## [Validation](https://beanvalidation.org) (JSR303, JSR349, JSR 380)
Java에서는 2009년부터 Bean Validation이라는 데이터 유효성 검사 프레임워크를 제공하고 있다.  
Bean Validation은 Java Bean 검증을하기 위해 다양한 제약(Contraint)을 어노테이션(Annotation)으로 정의할 수 있게한다.

|Bean validation | Hibernate Validator (구현체) |
|---|---|
|1.0 ([JSR-303](https://beanvalidation.org/1.0/spec/)) | Hibernate Validator 4.3.1.Final|
|1.1 ([JSR-349](https://beanvalidation.org/1.1/spec/)) | Hibernate Validator 5.1.1.Final|
|2.0 ([JSR-380](https://beanvalidation.org/2.0/spec/)) | Hibernate Validator 6.0.1.Final|

## 유효성 검사 어노테이션
- @Null
- @NotNull
- @AssertTrue
- @AssertFalse
- @Min
- @Max
- @DecimalMin
- @DecimalMax
- @Negative
- @NegativeOrZero
- @Positive
- @PositiveOrZero
- @Size
- @Digits
- @Past
- @PastOrPresent
- @Future
- @FutureOrPresent
- @Pattern
- @NotEmpty
- @NotBlank
- @Email
- @Range
- ....

## [custom validator](https://beanvalidation.org/2.0-jsr380/spec/#constraintsdefinitionimplementation-validationimplementation-example)

### Constraint definition properties
- [message](https://beanvalidation.org/2.0-jsr380/spec/#validationapi-message-defaultmessageinterpolation) : 에러메세지를 생성하는데 사용됨
  - 기본 텍스트 사용
  - {} 를 사용하여 [MessageSource 적용](https://beanvalidation.org/2.0-jsr380/spec/#validationapi-message-examples), ex. {name.empty}
  - ${} 를 사용하여 [EL 적용](https://beanvalidation.org/2.0-jsr380/spec/#validationapi-message-defaultmessageinterpolation), ex. ${formatter.format('%1$.2f', validatedValue)}
- [groups](https://beanvalidation.org/2.0-jsr380/spec/#validationapi-validatorapi-groups) : 일반적으로 제약 조건이 평가되는 순서를 제어하거나 JavaBean의 부분 상태에 대한 유효성 검사를 수행하는 데 사용
  - 기본값은 빈 배열이어야 함
  - 그룹을 지정하지 않을 경우 Default 그룹으로 처리
- [payload](https://beanvalidation.org/2.0-jsr380/spec/#constraintsdefinitionimplementation-constraintdefinition-properties-payload) : 지정된 제약 조건 선언에 첨부할 수 있는 페이로드 유형, 심각도를 나타내는 정도로 사용 가능

> 위 3개의 프로퍼티중 하나라도 선언이 안되어 있을 경우 `ConstraintDefinitionException` 발생

### @NoSpecialCharacter
```java
@Documented
@Constraint(validatedBy = NoSpecialCharacter.NoSpecialCharacterValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface NoSpecialCharacter {

  String message() default "can't contain special character";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class NoSpecialCharacterValidator implements ConstraintValidator<NoSpecialCharacter, String> {

    private static final Pattern REGEX = Pattern.compile("[^a-zA-Z0-9\\s]");

    @Override
    public void initialize(NoSpecialCharacter constraintAnnotation) {
      // 어노테이션 값을 이용하여 초기화 작업시에 사용, default 메서드이기 때문에 구현 안해도됨
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
        return false;
      }

      return !REGEX.matcher(value).find();
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
```

### @Between, [Constraint composition](https://beanvalidation.org/2.0-jsr380/spec/#constraintsdefinitionimplementation-constraintcomposition)
- 숫자에 대해서 범위 체크 가능한 컴포지트 어노테이션 추가
- @ReportAsSingleViolation 선언할 경우 @Min 에서 걸리더라도 Between 에서 걸린걸로 처리됨
- @OverridesAttribute 사용시 컴포지트 어노테이션의 속성을 재정의 가능함
> @Range 쓰면됨...
```java
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
```

## 참조
- [Hibernate, validator](https://hibernate.org/validator/)
- [Validation 관련 블로그](https://meetup.toast.com/posts/223)