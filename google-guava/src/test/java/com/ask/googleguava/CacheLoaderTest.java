package com.ask.googleguava;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class CacheLoaderTest {

  @Spy
  private CacheLoader<String, String> cacheLoader = new CacheLoader<String, String>() {
    @Override
    public String load(String key) {
      log.info("load, key: [{}]", key);
      return key.toUpperCase();
    }
  };

  @Spy
  private RemovalListener<String, String> removalListener = new RemovalListener<String, String>() {
    @Override
    public void onRemoval(RemovalNotification<String, String> removal) {
      log.info("onRemoval, key: [{}], value: [{}]", removal.getKey(), removal.getValue());
    }
  };

  @Test
  void cache() throws Exception {
    // given
    LoadingCache<String, String> caches = CacheBuilder.newBuilder()
        .maximumSize(10)
        .build(cacheLoader);

    // expect
    assertThat(caches.get("aaa")).isEqualTo("AAA");
    assertThat(caches.get("aaa")).isEqualTo("AAA");
    assertThat(caches.get("aaa")).isEqualTo("AAA");
    assertThat(caches.get("aaa")).isEqualTo("AAA");

    assertThat(caches.size()).isEqualTo(1);

    then(cacheLoader).should(only()).load(eq("aaa"));
  }

  @Test
  void cacheSleep() throws Exception {
    // given
    LoadingCache<String, String> caches = CacheBuilder.newBuilder()
        .expireAfterAccess(Duration.ofMillis(200))
        .maximumSize(10)
        .removalListener(removalListener)
        .build(cacheLoader);

    // expect
    assertThat(caches.get("aaa")).isEqualTo("AAA");
    assertThat(caches.size()).isEqualTo(1);

    TimeUnit.MILLISECONDS.sleep(500);
    caches.cleanUp(); // 명시적으로 호출하지 않으면 다음 LoadingCache 메서드 호출전까진 expired 대상이 남아있음

    then(removalListener).should(only()).onRemoval(any());

    assertThat(caches.get("bbb")).isEqualTo("BBB");
    assertThat(caches.size()).isEqualTo(1);

    then(cacheLoader).should(times(2)).load(any());
  }

}
