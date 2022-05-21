# Spring Jasypt

```xml
<dependency>
  <groupId>com.github.ulisesbocchio</groupId>
  <artifactId>jasypt-spring-boot-starter</artifactId>
  <version>3.0.4</version>
</dependency>
```

## jasypt 란?

- Jasypt 는 개발자가 암호화 작동 방식에 대한 깊은 지식 없이도 최소한의 노력으로 자신의 프로젝트에   
  기본 암호화 기능을 추가할 수 있도록 하는 Java 라이브러리이다.

## jasypt-spring-boot

Jasypt Spring Boot 는 Spring Boot 환경에서 프로퍼티에 대한 암호화를 지원한다.
3가지의 통합 방법을 제공한다.

1. `jasypt-spring-boot-starter` 의존성을 추가하고 `@SpringBootApplication` 을 사용한다.
2. `jasypt-spring-boot` 의존성을 추가하고 `@EnableEncryptableProperties` 를 사용한다.
3. `jasypt-spring-boot` 의존성을 추가하고 `@EncrytablePropertySource` 에 암호화 프로퍼티 대상을 지정한다.

## 설정

`JasyptSpringBootAutoConfiguration` 에 의해 `EnableEncryptablePropertiesConfiguration` 가 자동으로  
등록되므로 `@EncryptablePropertySource` 을 선언하지 않아도 된다.

`EncryptablePropertyResolverConfiguration` 에서 `StringEncryptor` 의 구현체인   
`DefaultLazyEncryptor` 가 Bean 으로 등록되므로 별도 설정을 해주지 않아도 된다.

- DefaultLazyEncryptor : 사용자 정의 StringEncryptor 빈에 위임하거나 제공된 속성에 따라 기본 PooledPBEStringEncryptor   
  또는 SimpleAsymmetricStringEncryptor 를 생성하는 기본 Lazy Encryptor 이다.

`jasypt.encryptor.bean` 의 Default 값은 `jasyptStringEncryptor` 이며 해당 Bean 이 존재하지 않을 경우
StringEncryptorBuilder 사용하여 프로퍼티를 기반으로 `StringEncryptor` 구현체를 컴포지션으로 세팅한다.

> 정리: 별도 StringEncryptor Bean 을 설정하지 않고 property 설정을 통해 사용하는것이 좋다.  

## Custom StringEncryptor 설정

### application.yml 값을 기반으로 생성

```java
@Configuration
public class JasyptConfig {

  @Bean
  public StringEncryptor jasyptStringEncryptor(EnvCopy envCopy) {
    return new StringEncryptorBuilder(bindConfigProps(envCopy.get()), "jasypt.encryptor")
        .build();
  }
  
}
```

### 직접 필드 세팅하여 생성

```java
@Bean
public StringEncryptor jasyptStringEncryptor() {
  PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
  SimpleStringPBEConfig config = new SimpleStringPBEConfig();
  config.setPassword("password");
  config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
  config.setKeyObtentionIterations("1000");
  config.setPoolSize("1");
  config.setProviderName("SunJCE");
  config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
  config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
  config.setStringOutputType("base64");
  encryptor.setConfig(config);
  return encryptor;
}
```

## StringEncryptor 설정 로그

### 커스텀 Bean 을 찾지 못했을 경우 로그

```text
c.u.j.encryptor.DefaultLazyEncryptor     : String Encryptor custom Bean not found with name 'jasyptStringEncryptor'. Initializing Default String Encryptor
c.u.j.c.StringEncryptorBuilder           : Encryptor config not found for property jasypt.encryptor.algorithm, using default value: PBEWITHHMACSHA512ANDAES_256
c.u.j.c.StringEncryptorBuilder           : Encryptor config not found for property jasypt.encryptor.key-obtention-iterations, using default value: 1000
c.u.j.c.StringEncryptorBuilder           : Encryptor config not found for property jasypt.encryptor.pool-size, using default value: 1
c.u.j.c.StringEncryptorBuilder           : Encryptor config not found for property jasypt.encryptor.provider-name, using default value: null
c.u.j.c.StringEncryptorBuilder           : Encryptor config not found for property jasypt.encryptor.provider-class-name, using default value: null
c.u.j.c.StringEncryptorBuilder           : Encryptor config not found for property jasypt.encryptor.salt-generator-classname, using default value: org.jasypt.salt.RandomSaltGenerator
c.u.j.c.StringEncryptorBuilder           : Encryptor config not found for property jasypt.encryptor.iv-generator-classname, using default value: org.jasypt.iv.RandomIvGenerator
c.u.j.c.StringEncryptorBuilder           : Encryptor config not found for property jasypt.encryptor.string-output-type, using default value: base64
```

