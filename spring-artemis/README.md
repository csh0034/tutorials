# Spring ActiveMQ Artemis

## ActiveMQ Artemis

- 고성능 논블럭킹 기반 차세대 메시징 어플리케이션
- Artemis 가 충분한 수준의 ActiveMQ 의 기능을 구현하면 추후 ActiveMQ 의 다음 메이저 버전이 된다.
- 기존 ActiveMQ 의 경우 `ActiveMQ Classic` 으로 불리고 있음.

## Artemis With Spring

- JMS 를 사용한다.
  - JMS: 둘 또는 그 이상의 클라이언트 사이에 메시지를 보내기 위한 자바 메시지 지향 미들웨어 **API**

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-artemis</artifactId>
</dependency>

<!-- embedded artemis jms server-->
<dependency>
  <groupId>org.apache.activemq</groupId>
  <artifactId>artemis-jms-server</artifactId>
</dependency>
```

## 참조

- [ActiveMQ](https://activemq.apache.org/)
- [ActiveMQ Artemis Support](https://docs.spring.io/spring-boot/docs/current/reference/html/messaging.html#messaging.jms.artemis)
- [ActiveMQ vs ActiveMQ-Artemis](https://serverfault.com/questions/873533/confusion-between-activemq-and-activemq-artemis)
