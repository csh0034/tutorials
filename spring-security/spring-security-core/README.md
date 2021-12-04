# Spring Security Core

## Settings
개발환경
- IntelliJ IDEA 2021.3
- spring boot 2.6.1
- spring security 5.6.0
- Java 8
- Maven

pom.xml
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
<dependency>
  <groupId>org.thymeleaf.extras</groupId>
  <artifactId>thymeleaf-extras-springsecurity5</artifactId>
</dependency>
```

## [Spring Security Architecture](https://docs.spring.io/spring-security/reference/servlet/architecture.html)

![01](./images/01.png)

- DelegatingFilterProxy
  - 특정 이름을 가진 bean 을 ApplicationContext 에서 찾아서 위임해준다.
  - `springSecurityFilterChain` 이라는 빈을 찾아 위임한다.
- FilterChainProxy
  - `List<SecurityFilterChain>` filterChains 목록을 관리하며 요청된 Request 를 처리 할 수 있는  
    RequestMatcher 를 가진 SecurityFilterChain 를 찾아서 처리를 위임한다.
  - 처리할수 있는 SecurityFilterChain 가 존재하면 VirtualFilterChain 으로 감싸서 doFilter 를 호출한다.
- SecurityFilterChain
  - Request 를 처리 할 수 있는 필터 체인을 관리한다. 
  - DefaultSecurityFilterChain 는 해당 요청에 적용되는지 여부를 결정하기 위해 RequestMatcher 를 가진다.

## Security Filters
Security Filter 추가 하는 경우에 순서가 중요하다.  
순서를 모두 알 필요는 없지만 중요한 몇가지에 대해선 알아두는것이 개발, 디버깅시에 편하다.

- ChannelProcessingFilter
- WebAsyncManagerIntegrationFilter
- SecurityContextPersistenceFilter
- HeaderWriterFilter
- CorsFilter
- CsrfFilter
- LogoutFilter
- OAuth2AuthorizationRequestRedirectFilter
- Saml2WebSsoAuthenticationRequestFilter
- X509AuthenticationFilter
- AbstractPreAuthenticatedProcessingFilter
- CasAuthenticationFilter
- OAuth2LoginAuthenticationFilter
- Saml2WebSsoAuthenticationFilter
- UsernamePasswordAuthenticationFilter
- OpenIDAuthenticationFilter
- DefaultLoginPageGeneratingFilter
- DefaultLogoutPageGeneratingFilter
- ConcurrentSessionFilter
- DigestAuthenticationFilter
- BearerTokenAuthenticationFilter
- BasicAuthenticationFilter
- RequestCacheAwareFilter
- SecurityContextHolderAwareRequestFilter
- JaasApiIntegrationFilter
- RememberMeAuthenticationFilter
- AnonymousAuthenticationFilter
- OAuth2AuthorizationCodeGrantFilter
- SessionManagementFilter
- ExceptionTranslationFilter
- FilterSecurityInterceptor
- SwitchUserFilter

## 참조
- [Spring, Security](https://docs.spring.io/spring-security/reference/index.html)