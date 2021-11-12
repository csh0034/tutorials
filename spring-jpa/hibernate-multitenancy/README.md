# Hibernate Multitenancy

## What is Multitenancy ?
- 소프트웨어 개발에 적용되어 애플리케이션의 단일 실행 인스턴스가 동시에 여러 클라이언트(테넌트)에 서비스를 제공하는 아키텍처를 나타낸다.
- 여기에는 데이터베이스에 저장된 각 테넌트가 소유한 데이터가 포함되며, SaaS 솔루션에서 매우 일반적인 구조이다.
- 다양한 테넌트와 관련된 정보(데이터, 사용자 지정 등)를 격리하는 것은 이러한 시스템에서 특히 어려운 문제이다.

### [SaaS 의 기술적 요소](https://blog.lgcns.com/951)
- Configuration 기반 어플리케이션 커스터마이징
- 멀티 테넌시 아키텍처
- 확장성을 지원하는 인프라

## Hibernate Multitenancy

### Multitenant 데이터 접근 방식
- Separate database
  - 각 테넌트의 데이터는 물리적으로 별도의 데이터베이스 인스턴스에 보관.
  - JDBC Connection 은 각 데이터베이스를 가리키므로 모든 풀링은 테넌트별로 이루어진다.
  - 일반적인 애플리케이션 접근 방식
    - 테넌트별로 JDBC 연결 풀을 정의 하고 현재 로그인한 사용자와 연결된 테넌트 식별자를 기반으로 사용할 풀을 선택하는것이다.
- Separate schema
  - 각 테넌트의 데이터는 단일 데이터베이스 인스턴스의 고유한 데이터베이스 스키마에 보관
  - JDBC 연결을 정의하는 두 가지 방법
    1. 드라이버가 연결 URL에서 기본 스키마 이름 지정을 지원하거나 풀링 메커니즘이 연결에 사용할 스키마 이름 지정을 지원하는 경우
    2. 데이터베이스 자체를 가리킬 수 있지만(일부 기본 스키마 사용) 연결은 SQL SET SCHEMA(또는 유사한) 명령을 사용하여 변경
- Partitioned (discriminator) data
  - 5.4.32.Final 기준 Hibernate 에서 제공되지 않음. 
  - 모든 데이터는 단일 데이터베이스 스키마에 보관된다. 
  - 각 테넌트에 대한 데이터는 파티션 값 또는 식별자를 사용하여 파티션된다.
 
### Multitenant 관련 추상화 인터페이스
- MultiTenantConnectionProvider – 테넌트당 연결 제공
- CurrentTenantIdentifierResolver – 사용할 테넌트 식별자를 확인합니다.
 
### Hibernate SchemaExport
- [StackOverflow, SchemaExport in Hibernate 5](https://stackoverflow.com/questions/47432115/replacing-schemaexportconfiguration-in-hibernate-5)
```java
@Component
@RequiredArgsConstructor
public class MultiTenantCreateRunner implements ApplicationRunner {

  private final DataSourceProperties dataSourceProperties;
  
  @Override
  public void run(ApplicationArguments args) {
    Map<String, Object> settings = createSettingsMap();

    ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
        .applySettings(settings)
        .build();

    Reflections reflections = new Reflections("com.ask.multitenancy.entity.tenant");
    Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);

    MetadataSources metadata = new MetadataSources(serviceRegistry);
    entities.forEach(metadata::addAnnotatedClass);

    SchemaExport schemaExport = new SchemaExport();
    schemaExport.create(EnumSet.of(TargetType.DATABASE), metadata.buildMetadata());

    // sql 파일로 생성
    // SchemaExport schemaExport = new SchemaExport();
    // schemaExport.setOutputFile("schema.sql");
    // schemaExport.setDelimiter(";");
    // schemaExport.create(EnumSet.of(TargetType.SCRIPT), metadata.buildMetadata());
  }

  private Map<String, Object> createSettingsMap() {
    DataSource dataSource = dataSourceProperties.initializeDataSourceBuilder()
        .url("jdbc:mariadb://127.0.0.1:3306/db_second?createDatabaseIfNotExist=true")
        .build();

    Map<String, Object> settings = getDefaultSettings();
    settings.put(AvailableSettings.DATASOURCE, dataSource);
    return settings;
  }

  // Using Hibernate built-in connection pool (not for production use!)
  private Map<String, Object> createSettingsMapWithoutDataSource() {
    Map<String, Object> settings = getDefaultSettings();
    settings.put(AvailableSettings.URL, "jdbc:mariadb://127.0.0.1:3306/db_second?createDatabaseIfNotExist=true");
    settings.put(AvailableSettings.USER, dataSourceProperties.getUsername());
    settings.put(AvailableSettings.PASS, dataSourceProperties.getPassword());
    return settings;
  }

  private HashMap<String, Object> getDefaultSettings() {
    HashMap<String, Object> settings = new HashMap<>();
    settings.put(AvailableSettings.SHOW_SQL, false);
    settings.put(AvailableSettings.FORMAT_SQL, true);
    return settings;
  }
}
```

## 참조
- [hibernate, multitenancy](https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#multitenacy)
- [baeldung, A Guide to Multitenancy in Hibernate 5](https://www.baeldung.com/hibernate-5-multitenancy)