# Spring Kafka

## Apache Kafka 란?

빠르고 확장 가능한 작업을 위해 데이터 피드의 분산 스트리밍, 파이프 라이닝 및 재생을 위한 실시간 스트리밍 데이터를   
처리하기 위한 목적으로 설계된 오픈 소스 분산형 게시-구독 메시징 플랫폼이다.

![01.png](images/01.png)

### Zookeeper

카프카 아키텍쳐 내에서 카프카 브로커들을 하나의 클러스터로 관리하기 위해서는 별도의 분산 코디네이팅 시스템인 Zookeeper 를 두어 관리한다.    
서버의 상태를 감지하기 위해 사용되며 새로운 토픽이 생성되었을 때, 토픽의 생성과 소비에 대한 상태를 저장한다.


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
      KAFKA_CREATE_TOPICS: "test_topic:1:1"
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

- zookeeper 를 통한 접근
  - `--zookeeper zookeeper`
- kafka 를 통한 접근
  - `--bootstrap-server 127.0.0.1:9092`

### Zookeeper Command Line

```shell
$ docker exec -it zookeeper bash
$ cd /opt/zookeeper-*/bin
$ ./zkCli.sh

$ docker exec -it zookeeper /opt/zookeeper-3.4.13/bin/zkCli.sh

# 목록 출력
$ ls /
```

## 참조

- [Kafka QuickStart](https://kafka.apache.org/quickstart)
- [Apache Kafka 란?, tibico](https://www.tibco.com/ko/reference-center/what-is-apache-kafka)
