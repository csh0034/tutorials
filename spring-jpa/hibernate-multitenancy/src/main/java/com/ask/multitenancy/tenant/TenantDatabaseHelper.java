package com.ask.multitenancy.tenant;

import com.ask.multitenancy.entity.master.Tenant;
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
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TenantDatabaseHelper {

  private static final String PACKAGE_OF_ENTITY_FOR_TENANT = "com.ask.multitenancy.entity.tenant";
  private final DataSourceProperties dataSourceProperties;

  public void executeSchemaExport(Tenant tenant) {
    ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
        .applySettings(createSettingsMap(tenant))
        .build();

    Reflections reflections = new Reflections(PACKAGE_OF_ENTITY_FOR_TENANT);
    Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);

    MetadataSources metadata = new MetadataSources(serviceRegistry);
    entities.forEach(metadata::addAnnotatedClass);

    SchemaExport schemaExport = new SchemaExport();
    schemaExport.create(EnumSet.of(TargetType.DATABASE), metadata.buildMetadata());
  }

  private Map<String, Object> createSettingsMap(Tenant tenant) {
    DataSource dataSource = dataSourceProperties.initializeDataSourceBuilder()
        .url(tenant.getJdbcUrl())
        .username(tenant.getDbUsername())
        .password(tenant.getDbPassword())
        .build();

    Map<String, Object> settings = new HashMap<>();
    settings.put(AvailableSettings.DATASOURCE, dataSource);
    settings.put(AvailableSettings.SHOW_SQL, false);
    settings.put(AvailableSettings.FORMAT_SQL, true);
    return settings;
  }
}
