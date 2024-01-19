# Spring Prometheus

## Prometheus 란?

오픈소스기반의 모니터링 솔루션이며 모니터링 대상이 되는 metric 정보를 pull 방식으로 수집한다.  
다른 모니터링 플랫폼에 비해 사용과 설정이 간단하며 유연하고 좋은 성능을 낸다.

## Grafana 란?

다양한 데이터소스로부터 데이터를 가져와 시각화하여 대시보드를 구성할 수 있도록 돕는 오픈소스 플랫폼이다.  

- Graphite
- Prometheus
- InfluxDB
- Elasticsearch
- AWS CloudWatch
- ...

## Prometheus And Grafana With Docker

### docker-compose.yml

```yaml
version: "3.6"

services:
  prometheus:
    container_name: prometheus
    image: prom/prometheus
    ports:
      - "9090:9090"
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'
      - '--web.enable-lifecycle' # curl -X POST http://127.0.0.1:9090/-/reload 설정 갱신
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml

  grafana:
    container_name: grafana
    image: grafana/grafana
    environment:
#      - GF_SECURITY_ADMIN_USER=user1
#      - GF_SECURITY_ADMIN_PASSWORD=user1
      - GF_USERS_ALLOW_SIGN_UP=false
#    volumes:
#      - /data/grafana/data:/var/lib/grafana
#      - /data/grafana/provisioning:/etc/grafana/provisioning
    ports:
      - "3000:3000"
    depends_on:
      - prometheus
```

### prometheus.yml

```yaml
global:
  scrape_interval: "15s"
  evaluation_interval: "15s"
scrape_configs:
  - job_name: "springboot"
    metrics_path: "/actuator/prometheus"
    static_configs:
      - targets:
          - "host.docker.internal:8080"
    basic_auth:
      username: 'actuator'  # spring security basic auth 설정해야함
      password: 'actuator'  # spring security basic auth 설정해야함
  - job_name: "rabbitmq"
    metrics_path: "/metrics"
    static_configs:
      - targets:
          - "host.docker.internal:15692"
  - job_name: "prometheus"
    static_configs:
      - targets:
          - "localhost:9090"
```

### Custom Metrics

[Registering Custom Metrics](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.metrics.registering-custom)

## 참조

- [Docs, Prometheus Docker](https://prometheus.io/docs/prometheus/latest/installation/#using-docker)
- [Docs, RabbitMQ Monitoring with Prometheus & Grafana](https://www.rabbitmq.com/prometheus.html)
- [Prometheus + Grafana 모니터링](https://meetup.toast.com/posts/237)
- [도커 컴포즈를 이용한 프로메테우스 설치](https://danawalab.github.io/common/2020/03/16/Common-Prometheus.html)
