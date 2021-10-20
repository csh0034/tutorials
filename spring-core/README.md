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
  - FactoryBean<Object> 구현 하였음
  - applicationContext.getBean("factoryBean");
    - FactoryBean 인터페이스의 구현체가 아닌 FactoryBean.getObject()에서 리턴되는 객체가 리턴
  - applicationContext.getBean("&factoryBean");
    - FactoryBean 자체가 리턴된다.

