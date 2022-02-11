# Spring Core

## List, Not Configured Bean Injection
### 빈주입시에 NULL 처리
빈생성시에 **DependencyDescriptor.isRequired()** 에서 필수 여부를 체크하여  
`Optional`, `@Nullable`, `@Autowired(required=false)` 일 경우 Optional.empty, null 로 생성  
그외 해당빈이 없을 경우 `NoSuchBeanDefinitionException`

### List 로 선언시
- 빈이 있을 경우
  - 제네릭 타입의 빈을 모두 Injection
- 빈이 없을 경우
  - 생성 주입의 경우 ArrayList size 0
  - @Autoried 필드 주입의 경우 null

> 한가지 타입에 여러 Bean의 주입이 필요한 경우 ObjectProvider 를 사용하거나 List 생성자 주입이 나은 방법으로 보임

```java
@Configuration
@RequiredArgsConstructor
public class InjectConfig {
  
  private final List<ComponentInterface> componentInterfaces; // ArrayList size 2
  private final ObjectProvider<ComponentInterface> componentObjectProvider; // DependencyObjectProvider

  private final List<NotComponent> notComponents; // ArrayList size 0
  private final ObjectProvider<NotComponent> notComponentObjectProvider; // DependencyObjectProvider
  
  private final NotComponent notComponent0; // NoSuchBeanDefinitionException!!

  @Autowired
  private NotComponent notComponent1; // NoSuchBeanDefinitionException!!
  
  @Nullable
  private final NotComponent notComponent2; // null

  private final Optional<NotComponent> notComponent3; // optional.empty

  @Nullable
  @Autowired
  private NotComponent notComponent4; // null

  @Autowired(required = false)
  private NotComponent notComponent5; // null

  @Autowired
  private Optional<NotComponent> notComponent6;// optional.empty
  
  @Nullable
  @Autowired
  private List<NotComponent> notComponentsAutowiredWithNullable; // null

  @Autowired(required = false)
  private List<NotComponent> notComponentsAutowiredWithFalse; // null
}
```

## [Conditional](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration.condition-annotations)
SpringBoot는 @Conditional을 확장하여, 여러가지 어노테이션을 제공한다.
- @ConditionalOnWebApplication : 프로젝트가 웹 애플리케이션이면 Bean 등록
- @ConditionalOnBean: 해당 Bean이 존재하면 자동 설정 등록
- @ConditionalOnMissingBean: 해당 Bean이 존재하지 않으면 자동설정 등록
- @ConditionalOnClass: 해당 클래스가 존재하면 자동설정 등록
- @ConditionalOnMissingClass: 해당 클래스가 클래스 패스에 존재하지 않으면 Bean 등록
- @ConditionalOnResource: 해당 자원(file 등)이 존재하면 자동설정 등록
- @ConditionalOnProperty: 설정한 프로퍼티가 존재하면 자동설정 등록
- @ConditionalOnExpression : SPEL 을 사용한 검증
- @ConditionalOnSingleCandidate : 해당 타입의 Bean이 하나일 경우 등록
- org.springframework.boot.autoconfigure.condition package annotation 확인
```java
@ConditionalOnProperty(value = "conditional.enabled", havingValue = "true")

// SPEL 
// #{} 자동으로 추가 
// ${} Environment 값으로 치환
@ConditionalOnExpression("${conditional.enabled:false} and '${conditional.name}'.equals('ask')")
```

## MethodInvoke
- MethodInvoker
  - 정적이든 비정적이든 선언적 방식으로 호출할 메서드를 지정할 수 있는 Helper 클래스
- MethodInvokingBean
  - method invoking 후에 결과를 어플리케이션 컨텍스트에 등록하지 않는다.
- MethodInvokingFactoryBean
  - method invoking 후에 결과를 어플리케이션 컨텍스트에 등록한다.
  - `FactoryBean<Object>` 구현 하였음
  - applicationContext.getBean("factoryBean");
    - FactoryBean 인터페이스의 구현체가 아닌 FactoryBean.getObject()에서 리턴되는 객체가 리턴
  - applicationContext.getBean("&factoryBean");
    - FactoryBean 자체가 리턴된다.

## Locale 
spring-webmvc-5.3.12기준

### LocaleResolver

