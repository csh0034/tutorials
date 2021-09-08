# spring jpa envers

개발환경
- IntelliJ IDEA 2021.2.1
- spring boot 2.5.4
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

[spring data envers github issue](https://github.com/spring-projects/spring-data-envers/issues/250)

***
## 참조
[hibernate document](https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#envers-configuration)