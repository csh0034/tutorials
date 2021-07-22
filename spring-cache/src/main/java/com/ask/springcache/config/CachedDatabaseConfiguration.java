package com.ask.springcache.config;

import javax.sql.DataSource;
import org.apache.commons.configuration.DatabaseConfiguration;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;

public class CachedDatabaseConfiguration extends DatabaseConfiguration {

  private final ApplicationContext context;
  private CachedDatabaseConfiguration selfProxy;

  public CachedDatabaseConfiguration(DataSource datasource, String table, String keyColumn, String valueColumn, ApplicationContext context) {
    super(datasource, table, keyColumn, valueColumn);
    this.context = context;
  }

  @Cacheable(value = "db")
  public Object getCacheProperty(String key) {
    return super.getProperty(key);
  }

  @CachePut(value = "db", key = "#key")
  public Object setCacheProperty(String key, Object value) {
    super.setProperty(key, value);
    return value;
  }

  @Override
  public void setProperty(String key, Object value) {
    getSelfProxy().setCacheProperty(key, value);
  }

  @Override
  public Object getProperty(String key) {
    return getSelfProxy().getCacheProperty(key);
  }

  private CachedDatabaseConfiguration getSelfProxy(){
    if (selfProxy == null) {
      selfProxy = context.getBean(this.getClass());
    }
    return selfProxy;
  }
}
