# Spring jpa cache (second-level cache)

## 2차캐시 관련 답변내용
- 엔티티는 스프링이나 외부캐시에 저장하면 안된다.
- 엔티티는 영속성 컨텍스트에서 상태를 관리하기 때문에  DTO로 변환하여 저장해야한다.
- 하이버네이트 2차 캐시보다 스프링이 지원하는 서비스 계층 캐시를 사용하는것이 효과적임

## 참조
- [Spring Boot, second-level caching](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-access.configure-hibernate-second-level-caching)
- [Hibernate, caching](https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#caching)
- [인프런, 김영한님 답변](https://www.inflearn.com/questions/33629)