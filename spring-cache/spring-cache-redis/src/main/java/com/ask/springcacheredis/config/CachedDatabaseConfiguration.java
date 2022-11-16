package com.ask.springcacheredis.config;

import javax.sql.DataSource;
import org.apache.commons.configuration.DatabaseConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;

public class CachedDatabaseConfiguration extends DatabaseConfiguration {

  @Autowired
  @Lazy
  private CachedDatabaseConfiguration self;

  public CachedDatabaseConfiguration(DataSource datasource, String table, String keyColumn, String valueColumn) {
    super(datasource, table, keyColumn, valueColumn);
  }

  @Cacheable(value = CacheConstants.DB_PROPERTY)
  public Object getCacheProperty(String key) {
    return super.getProperty(key);
  }

  @Override
  public Object getProperty(String key) {
    return self.getCacheProperty(key);
  }

  @Override
  @CacheEvict(value = CacheConstants.DB_PROPERTY, key = "#key")
  public void setProperty(String key, Object value) {
    super.setProperty(key, value);
  }
}
