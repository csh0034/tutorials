# Spring Security OAuth2 Client

## Spring Security OIDC 인증 처리

- OAuth2AuthorizationRequestRedirectFilter
  - End User 의 user-agent 를 권한서버 Authorization Endpoint 로 리다이렉트 하여   
    authorization code grant 또는 implicit grant 흐름을 처리한다. 
  - `/oauth2/authorization/{registrationId}` 에 대한 요청을 처리함
  - Google 의 경우 `https://accounts.google.com/o/oauth2/v2/auth` 로 리다이렉트한다.
- OAuth2LoginAuthenticationFilter
  - OAuth2LoginAuthenticationProvider
    - OAuth 2.0 authorization code grant 흐름을 활용하는 OAuth 2.0 로그인에서 사용
    - OIDC 를 사용할 경우 하단 provider 에서 처리한다.
  - OidcAuthorizationCodeAuthenticationProvider
    - OpenID Connect Core 1.0 authorization code grant 흐름에 대한 처리
    - Google 의 경우 `https://www.googleapis.com/oauth2/v4/token` 로 요청하여   
      id token, access token 을 받아온다.

인증이 필요한 유저가 접근할때 설정에 loginPage 세팅이 없으며   
ClientRegistration 이 1개 일 경우 자동으로 OIDC 로그인 처리를 한다.

### Google OIDC 처리 순서

1. /oauth2/authorization/google
2. OAuth2AuthorizationRequestRedirectFilter  
   `https://accounts.google.com/o/oauth2/v2/auth` 로 리다이렉트 `Authorization Code` 요청
3. OAuth2LoginAuthenticationFilter  
    파라미터로 전달한 redirect_uri(`/login/oauth2/code/google`) 로 `Authorization Code` 를 받는다.
4. OidcAuthorizationCodeAuthenticationProvider  
   `https://www.googleapis.com/oauth2/v4/token` 로 요청하여 id token, access token 을 발급 받는다.
5. OidcAuthorizationCodeAuthenticationProvider, `jwtDecoder.decode`  
   jwkSetUri (`https://www.googleapis.com/oauth2/v3/certs`) 를 통해 가져온 jwk 로 id 토큰을 검증한다.

## OIDC(OpenID Connect) 란?

- OAuth 2.0 프로토콜의 최상위 레이어와 동일한 레이어다.   
- OAuth 2.0을 확장하여 인증 방식을 표준화한다.

## 참고 사항

웹앱 Google 로그인 자바스크립트 플랫폼 라이브러리가 지원중단될 예정  
2023년 03월 31 이후에는 라이브러리 다운로드 안됨

2022년 7월 29일 이전에 생성된 새 클라이언트 ID는 Google 플랫폼 라이브러리를 사용하도록   
[plugin_name](https://developers.google.com/identity/sign-in/web/reference) 을 설정할 수 있다.

## 참조

- [Spring Security Reference, OAuth 2.0 Client](https://docs.spring.io/spring-security/reference/servlet/oauth2/client/index.html)
- [baeldung, Spring Security and OpenID Connect](https://www.baeldung.com/spring-security-openid-connect)
- [편의성을 높인 ID 인증 관리: OIDC가 주목 받는 이유
  ](https://s-core.co.kr/insight/view/%ED%8E%B8%EC%9D%98%EC%84%B1%EC%9D%84-%EB%86%92%EC%9D%B8-id-%EC%9D%B8%EC%A6%9D-%EA%B4%80%EB%A6%AC-oidc%EA%B0%80-%EC%A3%BC%EB%AA%A9-%EB%B0%9B%EB%8A%94-%EC%9D%B4%EC%9C%A0/)
- [Oauth 2.0과 OpenID Connect 프로토콜 정리](https://velog.io/@jakeseo_me/Oauth-2.0%EA%B3%BC-OpenID-Connect-%ED%94%84%EB%A1%9C%ED%86%A0%EC%BD%9C-%EC%A0%95%EB%A6%AC)
- [Docs, Google 로그인 버튼 만들기](https://developers.google.com/identity/sign-in/web/build-button)
- [Docs, 웹 앱에 Google 로그인 추가](https://developers.google.com/identity/sign-in/web)
- [google, OpenID Connect](https://developers.google.com/identity/openid-connect/openid-connect#java)
- [kakao developers, OpenID Connect](https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#oidc)
