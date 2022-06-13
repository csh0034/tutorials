# Spring Cloud Aws Parameter Store

## parameter store 프로퍼티 규칙

> {prefix}/{name}{profile-separator}{profile}/key

- `aws.paramstore.prefix`, default: `/config`
- `aws.paramstore.profile-separator`, default: `_`
- `aws.paramstore.name` 의 경우 `spring.application.name` 로 대체 가능
- `spring.profiles.active`

### 예시

- /config/web_local/custom.username
- /config/web_local/custom.password

## 참고

### Credentials 처리

`spring-cloud-starter-aws-parameter-store-config` 의 경우 `spring-cloud-starter-aws` 의존성을 사용하지  
않고 DefaultAWSCredentialsProviderChain 에 의해 처리됨.

- ProfileCredentialsProvider 로 처리 ~/.aws/credentials

### Endpoint Region Setting

[SSM Service Endpoints](https://docs.aws.amazon.com/general/latest/gr/ssm.html)

endpoint 만으로 region 이 구분되므로 코드에서 region 과 endpoint 를 동시에 세팅할수   
없도록 해놓은걸로보임

따라서 LocalStack 사용시 Default Region 을 `us-east-1` 로 하는것이 좋을것 같음

- AwsParamStoreConfigDataLocationResolver.createSimpleSystemManagementClient
- AwsParamStoreBootstrapConfiguration.createSimpleSystemManagementClient

## 참조

- [AWS Blog, AWS 기반 Spring Boot 애플리케이션 개발 시작하기](https://aws.amazon.com/ko/blogs/korea/getting-started-with-spring-boot-on-aws/)
- [Blog, Resolving Spring Boot Properties Using the AWS Parameter Store (SSM)](https://rieckpil.de/resolving-spring-boot-properties-using-the-aws-parameter-store-ssm/)
- [Docs, AWS Systems Manager Parameter Store](https://docs.aws.amazon.com/ko_kr/systems-manager/latest/userguide/systems-manager-parameter-store.html)
