package com.ask.multitenancy.tenant;

import com.ask.multitenancy.entity.master.Tenant;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.persistence.Entity;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.reflections.Reflections;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TenantDatabaseHelper {

  private static final String PACKAGE_OF_ENTITY_FOR_TENANT = "com.ask.multitenancy.entity.tenant";

  private final JpaProperties jpaProperties;

  public void executeSchemaExport(Tenant tenant) {
    ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
        .applySettings(createSettingsMap(tenant))
        .build();

    Reflections reflections = new Reflections(PACKAGE_OF_ENTITY_FOR_TENANT);
    Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);

    MetadataSources metadata = new MetadataSources(serviceRegistry);
    entities.forEach(metadata::addAnnotatedClass);

    SchemaExport schemaExport = new SchemaExport();
    schemaExport.createOnly(EnumSet.of(TargetType.DATABASE), metadata.buildMetadata());
  }

  private Map<String, Object> createSettingsMap(Tenant tenant) {
    Map<String, Object> settings = new HashMap<>(jpaProperties.getProperties());
    settings.put(AvailableSettings.URL, tenant.getJdbcUrl());
    settings.put(AvailableSettings.USER, tenant.getDbUsername());
    settings.put(AvailableSettings.PASS, tenant.getDbPassword());
    return settings;
  }

}
