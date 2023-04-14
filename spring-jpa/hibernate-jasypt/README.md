# Hibernate Jasypt

```xml
<dependency>
  <groupId>org.jasypt</groupId>
  <artifactId>jasypt-hibernate5</artifactId>
  <version>1.9.3</version>
</dependency>
```

## Hibernate Jasypt 란?

Jasypt 와 Hibernate 의 통합을 지원하여 엔티티의 지정한 컬럼에 대해서   
데이터 저장 및 조회 시점에 자동으로 암호화, 복호화를 지원한다.

하단과 같은 설정이 필요하다.

1. HibernatePBEStringEncryptor Bean 선언
2. Entity 에 @TypeDef 설정 및 @Type 지정

## Configuration

```yaml
# spring boot 설정 프로퍼티 생략
jasypt:
  password: b71c6f52
```

```java
@ConfigurationProperties("jasypt")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
@ToString
public class JasyptProperties {

  private final String password;

}
```

```java
@Configuration
@RequiredArgsConstructor
public class JasyptConfig {

  public static final String ENCRYPTOR_NAME = "encryptedString";
  public static final String ENCRYPTOR_REGISTERED_NAME = "hibernateStringEncryptor";

  private final JasyptProperties jasyptProperties;

  @Bean
  public HibernatePBEStringEncryptor hibernateStringEncryptor() {
    HibernatePBEStringEncryptor hibernateStringEncryptor = new HibernatePBEStringEncryptor();
    hibernateStringEncryptor.setRegisteredName(ENCRYPTOR_REGISTERED_NAME);
    hibernateStringEncryptor.setEncryptor(lenientPBStringEncryptor());
    return hibernateStringEncryptor;
  }

  private PBEStringEncryptor lenientPBStringEncryptor() {
    PooledPBEStringEncryptor stringEncryptor = new PooledPBEStringEncryptor();
    stringEncryptor.setAlgorithm("PBEWithMD5AndTripleDES");
    stringEncryptor.setPassword(jasyptProperties.getPassword());
    stringEncryptor.setPoolSize(4);
    return new LenientPBStringEncryptor(stringEncryptor);
  }

}
```

```java
@Entity
@Table(name = "tb_user")
@TypeDefs({
    @TypeDef(
        name = "encryptedString",
        typeClass = EncryptedStringType.class,
        parameters = {
            @Parameter(name = ParameterNaming.ENCRYPTOR_NAME, value = JasyptConfig.ENCRYPTOR_REGISTERED_NAME)
        }
    ),
    @TypeDef(
        name = "encryptedIntegerAsString",
        typeClass = EncryptedIntegerAsStringType.class,
        parameters = {
            @Parameter(name = ParameterNaming.ENCRYPTOR_NAME, value = JasyptConfig.ENCRYPTOR_REGISTERED_NAME)
        }
    ),
    @TypeDef(
        name = "encryptedDateAsString",
        typeClass = EncryptedDateAsStringType.class,
        parameters = {
            @Parameter(name = ParameterNaming.ENCRYPTOR_NAME, value = JasyptConfig.ENCRYPTOR_REGISTERED_NAME)
        }
    )
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class User {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "user_id")
  private String id;

  private String name;

  @Type(type = "encryptedString")
  private String data;

  @Type(type = "encryptedIntegerAsString")
  private Integer count;

  @Type(type = "encryptedDateAsString")
  private Date createdDt;

  public static User create(String name, String data) {
    User user = new User();
    user.name = name;
    user.data = data;
    user.count = 1000000;
    user.createdDt = new Date();
    return user;
  }

}
```

### 생성된 테이블 

- java Integer 타입의 count 컬럼이 varchar(255) 로 생성된다.
- java Date 타입의 created_dt 컬럼이 varchar(255) 로 생성된다.

```text
create table tb_user (
   user_id varchar(255) not null,
    count varchar(255),
    created_dt varchar(255),
    data varchar(255),
    name varchar(255),
    primary key (user_id)
)
```

## Troubleshooting

### DB 값 암호화 안되었을 경우 예외 발생

EncryptionOperationNotPossibleException 발생함.  
따라서 decrypt 시에 예외를 무시하도록 처리하여 해결

```java
@Slf4j
@RequiredArgsConstructor
public class LenientPBStringEncryptor implements PBEStringCleanablePasswordEncryptor {

  private final PooledPBEStringEncryptor pooledPBEStringEncryptor;

  @Override
  public String encrypt(String message) {
    return pooledPBEStringEncryptor.encrypt(message);
  }

  @Override
  public String decrypt(String encryptedMessage) {
    try {
      return pooledPBEStringEncryptor.decrypt(encryptedMessage);
    } catch (Exception ex) {
      log.warn("Decryption Failed, encryptedMessage: {}", encryptedMessage);
      return encryptedMessage;
    }
  }

  @Override
  public void setPasswordCharArray(char[] password) {
    pooledPBEStringEncryptor.setPasswordCharArray(password);
  }

  @Override
  public void setPassword(String password) {
    pooledPBEStringEncryptor.setPassword(password);
  }

}
```

### @Lob 의 경우에도 varchar(255) 로 생성됨

`@Type` 으로 처리시  `@Lob` 을 인식하지 않아 varchar(255) 로 생성됨  
columnDefinition 을 사용하는 방법도 있지만 db vendor 에 종속적이게됨

해결방법: @Converter 사용

## 참조

- [Reference, jasypt hibernate](http://www.jasypt.org/hibernate.html)
