# Spring Cloud Aws Secrets Manager

## secrets manager 프로퍼티 규칙

> {prefix}/{name}{profile-separator}{profile}

- `aws.secretsmanager.prefix`, default: `/secret`
- `aws.secretsmanager.profile-separator`, default: `_`
- `aws.secretsmanager.name` 의 경우 `spring.application.name` 로 대체 가능
- `spring.profiles.active`

### 예시

- /secret/web_local
- /secret/web_prod

해당 key 에 대해서 value 를 json 으로 구성하면된다.

- AwsSecretsManagerPropertySource 에서 처리
- json 을 Map 으로 변환하여 key, value 를 사용한다.

```json
{
  "custom.username": "ASk",
  "custom.password": "1234"
}
```

## 참고

### Credentials 처리,  Endpoint Region Setting

파라미터 스토어와 유사함

## 참조

- [Docs, AWS Secrets Manager](https://docs.aws.amazon.com/ko_kr/secretsmanager/latest/userguide/intro.html)
