# Keycloak

## Keycloak 이란?

Single Sign-On 인증을 위한 오픈 소스 자격 증명 관리 시스템  
최신 애플리케이션 및 서비스를 대상으로 하는 오픈 소스 ID 및 액세스 관리 솔루션이다.

레드햇에서 관리하며 RH-SSO 프로젝트의 업스트림 프로젝트이다.

### 지원 기능

- SSO(Single-Sign-On)
- OpenID Connect, OAuth 2.0 및 SAML 2.0의 세가지 프로토콜을 지원
- Identity Brokering (외부 OIDC 나 SAML idP에 대해 Authenticate)
- Social Login (구글, 깃허브, 페이스북, 트위터 등 소셜 네트워크를 통한 로그인)
- User Federation (LDAP, Active Directory 서버와 동기화
- Client Adapters
- Admin, Account Management Console

### 용어 정리

| 용어     | 설명                                                                            |
|--------|-------------------------------------------------------------------------------|
| Realm  | 인증, 권한 부여가 적용되는 범위의 단위이다.<br/>SSO 기능을 적용한다고 했을 때 SSO 가 적용되는 범위가 하나의 Realm 이다. |
| Client | 인증, 권한 부여 행위를 수행할 어플리케이션을 나타내는 단위이다.                                          |
| User   | 인증을 필요로하는 사용자를 나타낸다.                                                          |
| Role   | User 에게 부여할 권한의 내용을 나타낸다.                                                     |

## Docker 를 통한 설치

### 개발환경

```shell
docker run -d \
-p 8090:8080 \
-e KEYCLOAK_ADMIN=admin \
-e KEYCLOAK_ADMIN_PASSWORD=admin \
--name keycloak \
quay.io/keycloak/keycloak:20.0.0 start-dev
```

### 운영환경, [Running Keycloak in a container](https://www.keycloak.org/server/containers)

#### Running a standard keycloak container

```shell
docker run --name mykeycloak -p 8090:8080 \
        -e KEYCLOAK_ADMIN=admin \
        -e KEYCLOAK_ADMIN_PASSWORD=admin \
        quay.io/keycloak/keycloak:latest \
        start \
        --db=postgres --features=token-exchange \
        --db-url=<JDBC-URL> --db-username=<DB-USER> --db-password=<DB-PASSWORD> \
        --https-key-store-file=<file> --https-key-store-password=<password>
```

### With Docker Compose

- [keycloak, all config](https://www.keycloak.org/server/all-config)
- [keycloak, Using a reverse proxy](https://www.keycloak.org/server/reverseproxy)
- production mode 의 경우 https 가 기본설정이다. 프록시 서버를 사용하도록 설정하여 http 로 접근 가능하다.

```yaml
version: "3.8"

services:
  keycloak-mariadb:
    container_name: keycloak-mariadb
    image: mariadb:10
    environment:
      MYSQL_DATABASE: keycloak
      MYSQL_ROOT_PASSWORD: 111111
      MYSQL_USER: keycloak
      MYSQL_PASSWORD: 111111
    ports:
      - "3336:3306"
    command:
      - "--character-set-server=utf8mb4"
      - "--collation-server=utf8mb4_general_ci"
      - "--skip-character-set-client-handshake"
      - "--lower_case_table_names=0"

  keycloak:
    container_name: keycloak
    image: quay.io/keycloak/keycloak:21.0.0
    environment:
      KEYCLOAK_ADMIN: admin
      KEYCLOAK_ADMIN_PASSWORD: admin
      KC_PROXY: edge # Enables communication through HTTP between the proxy and Keycloak
      KC_HOSTNAME_STRICT: false # Trust the proxy to set hostname
      KC_DB: mariadb
      KC_DB_URL_HOST: keycloak-mariadb
      KC_DB_URL_PORT: 3306
      KC_DB_USERNAME: keycloak
      KC_DB_PASSWORD: 111111
    ports:
      - "7070:8080"
    command:
      - "start"
    depends_on:
      - keycloak-mariadb

```

## 참조

- [keycloak](https://www.keycloak.org/)
- [keycloak, docker](https://www.keycloak.org/getting-started/getting-started-docker)
- [baeldung, spring-boot-keycloak](https://www.baeldung.com/spring-boot-keycloak)
- [Socar Tech Blog, Keycloak 을 이용한 SSO 구축](https://tech.socarcorp.kr/security/2019/07/31/keycloak-sso.html#cisco-%EB%AC%B4%EC%84%A0-%EA%B3%B5%EC%9C%A0%EA%B8%B0-%EC%84%A4%EC%A0%95)
