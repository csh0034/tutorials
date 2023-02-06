package com.ask.springjpacore.config;

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty("spring.datasource.slave.url")
@Slf4j
public class ReplicationDataSourceConfig {

  @Bean
//  @FlywayDataSource // flyway 사용시 필요
  @ConfigurationProperties(prefix = "spring.datasource.hikari")
  public DataSource masterDataSource(@Qualifier("masterProperties") DataSourceProperties masterProperties) {
    return masterProperties.initializeDataSourceBuilder()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSourceProperties masterProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.slave.hikari")
  public DataSource slaveDataSource(@Qualifier("slaveProperties") DataSourceProperties slaveProperties) {
    return slaveProperties.initializeDataSourceBuilder()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.slave")
  public DataSourceProperties slaveProperties() {
    return new DataSourceProperties();
  }

  @Bean
  public DataSource routingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
      @Qualifier("slaveDataSource") DataSource slaveDataSource) {
    log.debug("initialize routingDataSource");

    ReplicationRoutingDataSource routingDataSource = new ReplicationRoutingDataSource();

    Map<Object, Object> sources = new HashMap<>();
    sources.put(DataSourceType.MASTER, masterDataSource);
    sources.put(DataSourceType.SLAVE, slaveDataSource);

    routingDataSource.setTargetDataSources(sources);
    routingDataSource.setDefaultTargetDataSource(masterDataSource);

    return routingDataSource;
  }

  @Bean
  @Primary
  public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
    return new LazyConnectionDataSourceProxy(routingDataSource);
  }

  private static class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
      return TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? DataSourceType.MASTER : DataSourceType.SLAVE;
    }
  }

  private enum DataSourceType {
    MASTER, SLAVE
  }

}
