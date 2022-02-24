# spring test core

## 개발환경
- spring boot 2.6.3
- Java 8
- Maven

## [Dependency Injection of Test Fixtures](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#testcontext-fixture-di)
- spring 에서 테스트 실행시에 인젝션은 `DependencyInjectionTestExecutionListener` 가 처리한다.
  - spring boot 의 경우 `SpringBootDependencyInjectionTestExecutionListener` 
- 프로덕션 코드에서는 생성자 주입 사용이 권장이지만 테스트 코드에선 기본 @Autowired 필드 주입을 사용한다.
  - [Constructor Injection](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#testcontext-junit-jupiter-di-constructor) 방식으로도 변경 가능하다.

> 필드 주입 방식이 테스트 코드에서 자연스러운 이유는 테스트 클래스를 직접 인스턴스화 하지 않기 때문이다.  
  Spring Framework 이 아닌 JUnit 에서 인스턴스화를 한다.

```java
@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@RequiredArgsConstructor
@Slf4j
class ConstructorInjectionTest {

  private final SampleService sampleService;

  @Test
  void test() {
    // do something...
  }
}
```

## [Context Caching](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#testcontext-ctx-management-caching)

### TestContext 캐싱

TestContext 프레임워크가 테스트에 대한 ApplicationContext(또는 WebApplicationContext)을 로드하면 해당 컨텍스트가  
캐싱되어 동일한 테스트 스위트 내에서 동일한 고유 컨텍스트 구성을 선언하는 모든 후속 테스트에 재사용된다.

캐싱이 작동하는 방식을 이해하려면 "고유"및 "테스트 스위트"가 의미하는 바를 이해하는 것이 중요하다.

TestContext 프레임워크는 하단 매개변수를 사용하여 컨텍스트 캐시키 (MergedContextConfiguration) 를 생성한다.

- locations (from @ContextConfiguration)
- classes (from @ContextConfiguration)
- contextInitializerClasses (from @ContextConfiguration)
- contextCustomizers (from ContextCustomizerFactory) – this includes @DynamicPropertySource methods as well as various features from Spring Boot’s testing support such as @MockBean and @SpyBean.
- contextLoader (from @ContextConfiguration)
- parent (from @ContextHierarchy)
- activeProfiles (from @ActiveProfiles)
- propertySourceLocations (from @TestPropertySource)
- propertySourceProperties (from @TestPropertySource)
- resourceBasePath (from @WebAppConfiguration)

주로 사용할만한 사항
- @ActiveProfiles
- @MockBean, @SpyBean (MockitoContextCustomizer)
- classes (from @ContextConfiguration)
- @TestPropertySource 및 @SpringBootTest(properties = "")

### [@DirtiesContext](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-testing-annotation-dirtiescontext)
- `@DirtiesContext` 는 테스트를 실행하는 동안 ApplicationContext 가 변경됐음을 나타낸다.
  - ex) 싱글톤 빈의 상태를 변경시킴
- ApplicationContext 가 더티로 표시되면 테스트 프레임워크의 캐시에서 제거된다.
- 동일 구성에 대해서도 후속 테스트에서 다시 ApplicationContext 를 생성한다.

```java
@SpringBootTest
@DirtiesContext
@Slf4j
class DirtiesContextTest {

  @Test
  void test() {
    log.info("invoke DirtiesContextTest.test");
  }
}
```

