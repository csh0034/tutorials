package com.ask.springjpajcache.config;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheEventLoggerListener implements CacheEventListener<Object, Object> {

  private static final long serialVersionUID = -1947904555841317418L;

  @Override
  public void onCreated(Iterable<CacheEntryEvent<?, ?>> events) throws CacheEntryListenerException {
    logging(events);
  }

  @Override
  public void onExpired(Iterable<CacheEntryEvent<?, ?>> events) throws CacheEntryListenerException {
    logging(events);
  }

  @Override
  public void onRemoved(Iterable<CacheEntryEvent<?, ?>> events) throws CacheEntryListenerException {
    logging(events);
  }

  @Override
  public void onUpdated(Iterable<CacheEntryEvent<?, ?>> events) throws CacheEntryListenerException {
    logging(events);
  }

  private void logging(Iterable<CacheEntryEvent<?,?>> events) {
    events.forEach(event -> {
      EventType eventType = event.getEventType();
      Object key = event.getKey();
      Object oldValue = event.getOldValue();
      Object value = event.getValue();
      log.info("cache {} event. key: {}, oldValue: {}, newValue: {}", eventType, key, oldValue, value);
    });
  }
}
