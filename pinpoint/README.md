# Pinpoint

## Pinpoint 란?

Pinpoint 는 대규모 분산 시스템의 성능을 분석하고 문제를 진단, 처리하는 플랫폼이다.  
Google Dapper 스타일의 추적 방식을 사용해, 분산된 요청을 하나의 트랜잭션으로 추적한다.

- [Pinpoint Live Demo](http://125.209.240.10:10123/main)

## Pinpoint 개발 동기 및 특징

n계층 아키텍처로 변화함에 따라 시스템의 복잡도도 증가했다. 시스템의 복잡도가 높으면 장애나 성능 문제가 발생했을 때 해결이 어렵다.  
복잡도가 높을수록 문제의 원인을 찾는 데 시간이 오래 걸리고 문제의 원인을 발견하지 못하는 경우도 많아진다.

따라서 하단과 같은 특징의 Pinpoint 가 탄생하였다.
- 분산된 애플리케이션의 메시지를 추적할 수 있는 분산 트랜잭션 추적
- 애플리케이션 구성을 파악할 수 있는 애플리케이션 토폴로지 자동 발견
- 대규모 서버군을 지원할 수 있는 수평 확장성
- 코드 수준의 가시성을 제공해 문제 발생 지점과 병목 구간을 쉽게 발견
- bytecode instrumentation 기법으로 코드를 수정하지 않고 원하는 기능을 추가

## Docker 를 통한 설치

### pinpoint 설치

```shell
$ git clone https://github.com/naver/pinpoint-docker.git
$ cd pinpoint-docker
$ git checkout tags/{tag} # 선택
$ docker-compose pull && docker-compose up -d
```

### quickstart, agent sample 주석처리

pinpoint-docker 에는 agent 연결 테스트용도로 QuickStart application 이 포함되어있다.  
따라서 운영환경에선 제외해야한다.

```shell
$ vi docker-compose.yml 
```

- services 목록중에 `pinpoint-quickstart`, `pinpoint-agent` 주석 또는 제거

## agent 세팅

```shell
$ wget https://github.com/pinpoint-apm/pinpoint/releases/download/v2.3.3/pinpoint-agent-2.3.3.tar.gz \
&& tar xvfz pinpoint-agent-*.tar.gz \
&& rm pinpoint-agent-*.tar.gz \
&& ln -sf pinpoint-agent-* pinpoint-agent

$ cd pinpoint-agent
$ vi pinpoint-root.config
```

하단 두가지 ip 를 pinpoint 가 설치된 서버 ip 로 변경

- profiler.transport.grpc.collector.ip=127.0.0.1
- profiler.collector.ip=127.0.0.1

### Executable jar or war

```shell
java -jar \
  -javaagent:$AGENT_PATH/pinpoint-bootstrap-2.3.3.jar \
  -Dpinpoint.agentId=app-1 \
  -Dpinpoint.applicationName=app \
  application.jar
```

### Executable jar or war with .conf file

1. jar 또는 war 파일 위치에 동일한 이름으로 `.conf` 파일 생성 후 하단 정보 세팅
2. `java -jar application.jar`

ex. application.conf

```text
JAVA_OPTS="-javaagent:$AGENT_PATH/pinpoint-bootstrap-2.3.3.jar -Dpinpoint.agentId=app-1 -Dpinpoint.applicationName=app"
```

### 외장 Tomcat
```shell
CATALINA_OPTS="$CATALINA_OPTS -javaagent:$AGENT_PATH/pinpoint-bootstrap-2.3.3.jar"
CATALINA_OPTS="$CATALINA_OPTS -Dpinpoint.agentId=app-1"
CATALINA_OPTS="$CATALINA_OPTS -Dpinpoint.applicationName=app"
```

## 참조
- [pinpoint, Reference](https://pinpoint-apm.github.io/pinpoint/main.html)
- [pinpoint-apm, GitHub](https://github.com/pinpoint-apm)
- [대규모 분산 시스템 추적 플랫폼, Pinpoint](https://d2.naver.com/helloworld/1194202)
- [spring-petclinic, agent sample](https://github.com/spring-projects/spring-petclinic)
