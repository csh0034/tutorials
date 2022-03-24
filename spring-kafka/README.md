# Spring Kafka

## Kafka With Docker

### docker-compose.yml

```yaml
version: '3.8'
services:
  zookeeper:
    container_name: zookeeper
    image: wurstmeister/zookeeper
    ports:
      - "2181:2181"
  kafka:
    container_name: kafka
    image: wurstmeister/kafka
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_HOST_NAME: 127.0.0.1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_CREATE_TOPICS: "test-topic:1:1"
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
```

### Kafka Command Line

```shell
$ docker-compose up -d
$ docker exec -it kafka bash

$ cd /opt/kafka/bin

# 토픽 조회
$ kafka-topics.sh --list --zookeeper zookeeper

# 토픽 추가
$ kafka-topics.sh --create --zookeeper zookeeper --replication-factor 1 -partition 1 --topic new_topic

# 토픽 삭제
$ kafka-topics.sh --delete --zookeeper zookeeper --topic test_topic

# environment 를 통해 등록한 토픽 상세 정보
$ kafka-topics.sh --describe --topic test_topic --zookeeper zookeeper

# consume
$ kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic test_topic

# produce, 하단 command 입력후 > 가 나오면 입력하면 됨
$ kafka-console-producer.sh --bootstrap-server 127.0.0.1:9092 --topic test_topic
```

## 참조

- [Kafka QuickStart](https://kafka.apache.org/quickstart)
