# spring validator (hibernate-validator)

## Validation (JSR303, JSR 380)
Java에서는 2009년부터 Bean Validation이라는 데이터 유효성 검사 프레임워크를 제공하고 있다.  
Bean Validation은 Java Bean 검증을하기 위해 다양한 제약(Contraint)을 어노테이션(Annotation)으로 정의할 수 있게한다.  
- [JSR-303](https://beanvalidation.org/1.0/spec/) : Bean Validation 1.0
- [JSR-380](https://beanvalidation.org/2.0-jsr380/spec/) : Bean Validation 2.0

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

## 참조
- [Hibernate, validator](https://hibernate.org/validator/)
- [Validation 어디까지 해봤니?](https://meetup.toast.com/posts/223)