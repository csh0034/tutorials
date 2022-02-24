# Spring Test Rest Assured

## Rest Assured 란?

Java 에서 REST 서비스를 테스트하고 검증하는 것은 Ruby 및 Groovy 와 같은 동적 언어보다 어렵다.   
REST Assured 는 Java 에서 간편하게 이러한 테스트를 할 수 있도록 기능을 제공한다.

REST 웹 서비스를 검증하기 위한 라이브러리이며 End-to-End Test(전 구간 테스트)에 사용된다.

내부적으론 Apache HttpClient 를 사용하여 API 를 호출한다.

## Rest Assured VS MockMvc

### Rest Assured

HttpClient 를 호출하여 실제 HTTP 호출을 하므로 `@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)` 로  
서블릿 컨테이너를 실행시킨후에 테스트를 진행해야한다. 

[io.rest-assured:spring-mock-mvc](https://github.com/rest-assured/rest-assured/wiki/Usage#spring-mock-mvc-module) 모듈을 사용하면 MockMvc 와 통합하여 @WebMvcTest 에서도 Rest Assured 를 사용할 수 있다.

### MockMvc

MockMvc 의 장점은  mock servlet 을 사용하여 테스트를 실행 하므로 웹서버 배포없이 통합 테스트를 실행 할 수 있다는 점이다.  
`@SpringBootTest(webEnvironment = WebEnvironment.MOCK)` 또는 `@WebMvcTest` 로 사용 가능하다.

## 참조
- [rest-assured, Reference](https://rest-assured.io/)
- [rest-assured, GitHub wiki](https://github.com/rest-assured/rest-assured/wiki/Usage)
- [spring-mock-mvc-rest-assured](https://www.baeldung.com/spring-mock-mvc-rest-assured)