### 커스텀 Bean 을 찾았을 경우 로그

```text
c.u.j.encryptor.DefaultLazyEncryptor     : Found Custom Encryptor Bean org.jasypt.encryption.pbe.PooledPBEStringEncryptor@680bddf5 with name: jasyptStringEncryptor
```

## Default Encryption Algorithm

- jasypt-spring-boot 기준
- 2019/11/24, 3.0.0 Version Release 되면서 Default 가 `PBEWITHHMACSHA512ANDAES_256` 로 변경됨.
- 이전엔 `PBEWithMD5AndDES` 였음

이전 버전을 유지하기위해선 하단과 같이 프로퍼티 설정 해야함.

```yaml
jasypt:
  encryptor:
    algorithm: PBEWithMD5AndDES
    iv-generator-classname: org.jasypt.iv.NoIvGenerator
```

### PBEWITHHMACSHA512ANDAES_256 이란?

PBKDF2를 적용하여 키를 생성하며 암호화는 AES-256, CBC 를 사용한다.  
RandomSaltGenerator 와 RandomIvGenerator 를 사용할 경우 암호화 될때 사용되며  
결과값에 salt 와 iv key 가 포함된다. 따라서 복호화시에 이 값을 추출하여 진행한다.

- PBE(Password based Encryption): 사용자가 제공하는 패스워드(key)를 기반으로 비밀키를 생성
- PBKDF2: 사용자가 제공하는 패스워드(key)를 기반으로 키를 유도하기 위한 대표적인 함수

> PBKDF2 를 사용해 password 로 부터 키를 유도하여 AES-256 을 통해 암호화한다.

## jasypt-maven-plugin

- property 설정을 기반으로 `DefaultLazyEncryptor` 를 사용한다.

```xml
<build>
  <plugins>
    <plugin>
      <groupId>com.github.ulisesbocchio</groupId>
      <artifactId>jasypt-maven-plugin</artifactId>
      <version>3.0.4</version>
    </plugin>
  </plugins>
</build>
```

### Value Encryption and Decryption

```shell
$ mvn jasypt:encrypt-value -Djasypt.encryptor.password="the password" -Djasypt.plugin.value="theValueYouWantToEncrypt"
$ ENC(Zb7f1jYujoqaS6Eq3tKTXoSFb227kebXTHEgWKHxMsD70L6/+oSjX0MhjhHC23Ke6JKawBIPxsJCnZlqOqmFKw==)

$ mvn jasypt:decrypt-value -Djasypt.encryptor.password="the password" -Djasypt.plugin.value="Zb7f1jYujoqaS6Eq3tKTXoSFb227kebXTHEgWKHxMsD70L6/+oSjX0MhjhHC23Ke6JKawBIPxsJCnZlqOqmFKw=="
$ theValueYouWantToEncrypt
```

### Property File Encryption and Decryption

#### Encryption

- `-Djasypt.plugin.path` default 값, `file:src/main/resources/application.properties`

```properties
# 실행전, file:src/main/resources/sample.properties
sensitive.password=DEC(secret value)
regular.property=example
```

```shell
$ mvn jasypt:encrypt -Djasypt.encryptor.password="the password" -Djasypt.plugin.path="file:src/main/resources/sample.properties"
```

```properties
# 실행후, 파일 자체가 변경되어 있음
sensitive.password=ENC(fqX+g+v8/yC0nauRYwK9qYXYwERzNm08oFJGvPYZyIxakrws6/e2bgSynWRfyFTZ)
regular.property=example
```

#### Decryption

```properties
# 실행전, file:src/main/resources/sample.properties
sensitive.password=ENC(fqX+g+v8/yC0nauRYwK9qYXYwERzNm08oFJGvPYZyIxakrws6/e2bgSynWRfyFTZ)
regular.property=example
```

```shell
$ mvn jasypt:decrypt -Djasypt.encryptor.password="the password" -Djasypt.plugin.path="file:src/main/resources/sample.properties"
```

```properties
# 실행후, 파일 자체가 변경되진 않고 콘솔에 출력됨
sensitive.password=DEC(secret value)
regular.property=example
```

- 파일자체를 변경하지 않는 이유는 복호화의 경우 값을 영구적으로 복호화 하기보단 확인하고자 실행하는 경우가 크며  
  decryption 된 값을 형상관리에 올리는 실수를 줄이기 위해서이다.

## 참조

- [Reference, jasypt](http://www.jasypt.org/)
- [GitHub, jasypt-spring-boot](https://github.com/ulisesbocchio/jasypt-spring-boot)