### 캐싱 처리 코드
DefaultCacheAwareContextLoaderDelegate
```java
package org.springframework.test.context.cache;

public class DefaultCacheAwareContextLoaderDelegate implements CacheAwareContextLoaderDelegate {
  // ...
  private final ContextCache contextCache;
  
  @Override
  public ApplicationContext loadContext(MergedContextConfiguration mergedContextConfiguration) {
    synchronized (this.contextCache) {
      ApplicationContext context = this.contextCache.get(mergedContextConfiguration);
      if (context == null) {
        try {
          context = loadContextInternal(mergedContextConfiguration);
          if (logger.isDebugEnabled()) {
            logger.debug(String.format("Storing ApplicationContext [%s] in cache under key [%s]",
                System.identityHashCode(context), mergedContextConfiguration));
          }
          this.contextCache.put(mergedContextConfiguration, context);
        }
        catch (Exception ex) {
          throw new IllegalStateException("Failed to load ApplicationContext", ex);
        }
      }
      else {
        if (logger.isDebugEnabled()) {
          logger.debug(String.format("Retrieved ApplicationContext [%s] from cache with key [%s]",
              System.identityHashCode(context), mergedContextConfiguration));
        }
      }

      this.contextCache.logStatistics();

      return context;
    }
  }
  // ... 
}
```

ContextCache
```java
package org.springframework.test.context.cache;

public class DefaultContextCache implements ContextCache {
  // ...
  private final Map<MergedContextConfiguration, ApplicationContext> contextMap =
      Collections.synchronizedMap(new LruCache(32, 0.75f));
  // ...
}
```

MergedContextConfiguration
```java
package org.springframework.test.context;

public class MergedContextConfiguration implements Serializable {
  // ...
  @Override
  public boolean equals(@Nullable Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || other.getClass() != getClass()) {
      return false;
    }

    MergedContextConfiguration otherConfig = (MergedContextConfiguration) other;
    if (!Arrays.equals(this.locations, otherConfig.locations)) {
      return false;
    }
    if (!Arrays.equals(this.classes, otherConfig.classes)) {
      return false;
    }
    if (!this.contextInitializerClasses.equals(otherConfig.contextInitializerClasses)) {
      return false;
    }
    if (!Arrays.equals(this.activeProfiles, otherConfig.activeProfiles)) {
      return false;
    }
    if (!Arrays.equals(this.propertySourceLocations, otherConfig.propertySourceLocations)) {
      return false;
    }
    if (!Arrays.equals(this.propertySourceProperties, otherConfig.propertySourceProperties)) {
      return false;
    }
    if (!this.contextCustomizers.equals(otherConfig.contextCustomizers)) {
      return false;
    }

    if (this.parent == null) {
      if (otherConfig.parent != null) {
        return false;
      }
    }
    else if (!this.parent.equals(otherConfig.parent)) {
      return false;
    }

    if (!nullSafeClassName(this.contextLoader).equals(nullSafeClassName(otherConfig.contextLoader))) {
      return false;
    }

    return true;
  }
  
  @Override
  public int hashCode() {
    int result = Arrays.hashCode(this.locations);
    result = 31 * result + Arrays.hashCode(this.classes);
    result = 31 * result + this.contextInitializerClasses.hashCode();
    result = 31 * result + Arrays.hashCode(this.activeProfiles);
    result = 31 * result + Arrays.hashCode(this.propertySourceLocations);
    result = 31 * result + Arrays.hashCode(this.propertySourceProperties);
    result = 31 * result + this.contextCustomizers.hashCode();
    result = 31 * result + (this.parent != null ? this.parent.hashCode() : 0);
    result = 31 * result + nullSafeClassName(this.contextLoader).hashCode();
    return result;
  }
}
```

WebMergedContextConfiguration
```java
package org.springframework.test.context.web;

public class WebMergedContextConfiguration extends MergedContextConfiguration {
  // ...
  @Override
  public boolean equals(@Nullable Object other) {
    return (this == other || (super.equals(other) &&
        this.resourceBasePath.equals(((WebMergedContextConfiguration) other).resourceBasePath)));
  }
  
  @Override
  public int hashCode() {
    return (31 * super.hashCode() + this.resourceBasePath.hashCode());
  }
}
```

### 캐싱 로깅 활성화
application.yml
```yaml
logging:
  level:
    "[org.springframework.test.context.cache]": debug
```

