# spring-jpa-querydsl

## 이슈 사항

### 이슈 1
- spring boot 2.6.1 의 querydsl version 은 5.0.0 이다.
- QClass 생성시에 `javax.annotation.processing.Generated` 가 존재 하지 않음.
```java
import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

@Generated("com.querydsl.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {
```

- querydsl 4.4.0 버전으로 생성시
- `javax.annotation.Generated` 로 생성됨. 정상 동작
```java
import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

@Generated("com.querydsl.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {
```

### 이슈 2
- parent pom 에서 사용하는 버전을 override 하여 사용 시도
```xml
<properties>
  <querydsl.version>4.4.0</querydsl.version>
</properties>
```

- spring boot 2.6.x 부터 의존성 정의 방법이 pom 으로 변경됨
```xml
<dependency>
  <groupId>com.querydsl</groupId>
  <artifactId>querydsl-bom</artifactId>
  <version>${querydsl.version}</version>
  <type>pom</type>
  <scope>import</scope>
</dependency>
```

- 따라서 하단처럼 관련 의존성 모두 버전 명시 해야함
```xml
<dependency>
  <groupId>com.querydsl</groupId>
  <artifactId>querydsl-core</artifactId>
  <version>4.4.0</version>
</dependency>
<dependency>
  <groupId>com.querydsl</groupId>
  <artifactId>querydsl-codegen</artifactId>
  <version>4.4.0</version>
</dependency>
<dependency>
  <groupId>com.querydsl</groupId>
  <artifactId>querydsl-apt</artifactId>
  <version>4.4.0</version>
</dependency>
<dependency>
  <groupId>com.querydsl</groupId>
  <artifactId>querydsl-jpa</artifactId>
  <version>4.4.0</version>
</dependency>
```