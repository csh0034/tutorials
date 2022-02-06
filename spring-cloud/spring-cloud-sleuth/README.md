# Spring Cloud Sleuth
Spring Cloud 용 분산 추적 솔루션용 API 를 제공 하며, OpenZipkin Brave 와 통합을 지원한다.

요청과 메시지를 추적할 수 있으므로 해당 통신을 해당 로그 항목과 연관시킬 수 있다.   
추적 정보를 외부 시스템으로 내보내 대기 시간을 시각화할 수도 있다.

OpenZipkin 호환 시스템을 직접 지원한다.

[Dapper](https://research.google/pubs/pub36356/) 의 용어를 사용한다.

##  OpenTracing
CNCF(Cloud Native Computing Foundation) 에서 만든 비공식 분산 추적 표준이다.  
대표적인 구현체로 Jaeger, Zipkin 등이 있다.

## Zipkin
분산 추적 시스템이며 서비스 아키텍처의 대기 시간 문제를 해결하는 데 필요한 타이밍 데이터를 수집하는 데 도움이 된다.  
기능에는 이 데이터의 수집 및 조회가 모두 포함된다.

### docker (In-memory 방식)
```shell
$ docker run -d -p 9411:9411 --name zipkin openzipkin/zipkin
```


## 참조
- [Spring Cloud Sleuth, Reference](https://docs.spring.io/spring-cloud-sleuth/docs/3.1.0/reference/html/index.html)
- [Spring Cloud Sleuth 2.2.4, 번역](https://velog.io/@hanblueblue/%EB%B2%88%EC%97%AD-Spring-Cloud-Sleuth-1-Introduction)
- [zipkin, Reference](https://zipkin.io/)
- [zipkin, dockerhub](https://hub.docker.com/r/openzipkin/zipkin/)
- [Blog 1, Distributed Tracing](https://ksr930.tistory.com/112)
- [Blog 2](https://happycloud-lee.tistory.com/216?category=902419)
- [Blog 3](https://velog.io/@hanblueblue/Spring-boot%EB%A1%9C-Spring-Cloud-Sleuth-Zipkin-%EC%8B%A4%EC%8A%B5)
