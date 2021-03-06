# Spring Docker

## [Spring Boot Plugin 사용](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/#build-image)

- [BuildPack](https://buildpacks.io) 을 사용하여 Dockerfile 없이 이미지를 생성한다.

```shell
$ spring-boot:build-image 
```

## Docker Build

- [Build Option](https://docs.docker.com/engine/reference/commandline/build/#options)
- `docker build --help` 명령어

### Dockerfile

- layertools 모드는 fully executable jar 에는 사용할 수 없다.
- layertools 를 사용하여 jar file 을 각 layer 로 분리하여 레이어드 캐시를 사용한다.
  - dependencies (for regular released dependencies)
  - spring-boot-loader (for everything under org/springframework/boot/loader)
  - snapshot-dependencies (for snapshot dependencies)
  - application (for application classes and resources)

```dockerfile
FROM eclipse-temurin:11-jre as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:11-jre
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
```

### 일반 빌드

- `--tag` 와 `-tag` 동일

```shell
$ docker build --tag csh0034/sample:0.1 .
```

### Buildx Multi-Architecture Image 빌드

- `--push`: 멀티 아키텍처 이미지를 원격에 push 함
- `--load`: 로컬에 이미지 생성함 *) platform 이 하나일 경우에만 사용 가능함

```shell
$ docker buildx create --use --name multiarch-builder # Builder 생성 및 사용 설정
$ docker buildx inspect --bootstrap # 현재 Builder Instance 정보
```

```shell
$ docker buildx build \
--push \
--platform linux/arm64,linux/amd64 \
--tag csh0034/sample:0.2 .
```

```shell
$ docker buildx build \
--load \
--platform linux/amd64 \
--tag csh0034/sample:0.3 .
```

## Troubleshooting

### amd64 환경에서 arm64 이미지 만들어야 할 경우

- [github, qemu-user-static](https://github.com/multiarch/qemu-user-static#getting-started)
- 하단 명령어 실행 할 경우 `docker buildx ls` 에 arm64 나옴

```shell
docker run --rm --privileged multiarch/qemu-user-static --reset -p yes
```


## 참조

- [Docker Buildx](https://docs.docker.com/buildx/working-with-buildx/)
- [Spring Boot Reference, Dockerfiles](https://docs.spring.io/spring-boot/docs/current/reference/html/container-images.html#container-images.dockerfiles)
- [Baeldung, spring boot docker images](https://www.baeldung.com/spring-boot-docker-images)
