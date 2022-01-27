# spring jpa envers

개발환경
- spring boot 2.6.3
- Java 8
- Maven
- MariaDB 10.4.20

pom.xml
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.data</groupId>
  <artifactId>spring-data-envers</artifactId>
</dependency>
<dependency>
  <groupId>org.mariadb.jdbc</groupId>
  <artifactId>mariadb-java-client</artifactId>
</dependency>
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <scope>runtime</scope>
</dependency>
```

Java Config
```java
@SpringBootApplication
@EnableEnversRepositories // since 2.5
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class) // before 2.5
public class SpringJpaEnversApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringJpaEnversApplication.class, args);
  }

}

```

application.yml
```yaml
spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/envers
    username: root
    password: 111111

  jpa:
    hibernate:
      ddl-auto: update
    properties:
      org:
        hibernate:
          envers:
            audit_table_suffix: _h          # audit table suffix 세팅
            store_data_at_delete: true      # 엔터티가 삭제될 때 엔터티 데이터를 저장
            global_with_modified_flag: true # 모든 audit entity 및 모든 속성에 대해 modified_flag 저장
            modified_flag_suffix: _mod      # modified_flag suffix 세팅
            #do_not_audit_optimistic_locking_field: false
      hibernate:
        show_sql: false
        format_sql: true
    open-in-view: false

logging:
  level:
    "[org.hibernate.SQL]": debug
    "[org.hibernate.type.descriptor.sql.BasicBinder]": trace
```

***
## Spring Data Envers  
Entity가 변경 된 전체 **이력을 관리**하는 History 기능  
- 변경 이력을 추적 할 Entity에 `@Audited` 애노테이션을 추가 할 경우 Entity가 영속화 될 때마다  
  History 테이블이 자동으로 관리되며 각 Entity에 매칭되는 이력 관리용 테이블이 추가생성된다.
- Audit 대상에서 특정 컬럼을 제외 할 경우 entity 필드에 `@NotAudited` 추가
- 연관 관계 대상 테이블이 Envers를 적용하지 않았을 경우 `@Audited(targetAuditMode = NOT_AUDITED)` 추가

***
## Revision 테이블 rev 타입 변경
```java
@Entity
@Getter
@RevisionEntity
@Table(name = "revinfo")
public class Revision implements Serializable {

  private static final long serialVersionUID = 7703526253442389993L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @RevisionNumber
  private Long rev;

  @RevisionTimestamp
  private Long timestamp;
}
```

***
## 이슈

### Custom RevisionEntity 추가시에 metadata 조회 안됨
@RevisionEntity 선언시에 RevisionRepository 로 조회하면  
@RevisionNumber, @RevisionTimestamp 필드 값 출력안됨
AnnotationRevisionMetadata 에서 detectAnnotation 메서드에서 필드값을 바로 조회함  
entity가 프록시상태로 가져오므로 필드를 바로 조회하면 안될듯....

```java
private static <T> Lazy<Optional<T>> detectAnnotation(Object entity, Class<? extends Annotation> annotationType) {

  return Lazy.of(() -> {

    AnnotationDetectionFieldCallback callback = new AnnotationDetectionFieldCallback(annotationType);
    ReflectionUtils.doWithFields(entity.getClass(), callback);
    return Optional.ofNullable(callback.getValue(entity));
  });
}
```
new AnnotationDetectionFieldCallback(annotationType);  
에서 org.hibernate.envers.RevisionNumber 값을 세팅 하고  
ReflectionUtils.doWithFields(entity.getClass(), callback); 메서드로 타겟 필드를 세팅함  
callback.getValue(entity)) 메서드는 실행시 프록시 객체가아닌 원래 객체의 값 호출함 

((Revision) entity).id : null  
((Revision) entity).getId() : 실제 데이터 값 나옴.  

**해결방법**  
프록시로 조회하지 않도록 `@Proxy(lazy = false)` 추가  
> **적용시 이슈** : 프록시를 진짜 객체로 조회하기 위해 n + 1 문제 발생함  
> [관련 사항 spring data envers github issue 추가](https://github.com/spring-projects/spring-data-envers/issues/313)

[spring data envers github issue](https://github.com/spring-projects/spring-data-envers/issues/250)

***
## Listener 를 통한 로깅  
EntityManagerFactory 빈생성 이후에 EventListenerRegistry 에 로깅을 처리하는 리스너를 등록

리스너 등록
```java
@Configuration
@RequiredArgsConstructor
public class JpaConfig  {

