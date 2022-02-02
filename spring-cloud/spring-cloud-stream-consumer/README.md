# Spring Cloud Stream Consumer

## Bindings Properties

```yaml
spring:
  rabbitmq:
    virtual-host: cloud
  cloud:
    function:
      definition: inSample1;inSample2
    stream:
      bindings:
        inSample1-in-0:
          destination: sample1
          group: qeueue
        inSample2-in-0:
          destination: sample2
          group: qeueue
```

spring.cloud.stream.bindings 

[group](https://docs.spring.io/spring-cloud-stream/docs/3.2.1/reference/html/spring-cloud-stream.html#consumer-groups)
- 지정하지 않을 경우 큐가 Auto Delete, Exclusive 로 생성되며 큐이름이 랜덤으로 생성된다.
- 따라서 서버가 여러대일 경우 하나의 큐를 사용하여 한곳에서만 처리하게 해야할때 필요하다.
