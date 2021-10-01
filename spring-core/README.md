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