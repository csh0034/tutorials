package com.ask.springwebflux.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
@Slf4j
public class WebConfig {

  @Bean
  public WebClient webClient(WebClient.Builder builder) {
    HttpClient httpClient = HttpClient.create()
//        .followRedirect(true)
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
        .responseTimeout(Duration.ofMillis(5000))
        .doOnConnected(conn -> conn
            .addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
            .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));

    ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 50))
        .build();

    return builder.baseUrl("http://localhost:8080")
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .exchangeStrategies(exchangeStrategies)
        .filter(ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
          log.info(">>>>>>>>>> REQUEST <<<<<<<<<<");
          log.info("Request: {} {}", clientRequest.method(), clientRequest.url());

          clientRequest.headers()
              .forEach((name, values) -> values.forEach(value -> log.info("{} : {}", name, value)));

          return Mono.just(clientRequest);
        }))
        .filter(ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
          log.info(">>>>>>>>>> RESPONSE <<<<<<<<<<");
          log.info("statusCode : {}", clientResponse.statusCode());

          clientResponse.headers()
              .asHttpHeaders()
              .forEach((name, values) -> values.forEach(value -> log.info("{} : {}", name, value)));

          return Mono.just(clientResponse);
        }))
//        .filter(basicAuthentication("user", "password"))
        .build();
  }

}
