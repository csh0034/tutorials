package com.ask.multitenancy.runner;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.persistence.Entity;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.reflections.Reflections;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

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
