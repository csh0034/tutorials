package com.ask.multitenancy.config;

import static java.util.stream.Collectors.toList;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(basePackages = "com.ask.multitenancy.repository.tenant",
    entityManagerFactoryRef = "tenantEntityManagerFactory",
    transactionManagerRef = "tenantTransactionManager"
)
public class TenantConfig {

  private final MultiTenantConnectionProvider connectionProvider;
  private final CurrentTenantIdentifierResolver tenantIdentifierResolver;

  private final ObjectProvider<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers;
  private final JpaProperties properties;
  private final HibernateProperties hibernateProperties;

  @Bean
  public LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory() {
    Map<String, Object> vendorProperties = getVendorProperties();
    customizeVendorProperties(vendorProperties);

    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactoryBean.setPackagesToScan("com.ask.multitenancy.entity.tenant");
    entityManagerFactoryBean.setPersistenceUnitName("tenant");
    entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    entityManagerFactoryBean.setJpaPropertyMap(vendorProperties);
    return entityManagerFactoryBean;
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
    vendorProperties.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
    vendorProperties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
    vendorProperties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);
  }

}
