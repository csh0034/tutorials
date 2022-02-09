package com.ask.gateway.config;

import com.ask.gateway.filter.DebugGatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

  private static final String DOWNSTREAM_URI_1 = "http://localhost:9999";
  private static final String DOWNSTREAM_URI_2 = "http://localhost:8888";

  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder builder, DebugGatewayFilter debugGatewayFilter) {
    return builder.routes()
        .route("order_route", r -> r.path("/order/**")
            .filters(f -> f
                .rewritePath("/order/(.*)", "/$1")
//                .rewritePath("/order/?(?<segment>.*)", "/${segment}")
//                .rewritePath("/order", "")
                .addRequestHeader("X-Order-Header", "order"))
            .metadata("orderKey", "orderValue")
            .uri(DOWNSTREAM_URI_1))

        .route("host_rewrite_route", r -> r.host("*.test.com")
            .filters(f -> f.prefixPath("/v1")
                .addResponseHeader("X-TestHeader", "rewrite_empty_response")
                .modifyResponseBody(String.class, String.class,
                    (exchange, s) -> {
                      if (s == null) {
                        return Mono.just("emptybody");
                      }
                      return Mono.just(s.toUpperCase());
                    })

            ).uri(DOWNSTREAM_URI_2)
        )

        .route("debug_route", r -> r.order(-1)
            .query("debug", "1")
            .filters(f -> f
                .filter(debugGatewayFilter))
            .uri(DOWNSTREAM_URI_1))
        .build();
  }

}