  private final EntityManagerFactory emf;

  private final CompanyLogService companyLogService;

  @PostConstruct
  public void init() {
    SessionFactoryImpl sessionFactory = emf.unwrap(SessionFactoryImpl.class);
    EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
    CompanyLogWriteListener logWriteListener = new CompanyLogWriteListener(companyLogService);

    registry.appendListeners(EventType.POST_INSERT, logWriteListener);
    registry.appendListeners(EventType.POST_UPDATE, logWriteListener);
    registry.appendListeners(EventType.POST_DELETE, logWriteListener);
  }
} 
```

리스너에서 로깅테이블 저장
```java
@RequiredArgsConstructor
@Slf4j
public class CompanyLogWriteListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

  private static final long serialVersionUID = 7876932731944094153L;

  private final CompanyLogService companyLogService;

  @Override
  public void onPostInsert(PostInsertEvent event) {
    final Object entity = event.getEntity();

    if (entity instanceof Company) {
      log.info("Company onPostInsert");
    }
  }

  @Override
  public void onPostUpdate(PostUpdateEvent event) {
    final Object entity = event.getEntity();

    if (entity instanceof Company) {
      log.info("Company onPostUpdate");
    }
  }

  @Override
  public void onPostDelete(PostDeleteEvent event) {
    final Object entity = event.getEntity();

    if (entity instanceof Company) {
      log.info("Company onPostDelete");
    }
  }

  @Override
  public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
    return true;
  }
}
```

***
## Hibernate Listener vs Hibernate Interceptor  
> HibernatePropertiesCustomizer를 사용하여 등록할 경우 JpaRepository 관련 의존성은 주입못함  
> JpaRepository 생성시 EntityManagerFactory 가 필요하여 HibernatePropertiesCustomizer 은 EntityManagerFactory   
> 빈생성시 호출되므로 순환 참조임

### Hibernate Listener  
- org.hibernate.event.spi.*Listener 인터페이스 중 하나를 구현
- 여러개 등록 가능하며 구현 메서드가 간결함
- [Listener 를 통한 로깅](#listener-를-통한-로깅)

application.yml 로 설정 방법
> 이때 Integrator 를 상속 받아 구현 하며 캐스팅 타입이 IntegratorProvider 이므로 둘다 구현해야함  
> EntityManagerFactoryBuilderImpl applyIntegrationProvider 에서 처리함
```yaml
spring:
  jpa:
    properties:
      hibernate:
        integrator_provider: com.ask.springjpaenvers.listener.TestIntegrator
```
```java
@Slf4j
public class TestIntegrator implements Integrator, IntegratorProvider {

  @Override
  public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory,
      SessionFactoryServiceRegistry serviceRegistry) {
    log.info("call TestIntegrator integrate");
  }

  @Override
  public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
  }

  @Override
  public List<Integrator> getIntegrators() {
    return Collections.singletonList(new TestIntegrator());
  }
}
```

bean 을 등록하며 설정 방법
```java
@Bean
public HibernatePropertiesCustomizer customizer() {
  return (properties) -> properties.put("hibernate.integrator_provider", 
    (IntegratorProvider) () -> Collections.singletonList(testIntegrator()));
}

@Bean
public TestIntegrator testIntegrator() {
  return new TestIntegrator();
}
```


### Hibernate Interceptor  
- org.hibernate.EmptyInterceptor 상속 받아 사용
- 코드가 적고 구성이 비교적 간단하지만 전체 애플리케이션에 대해 하나만 가질 수 있다

application.yml 로 설정 방법
```yaml
spring:
  jpa:
    properties:
      hibernate:
        session_factory:
          interceptor: my.package.MyInterceptor
```

bean 을 등록하며 설정 방법
```java
@Bean
public HibernatePropertiesCustomizer customizer() {
  return (properties) -> properties.put("hibernate.session_factory.interceptor", hibernateInterceptor());
}

@Bean
public EmptyInterceptor hibernateInterceptor() {
  return new MyInterceptor();
}
```

***
## 참조
[hibernate document](https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#envers-configuration)
