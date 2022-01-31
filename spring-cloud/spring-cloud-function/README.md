# Spring Cloud Function

## [AWS Deploy](https://docs.spring.io/spring-cloud-function/docs/current/reference/html/sprㄱng-cloud-function.html#_aws_request_handlers)

요청 핸들러
> MAIN_CLASS=org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest

둘 이상 Function 이 있을 경우 하단 프로퍼티 지정.  
환경변수는 대문자 언더스코어로 지정해야 스프링에서 인식함. [Binding from Environment Variables](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config.typesafe-configuration-properties.relaxed-binding.environment-variables)
- Replace dots (.) with underscores (_).
- Remove any dashes (-).
- Convert to uppercase.

> SPRING_CLOUD_FUNCTION_DEFINITION=uppercase

## 참조
- [Spring Cloud Function, Reference](https://docs.spring.io/spring-cloud-function/docs/3.2.1/reference/html/)
- [Spring Cloud Function, GitHub](https://github.com/spring-cloud/spring-cloud-function)
- [Blog 1, Introduce](https://binux.tistory.com/61?category=907689)
- [Blog 2, AWS Deploy](https://siyoon210.tistory.com/174)
