# Spring Cloud

## [Release train Spring Boot compatibility](https://spring.io/projects/spring-cloud)
- [Spring Cloud, GitHub wiki](https://github.com/spring-cloud/spring-cloud-release/wiki)

| Release Train	       | Boot Version                          |
|----------------------|---------------------------------------|
| 2021.0.x aka Jubilee | 2.6.x                                 |
| 2020.0.x aka Ilford  | 2.4.x, 2.5.x (Starting with 2020.0.3) |
| Hoxton               | 2.2.x, 2.3.x (Starting with SR5)      |
| Greenwich            | 2.1.x                                 |
| Finchley             | 2.0.x                                 |
| Edgware              | 1.5.x                                 |
| Dalston              | 1.5.x                                 |

## 외부 연동(통합) 서비스

### [Consul, hashicorp](https://www.consul.io/)
`Service Mesh Made Easy`

동적이고 분산된 인프라에서 애플리케이션을 연결하고 구성하기 위해 설계된 고가용성과 분산 환경을 지원하는 솔루션이다. 

- Service Discovery
- Health check (Failure Detection)
- Service Configuration (Key, Value Store)
- Load Balancing
- ...


### [Vault, hashicorp](https://www.vaultproject.io/)

크로스플랫폼을 지원하는 패스워드 및 인증 관리 시스템이다.   
공개되면 안되는 비밀번호, API 키, 토큰 등을 저장하고 관리한다.

다양한 Storage Backend 를 지원한다.

- Consul
- Zookeeper
- MySQL
- MSSQL
- In-Memory
- AWS S3
- AWS DynamoDB
- Azure Storage Container
- GCP Cloud Storage
- GCP Cloud Spanner
- ...

### [Zookeeper, Apache Software](https://zookeeper.apache.org/)

분산 코디네이션 서비스를 제공하는 오픈소스 프로젝트이다.  
공개 분산형 구성 서비스, 동기 서비스 및 대용량 분산 시스템을 위한 네이밍 레지스트리를 제공한다.

- Service Discovery
- Service Configuration
- ...

## 참조
- [Spring Cloud, reference](https://docs.spring.io/spring-cloud/docs/current/reference/html/)
- [Spring Cloud Config, GitHub](https://github.com/spring-cloud/spring-cloud-config)
