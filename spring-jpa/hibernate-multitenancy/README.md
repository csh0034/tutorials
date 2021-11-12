# Hibernate Multitenancy

## What is Multitenancy ?
- 소프트웨어 개발에 적용되어 애플리케이션의 단일 실행 인스턴스가 동시에 여러 클라이언트(테넌트)에 서비스를 제공하는 아키텍처를 나타낸다.
- 여기에는 데이터베이스에 저장된 각 테넌트가 소유한 데이터가 포함되며, SaaS 솔루션에서 매우 일반적인 구조이다.
- 다양한 테넌트와 관련된 정보(데이터, 사용자 지정 등)를 격리하는 것은 이러한 시스템에서 특히 어려운 문제이다.

### [SaaS 의 기술적 요소](https://blog.lgcns.com/951)
- Configuration 기반 어플리케이션 커스터마이징
- 멀티 테넌시 아키텍처
- 확장성을 지원하는 인프라

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