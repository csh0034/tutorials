# LocalStack

## LocalStack 이란?

- 로컬에서 테스트할 수 있는 AWS 클라우드 환경을 제공한다.  
- 단독으로 실행이 가능하며 이것을 사용하여 로컬환경에서 AWS 서비스를 사용하는 웹 어플리케이션을 쉽게 테스트할 수 있다.  
- AWS 에서 사용되는 서비스들을 대부분 지원하고 있으며 도커를 사용하여 손쉽게 실행할 수 있다.
- [AWS Service Feature Coverage](https://docs.localstack.cloud/aws/feature-coverage/)

```yaml
version: "3.8"

services:
  localstack:
    container_name: "localstack"
    image: localstack/localstack:0.14.3   # 버전에 따라 설정이 다른점이 많아서 지정하였음
    network_mode: bridge
    ports:
      - "4510-4559:4510-4559"  # external service port range
      - "4566:4566"            # LocalStack Edge Proxy
    environment:
      - SERVICES=sns,sqs,s3,dynamodb   # 사용할 서비스 목록
      - DEBUG=${DEBUG-}
      - DATA_DIR=${DATA_DIR-}
      - HOST_TMP_FOLDER=${TMPDIR:-/tmp/}localstack
      - DOCKER_HOST=unix:///var/run/docker.sock
      - AWS_ACCESS_KEY_ID=test            # 내부에서 aws 커맨드 사용을 위해 지정 awslocal 을 사용하면 필요없음
      - AWS_SECRET_ACCESS_KEY=test        # 위와 동일
      - AWS_DEFAULT_REGION=us-east-1      # 위와 동일
    volumes:
      - "${TMPDIR:-/tmp}/localstack:/tmp/localstack"
      - "/var/run/docker.sock:/var/run/docker.sock"
```

> REGION 의 경우 별도 Region 세팅안할 경우 기본값인 us-east-1 로 하는것이 좋음  

## Local AWS Services

- 하단 두개 명령어 사용 가능

```shell
$ awslocal
$ aws --endpoint-url=http://localhost:4566 
```

### SQS

- 명령어 확인 `awslocal sqs help`

```shell
$ awslocal sqs create-queue --queue-name sample-queue
{
    "QueueUrl": "http://localhost:4566/000000000000/sample-queue"
}

$ awslocal sqs list-queues
{
    "QueueUrls": [
        "http://localhost:4566/000000000000/sample-queue"
    ]
}

$ awslocal sqs send-message --queue-url http://localhost:4566/00000000000/sample-queue --message-body smaple..
{
    "MD5OfMessageBody": "a5d810f0d03d51578ae1f8f99a0043b3",
    "MessageId": "441ba4ad-18c7-4807-9722-cdb8c0db707b"
}

$ awslocal sqs receive-message --queue-url http://localhost:4566/00000000000/sample-queue
{
    "Messages": [
        {
            "MessageId": "441ba4ad-18c7-4807-9722-cdb8c0db707b",
            "ReceiptHandle": "MDRjOTE5OTQtMjM5ZC00ODhiLWFkNjQtYWMyNGJlYzdhODllIGFybjphd3M6c3FzOnVzLWVhc3QtMTowMDAwMDAwMDAwMDA6c2FtcGxlLXF1ZXVlIDQ0MWJhNGFkLTE4YzctNDgwNy05NzIyLWNkYjhjMGRiNzA3YiAxNjU0OTQ4NTkxLjc0NTY0MDg=",
            "MD5OfBody": "a5d810f0d03d51578ae1f8f99a0043b3",
            "Body": "smaple.."
        }
    ]
}

$ awslocal sqs delete-queue --queue-url http://localhost:4566/000000000000/sample-queue
```

### Systems Manager Parameter Store

```shell
$ awslocal ssm put-parameter \
    --name "/config/web_local/custom.username" \
    --value "ASk" \
    --type String

$ awslocal ssm put-parameter \
    --name "/config/web_local/custom.password" \
    --value "1234" \
    --type String
    
$ awslocal ssm get-parameters --names /config/web_local/custom.username
$ awslocal ssm get-parameters-by-path --path /config/web_local

$ awslocal ssm delete-parameter --name /config/web_local/custom.username 
```

### Secrets Manager

```shell
$ awslocal secretsmanager list-secrets

$ awslocal secretsmanager create-secret --name /secret/web_local --secret-string '{"custom.username":"ASk","custom.password":"1234"}'

$ awslocal secretsmanager create-secret --name /secret/web_local --secret-string file://web_local.json

$ awslocal secretsmanager update-secret --secret-id /secret/web_local --secret-string file://web_local.json

$ awslocal secretsmanager get-secret-value --secret-id /secret/web_local

$ awslocal secretsmanager delete-secret --secret-id /secret/web_local # 기본 30일 안에 복구 가능

$ awslocal secretsmanager restore-secret --secret-id /secret/web_local

$ awslocal secretsmanager delete-secret --secret-id /secret/web_local --force-delete-without-recovery
```

### S3

접근 경로

- `http://localhost:4566/{bucket}/{file}`
- `http://{bucket}.s3.{region}.localhost.localstack.cloud:4566/{file}}`

```shell
$ awslocal s3 ls

$ awslocal s3 mb s3://sample-bucket

$ awslocal s3 sync . s3://sample-bucket # 현재 디렉토리 버킷에 업로드

$ awslocal s3 ls s3://sample-bucket

$ awslocal s3 rb s3://sample-bucket --force

$ awslocal s3 rm s3://sample-bucket --recursive

$ awslocal s3 rm s3://sample-bucket/sample.json

$ awslocal s3api list-buckets

$ awslocal s3api create-bucket --bucket sample-bucket

$ awslocal s3api put-object --bucket sample-bucket --key sample.json --body sample.json

$ awslocal s3api delete-object --bucket sample-bucket --key sample.json

$ awslocal s3api list-objects --bucket sample-bucket
```

### SNS

```shell
$ awslocal sns create-topic --name default-topic
{
    "TopicArn": "arn:aws:sns:us-east-1:000000000000:default-topic"
}

$ awslocal sns list-topics
{
    "Topics": [
        {
            "TopicArn": "arn:aws:sns:us-east-1:000000000000:default-topic"
        }
    ]
}

$ awslocal sns subscribe \
  --topic-arn arn:aws:sns:us-east-1:000000000000:default-topic \
  --protocol http \
  --notification-endpoint http://host.docker.internal:8080/default-topic
{
    "SubscriptionArn": "arn:aws:sns:us-east-1:000000000000:default-topic:10e02c28-4285-4b5c-b682-268816dad501"
}

$ awslocal sns list-subscriptions
{
    "Subscriptions": [
        {
            "SubscriptionArn": "arn:aws:sns:us-east-1:000000000000:default-topic:10e02c28-4285-4b5c-b682-268816dad501",
            "Owner": "",
            "Protocol": "http",
            "Endpoint": "http://host.docker.internal:8080/default-topic",
            "TopicArn": "arn:aws:sns:us-east-1:000000000000:default-topic"
        }
    ]
}

$ awslocal sns publish \
  --topic-arn arn:aws:sns:us-east-1:000000000000:default-topic \
  --subject 제목... \
  --message 메세지...
{
    "MessageId": "f8a514d9-142c-4d45-a8ca-2bb4dc7ccd97"
}

$ awslocal sns unsubscribe \ 
  --subscription-arn arn:aws:sns:us-east-1:000000000000:default-topic:10e02c28-4285-4b5c-b682-268816dad501

$ awslocal sns delete-topic --topic-arn arn:aws:sns:us-east-1:000000000000:default-topic
```

## Localstack Utils

- LocalStack 을 사용하기 위해 JUnit 러너와 JUnit 5 확장 제공.
- 최신 localstack 도커 이미지를 자동으로 가져와 실행한 다음 테스트가 완료되면 종료하는 JUnit 테스트 러너를 제공.
- aws sdk v1, v2 지원
  - cloud.localstack.awssdkv1.TestUtils
  - cloud.localstack.awssdkv2.TestUtils

```xml
<dependency>
  <groupId>cloud.localstack</groupId>
  <artifactId>localstack-utils</artifactId>
  <version>0.2.20</version>
  <scope>test</scope>
</dependency>
```

```java
@ExtendWith(LocalstackDockerExtension.class)
@LocalstackDockerProperties(services = ServiceName.SQS)
@Slf4j
class LocalStackDockerExtensionTest {
  
  @Test
  void sqs() {
    AmazonSQS sqs = TestUtils.getClientSQS();

    CreateQueueResult queue = sqs.createQueue("sample-queue");
    log.info("queue urls: {}", sqs.listQueues().getQueueUrls());

    SendMessageResult sendMessageResult = sqs.sendMessage(queue.getQueueUrl(), "message...");
    log.info("send message, {}, {}", sendMessageResult.getMessageId(), sendMessageResult.getMD5OfMessageBody());

    ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(queue.getQueueUrl());
    log.info("receive message, {}", receiveMessageResult.getMessages());
  }

}
```

```text
12:56:14.990 [main] INFO com.ask.localstack.LocalStackDockerExtensionTest - queue urls: [http://localhost:5001/000000000000/sample-queue]
12:56:15.027 [main] INFO com.ask.localstack.LocalStackDockerExtensionTest - send message, b11c53c5-f6b6-499a-83f9-d4209db3f126, 563266512e6889f583b2a70e8694f236
12:56:15.040 [main] INFO com.ask.localstack.LocalStackDockerExtensionTest - receive message, [{MessageId: b11c53c5-f6b6-499a-83f9-d4209db3f126,ReceiptHandle: NjZmMGI2NWUtOWQ3OC00NWUwLTk3YTItMjM5ZmM2MzY4NmFmIGFybjphd3M6c3FzOnVzLWVhc3QtMTowMDAwMDAwMDAwMDA6c2FtcGxlLXF1ZXVlIGIxMWM1M2M1LWY2YjYtNDk5YS04M2Y5LWQ0MjA5ZGIzZjEyNiAxNjU1MDA2MTc1LjAzNjkyNjM=,MD5OfBody: 563266512e6889f583b2a70e8694f236,Body: message...,Attributes: {},MessageAttributes: {}}]
```

## 참조

- [Reference, LocalStack](https://docs.localstack.cloud/get-started/)
- [GitHub, LocalStack](https://github.com/localstack/localstack)
- [GitHub, LocalStack Java Utils](https://github.com/localstack/localstack-java-utils)
- [LocalStack 을 활용한 Integration Test 환경 만들기](https://techblog.woowahan.com/2638/)
- [Baeldung, aws-queues-java](https://www.baeldung.com/aws-queues-java)
- [reference, aws cli command](https://docs.aws.amazon.com/cli/latest/reference/)
