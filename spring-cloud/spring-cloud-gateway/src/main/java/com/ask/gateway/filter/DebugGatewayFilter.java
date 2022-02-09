package com.ask.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DebugGatewayFilter implements GatewayFilter {

  private final ObjectMapper objectMapper;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String result = createServerInfo();

    exchange.getResponse().setStatusCode(HttpStatus.OK);
    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(result.getBytes(StandardCharsets.UTF_8));
    return exchange.getResponse().writeWith(Mono.just(buffer));
  }

  private String createServerInfo() {
    OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    Map<String, Object> map = new LinkedHashMap<>();
    map.put("availableProcessors", Runtime.getRuntime().availableProcessors());
    map.put("CPU Usage", String.format("%.2f", osBean.getSystemCpuLoad() * 100));
    map.put("Memory Free Space", String.format("%.2f MB", (double) osBean.getFreePhysicalMemorySize() / 1024 / 1024));
    map.put("Memory Total Space", String.format("%.2f MB", (double) osBean.getTotalPhysicalMemorySize() / 1024 / 1024));

    try {
      return objectMapper.writeValueAsString(map);
    } catch (JsonProcessingException e) {
      return "error";
    }
  }

}
