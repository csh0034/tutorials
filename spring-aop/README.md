# Spring Aop

## Pointcut

- execution: 메서드 실행 조인 포인트를 매칭
- within: 특정 타입 내의 조인 포인트를 매칭
- args: 인자가 주어진 타입의 인스턴스인 조인 포인트
- this: 스프링 빈 객체(스프링 AOP 프록시)를 대상으로 하는 조인 포인트
  - `*` 불가 
  - 부모타입 허용
- target: Target 객체(스프링 AOP 프록시가 가르키는 실제 대상)를 대상으로 하는 조인 포인트
  - `*` 불가 
  - 부모타입 허용
- @target: 실행 객체의 클래스에 주어진 타입의 어노테이션이 있는 조인 포인트
- @within: 주어진 어노테이션이 있는 타입 내 조인 포인트
- @annotation: 메서드가 주어진 어노테이션을 가지고 있는 조인 포인트를 매칭
- @args: 전달된 실제 인수의 런타임 타입이 주어진 타입의 어노테이션을 갖는 조인 포인트
- bean: 스프링 전용 포인트컷 지시자, 빈의 이름으로 포인트컷을 지정

### execution

`@Pointcut("execution(* com.ask..service.*Service.*(..))")`

- Service 로 끝나는 모든 메서드에 대해서 적용
- 슈퍼클래스 메서드에 대해서는 적용되지 않는다

### within

`@Pointcut("within(com.ask.springaop.common.GenericService+)")`

- GenericService 의 하위 클래스까지 적용
- `+` 의 경우 해당 클래스/인터페이스와 모든 서브클래스/구현체에 해당된다

## 참조

- [Docs, Spring AOP](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop)
