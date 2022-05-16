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
@TypeDef(
    name = JasyptConfig.ENCRYPTOR_NAME,
    typeClass = EncryptedStringType.class,
    parameters = {
        @Parameter(name = ParameterNaming.ENCRYPTOR_NAME, value = JasyptConfig.ENCRYPTOR_REGISTERED_NAME)
    }
)
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class User {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "user_id")
  private String id;

  private String name;

  @Type(type = JasyptConfig.ENCRYPTOR_NAME)
  private String data;

  public static User create(String name, String data) {
    User user = new User();
    user.name = name;
    user.data = data;
    return user;
  }

}
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

## 참조

- [Reference, jasypt hibernate](http://www.jasypt.org/hibernate.html)