```text
Storing ApplicationContext [941403433] in cache under key [[WebMergedContextConfiguration@72ea6193 testClass = MockitoTest, locations = '{}', classes = '{class com.ask.springtest.SpringTestApplication}', contextInitializerClasses = '[]', activeProfiles = '{}', propertySourceLocations = '{}', propertySourceProperties = '{org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true}', contextCustomizers = set[org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@37313c65, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@1df8da7a, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@0, org.springframework.boot.test.web.client.TestRestTemplateContextCustomizer@7d322cad, org.springframework.boot.test.autoconfigure.actuate.metrics.MetricsExportContextCustomizerFactory$DisableMetricExportContextCustomizer@15d49048, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizerFactory$Customizer@32115b28, org.springframework.boot.test.context.SpringBootTestArgs@1, org.springframework.boot.test.context.SpringBootTestWebEnvironment@30b7c004], resourceBasePath = 'src/main/webapp', contextLoader = 'org.springframework.boot.test.context.SpringBootContextLoader', parent = [null]]]
Spring test ApplicationContext cache statistics: [DefaultContextCache@a146b11 size = 1, maxSize = 32, parentContextCount = 0, hitCount = 0, missCount = 1]

Storing ApplicationContext [1772780918] in cache under key [[WebMergedContextConfiguration@68b6f0d6 testClass = MockitoTest.Spy, locations = '{}', classes = '{class com.ask.springtest.SpringTestApplication}', contextInitializerClasses = '[]', activeProfiles = '{}', propertySourceLocations = '{}', propertySourceProperties = '{org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true}', contextCustomizers = set[org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@37313c65, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@1df8da7a, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@a3bedf01, org.springframework.boot.test.web.client.TestRestTemplateContextCustomizer@7d322cad, org.springframework.boot.test.autoconfigure.actuate.metrics.MetricsExportContextCustomizerFactory$DisableMetricExportContextCustomizer@15d49048, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizerFactory$Customizer@32115b28, org.springframework.boot.test.context.SpringBootTestArgs@1, org.springframework.boot.test.context.SpringBootTestWebEnvironment@30b7c004], resourceBasePath = 'src/main/webapp', contextLoader = 'org.springframework.boot.test.context.SpringBootContextLoader', parent = [null]]]
Spring test ApplicationContext cache statistics: [DefaultContextCache@a146b11 size = 2, maxSize = 32, parentContextCount = 0, hitCount = 2, missCount = 2]

Storing ApplicationContext [830592222] in cache under key [[WebMergedContextConfiguration@16ac4d3d testClass = MockitoTest.Mock, locations = '{}', classes = '{class com.ask.springtest.SpringTestApplication}', contextInitializerClasses = '[]', activeProfiles = '{}', propertySourceLocations = '{}', propertySourceProperties = '{org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true}', contextCustomizers = set[org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@37313c65, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@1df8da7a, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@3d6d8c85, org.springframework.boot.test.web.client.TestRestTemplateContextCustomizer@7d322cad, org.springframework.boot.test.autoconfigure.actuate.metrics.MetricsExportContextCustomizerFactory$DisableMetricExportContextCustomizer@15d49048, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizerFactory$Customizer@32115b28, org.springframework.boot.test.context.SpringBootTestArgs@1, org.springframework.boot.test.context.SpringBootTestWebEnvironment@30b7c004], resourceBasePath = 'src/main/webapp', contextLoader = 'org.springframework.boot.test.context.SpringBootContextLoader', parent = [null]]]
Spring test ApplicationContext cache statistics: [DefaultContextCache@a146b11 size = 3, maxSize = 32, parentContextCount = 0, hitCount = 17, missCount = 3]

Storing ApplicationContext [545580634] in cache under key [[WebMergedContextConfiguration@5885e231 testClass = PropertySecondTest, locations = '{}', classes = '{class com.ask.springtest.SpringTestApplication}', contextInitializerClasses = '[]', activeProfiles = '{}', propertySourceLocations = '{}', propertySourceProperties = '{sample.key=sampleKey2, org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true}', contextCustomizers = set[org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@37313c65, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@1df8da7a, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@0, org.springframework.boot.test.web.client.TestRestTemplateContextCustomizer@7d322cad, org.springframework.boot.test.autoconfigure.actuate.metrics.MetricsExportContextCustomizerFactory$DisableMetricExportContextCustomizer@15d49048, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizerFactory$Customizer@32115b28, org.springframework.boot.test.context.SpringBootTestArgs@1, org.springframework.boot.test.context.SpringBootTestWebEnvironment@30b7c004], resourceBasePath = 'src/main/webapp', contextLoader = 'org.springframework.boot.test.context.SpringBootContextLoader', parent = [null]]]
Spring test ApplicationContext cache statistics: [DefaultContextCache@a146b11 size = 4, maxSize = 32, parentContextCount = 0, hitCount = 31, missCount = 4]

Storing ApplicationContext [1261714285] in cache under key [[WebMergedContextConfiguration@72f3f14c testClass = ProfileTest.German, locations = '{}', classes = '{class com.ask.springtest.SpringTestApplication}', contextInitializerClasses = '[]', activeProfiles = '{lang_de}', propertySourceLocations = '{}', propertySourceProperties = '{org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true}', contextCustomizers = set[org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@37313c65, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@1df8da7a, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@0, org.springframework.boot.test.web.client.TestRestTemplateContextCustomizer@7d322cad, org.springframework.boot.test.autoconfigure.actuate.metrics.MetricsExportContextCustomizerFactory$DisableMetricExportContextCustomizer@15d49048, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizerFactory$Customizer@32115b28, org.springframework.boot.test.context.SpringBootTestArgs@1, org.springframework.boot.test.context.SpringBootTestWebEnvironment@30b7c004], resourceBasePath = 'src/main/webapp', contextLoader = 'org.springframework.boot.test.context.SpringBootContextLoader', parent = [null]]]
Spring test ApplicationContext cache statistics: [DefaultContextCache@a146b11 size = 5, maxSize = 32, parentContextCount = 0, hitCount = 94, missCount = 6]

Storing ApplicationContext [1411865437] in cache under key [[WebMergedContextConfiguration@284c4f02 testClass = ProfileTest.English, locations = '{}', classes = '{class com.ask.springtest.SpringTestApplication}', contextInitializerClasses = '[]', activeProfiles = '{lang_en}', propertySourceLocations = '{}', propertySourceProperties = '{org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true}', contextCustomizers = set[org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@37313c65, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@1df8da7a, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@0, org.springframework.boot.test.web.client.TestRestTemplateContextCustomizer@7d322cad, org.springframework.boot.test.autoconfigure.actuate.metrics.MetricsExportContextCustomizerFactory$DisableMetricExportContextCustomizer@15d49048, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizerFactory$Customizer@32115b28, org.springframework.boot.test.context.SpringBootTestArgs@1, org.springframework.boot.test.context.SpringBootTestWebEnvironment@30b7c004], resourceBasePath = 'src/main/webapp', contextLoader = 'org.springframework.boot.test.context.SpringBootContextLoader', parent = [null]]]
Spring test ApplicationContext cache statistics: [DefaultContextCache@a146b11 size = 6, maxSize = 32, parentContextCount = 0, hitCount = 108, missCount = 7]
```

### 정리
DefaultContextCache 에서 Map<MergedContextConfiguration, ApplicationContext> 으로 관리하며  
동일한 MergedContextConfiguration 에 대해서 새로 생성하지 않고 기존 ApplicationContext 를 재활용 한다.

## 결론
Spring TestContext 프레임워크의 ApplicationContext 생성 전략(캐싱 전략)을 잘 파악하여  
ApplicationContext 를 최소한으로 생성하게 할 경우 테스트 시간이 감소 될 수 있다.

## 참조
- [Spring, TestContext Framework](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#testcontext-framework)
- [Improve Build Times with Context Caching from Spring Test](https://rieckpil.de/improve-build-times-with-context-caching-from-spring-test)
