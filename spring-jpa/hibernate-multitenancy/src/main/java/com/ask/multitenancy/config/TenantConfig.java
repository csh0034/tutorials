package com.ask.multitenancy.config;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(basePackages = "com.ask.multitenancy.repository.tenant",
    entityManagerFactoryRef = "tenantEntityManagerFactory",
    transactionManagerRef = "tenantTransactionManager"
)
public class TenantConfig {

  private static final String PROVIDER_DISABLES_AUTOCOMMIT = "hibernate.connection.provider_disables_autocommit";

  private final MultiTenantConnectionProvider connectionProvider;
  private final CurrentTenantIdentifierResolver tenantIdentifierResolver;

  private final ObjectProvider<Collection<DataSourcePoolMetadataProvider>> metadataProviders;
  private final ObjectProvider<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers;
  private final JpaProperties properties;
  private final HibernateProperties hibernateProperties;
  private final EntityManagerFactoryBuilder builder;
  private final DataSource dataSource;

  @Bean
  public LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory() {
    Map<String, Object> vendorProperties = getVendorProperties();
    customizeVendorProperties(vendorProperties);
    return builder.dataSource(dataSource)
        .packages("com.ask.multitenancy.entity.tenant")
        .properties(vendorProperties)
        .persistenceUnit("tenant")
        .build();
  }

  @Bean
  public PlatformTransactionManager tenantTransactionManager(
      @Qualifier("tenantEntityManagerFactory") EntityManagerFactory tenantEntityManagerFactory) {
    return new JpaTransactionManager(tenantEntityManagerFactory);
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

    vendorProperties.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
    vendorProperties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
    vendorProperties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);
    vendorProperties.put(Environment.HBM2DDL_AUTO, "none");
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