|타입|설명|
|---|---|
|AcceptHeaderLocaleResolver|웹 브라우저가 전송한 Accept-Language 헤더로부터 Locale을 선택|
|CookieLocaleResolver|쿠키를 이용해서 Locale 정보 구함. setLocale() 메소드는 쿠키에 Locale 정보를 저장함|
|SessionLocaleResolver|세션으로부터 Locale 정보를 구함. setLocale() 메소드는 세션에 Locale 정보를 저장함|
|FixedLocaleResolver|고정 Locale을 사용함. setLocale() 메소드가 없음|

### 기본 설정
> request.getLocale() : Accept-Language 헤더를 기반  
> Locale.getDefault() : JVM 의 이 인스턴스에 대한 기본 로케일의 현재 값
1. RequestContextFilter
   - initContextHolders
   - `LocaleContextHolder.setLocale(request.getLocale(), this.threadContextInheritable);`
2. DispatcherServlet
   - buildLocaleContext 
   - `((LocaleContextResolver) lr).resolveLocaleContext(request);`
   - 리턴 값 LocaleContextHolder 에 저장

### 스프링 부트 Auto Configuration
`WebMvcAutoConfiguration`
```java
@Override
@Bean
@ConditionalOnMissingBean(name = DispatcherServlet.LOCALE_RESOLVER_BEAN_NAME)
@SuppressWarnings("deprecation")
public LocaleResolver localeResolver() {
  if (this.webProperties.getLocaleResolver() == WebProperties.LocaleResolver.FIXED) {
    return new FixedLocaleResolver(this.webProperties.getLocale());
  }
  if (this.mvcProperties.getLocaleResolver() == WebMvcProperties.LocaleResolver.FIXED) {
    return new FixedLocaleResolver(this.mvcProperties.getLocale());
  }
  AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
  Locale locale = (this.webProperties.getLocale() != null) ? this.webProperties.getLocale()
    : this.mvcProperties.getLocale();
  localeResolver.setDefaultLocale(locale);
  return localeResolver;
}
```

```yaml
spring:
  web:
    locale-resolver: accept_header or fixed # accept_header default
    locale: ko # Accept-Language 헤더가 없는 경우 대체할 고정 기본 로케일을 구성
```

## @DateTimeFormat, @JsonFormat
@DateTimeFormat
- Spring annotation
- @RequestParam, @ModelAttribute 에서 사용 가능
- GET, POST 요청시 사용 가능
  - POST 일 경우 @JsonFormat 이 없어야함

@JsonFormat
- jackson annotation
- POST RequestBody, ResponseBody 에서의 date format 에 적용됨

하단 방식은 Java8 날짜 유형(LocalDate, LocalDateTime..) 엔 적용이 안된다.  
Date, Calendar 에 적용됨
```yaml
spring:
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
```

### 추가 사항
ObjectMapper 생성이 AutoConfiguration 으로 될 경우 `JacksonAutoConfiguration` 에서 처리한다.  

Jackson2ObjectMapperBuilder.build() 를 호출 하여 생성하는데 해당 코드를 보면  

```java
// Jackson2ObjectMapperBuilder.configure()
if (this.findWellKnownModules) {
  registerWellKnownModulesIfAvailable(modulesToRegister);
}
```

클래스패스에 하단 클래스가 존재할경우 ObjectMapper Module 에 등록한다.

- com.fasterxml.jackson.datatype.jdk8.Jdk8Module
- com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
- org.joda.time.YearMonth -> com.fasterxml.jackson.datatype.joda.JodaModule
- com.fasterxml.jackson.module.kotlin.KotlinModule

그중 `JavaTimeModule` 이 Java8 날짜 유형에 대한 Serializer 및 Deserializer 를 등록한다 따라서  
ObjectMapper AutoConfiguration 을 사용할 경우 Java8 날짜 유형은 Json 처리시에 @JsonFormat 없이 처리 가능하다.

> 기본적으로 ISO 8601 포맷을 따르는데 변경하려면 @JsonFormat 을 사용 해야 할듯하다. 

### Default Property 변경
`JacksonAutoConfiguration` 최상단을 보면 하단 프로퍼티 디폴트를 변경해 두었다.
```java
featureDefaults.put(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
featureDefaults.put(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
```

### 참조
- [spring-boot-formatting-json-dates](https://www.baeldung.com/spring-boot-formatting-json-dates) 
