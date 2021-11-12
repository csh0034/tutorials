package com.ask.multitenancy.config;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jdbc.metadata.CompositeDataSourcePoolMetadataProvider;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.ask.multitenancy.repository.master",
    entityManagerFactoryRef = "masterEntityManagerFactory")
public class MasterConfig {

  private static final String PROVIDER_DISABLES_AUTOCOMMIT = "hibernate.connection.provider_disables_autocommit";

  private final ObjectProvider<Collection<DataSourcePoolMetadataProvider>> metadataProviders;
  private final ObjectProvider<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers;
  private final JpaProperties properties;
  private final HibernateProperties hibernateProperties;
  private final EntityManagerFactoryBuilder builder;
  private final DataSource dataSource;

  @Bean
  @Primary
  public LocalContainerEntityManagerFactoryBean masterEntityManagerFactory() {
    Map<String, Object> vendorProperties = getVendorProperties();
    customizeVendorProperties(vendorProperties);
    return builder.dataSource(dataSource)
        .packages("com.ask.multitenancy.entity.master")
        .properties(vendorProperties)
        .persistenceUnit("master")
        .build();
  }

  private Map<String, Object> getVendorProperties() {
    return new LinkedHashMap<>(this.hibernateProperties.determineHibernateProperties(
        this.properties.getProperties(),
        new HibernateSettings().hibernatePropertiesCustomizers(
            hibernatePropertiesCustomizers.orderedStream().collect(toList()))));
  }

  private void customizeVendorProperties(Map<String, Object> vendorProperties) {
    if (!vendorProperties.containsKey(PROVIDER_DISABLES_AUTOCOMMIT)) {
      configureProviderDisablesAutocommit(vendorProperties);
    }
  }

  private void configureProviderDisablesAutocommit(Map<String, Object> vendorProperties) {
    if (isDataSourceAutoCommitDisabled()) {
      vendorProperties.put(PROVIDER_DISABLES_AUTOCOMMIT, "true");
    }
  }

  private boolean isDataSourceAutoCommitDisabled() {
    DataSourcePoolMetadataProvider poolMetadataProvider = new CompositeDataSourcePoolMetadataProvider(
        metadataProviders.getIfAvailable());
    DataSourcePoolMetadata poolMetadata = poolMetadataProvider.getDataSourcePoolMetadata(dataSource);
    return poolMetadata != null && Boolean.FALSE.equals(poolMetadata.getDefaultAutoCommit());
  }
}
