package com.ask.springcache.config;

import javax.sql.DataSource;
import org.apache.commons.configuration.DatabaseConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public class CachedDatabaseConfiguration extends DatabaseConfiguration {

  private static final String CACHE_DB = "cache_DB";

  @Autowired
  private CachedDatabaseConfiguration self;

  public CachedDatabaseConfiguration(DataSource datasource, String table, String keyColumn, String valueColumn) {
    super(datasource, table, keyColumn, valueColumn);
  }

  @Cacheable(value = CACHE_DB)
  public Object getCacheProperty(String key) {
    return super.getProperty(key);
  }

  @Override
  public Object getProperty(String key) {
    return self.getCacheProperty(key);
  }

  @Override
  @CacheEvict(value = CACHE_DB, key = "#key")
  public void setProperty(String key, Object value) {
    super.setProperty(key, value);
  }
}
