package com.ask.springwebflux.config.cache;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import reactor.cache.CacheFlux;
import reactor.cache.CacheMono;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReactorCacheManager {

  private final CacheManager cacheManager;

  public <T> Mono<T> findCachedMono(String cacheName, Object key, Supplier<Mono<T>> retriever, Class<T> classType) {
    Cache cache = Objects.requireNonNull(cacheManager.getCache(cacheName));
    return CacheMono.lookup(k -> {
          T result = cache.get(k, classType);
          return Mono.justOrEmpty(result)
              .map(Signal::next);
        }, key)
        .onCacheMissResume(Mono.defer(retriever))
        .andWriteWith((k, signal) -> Mono.fromRunnable(() -> {
          if (!signal.isOnError()) {
            log.debug("[ReactorCache Mono] key : {}", k);
            T value = signal.get();
            cache.put(k, value);
          }
        }));
  }

  @SuppressWarnings("unchecked")
  public <T> Flux<T> findCachedFlux(String cacheName, Object key, Supplier<Flux<T>> retriever) {
    Cache cache = Objects.requireNonNull(cacheManager.getCache(cacheName));
    return CacheFlux.lookup(k -> {
          List<T> result = cache.get(k, List.class);
          return Mono.justOrEmpty(result)
              .flatMap(list -> Flux.fromIterable(list).materialize().collectList());
        }, key)
        .onCacheMissResume(Flux.defer(retriever))
        .andWriteWith((k, signalList) -> Flux.fromIterable(signalList)
            .dematerialize()
            .collectList()
            .doOnNext(list -> {
              log.debug("[ReactorCache Flux] key : {}", k);
              cache.put(k, list);
            })
            .then());
  }

}
