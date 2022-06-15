# Spring Cloud Aws S3

별도 라이브러리가 존재하는것은 아니고 `spring-cloud-starter-aws` 에 포함되어있음

## File Download

```java
Resource resource = resourceLoader.getResource("s3://my-bucket/rootFile.log");
InputStream inputStream = resource.getInputStream();
```

## File Upload

```java
Resource resource = resourceLoader.getResource("s3://my-bucket/rootFile.log");
WritableResource writableResource = (WritableResource) resource;

try (OutputStream outputStream = writableResource.getOutputStream()) {
  outputStream.write("test".getBytes());
}
```

## LocalStack 사용시 접근 URL

```text
http://localhost:4566/{bucket}/{file}
http://{bucket}.s3.{region}.localhost.localstack.cloud:4566/{file}}
http://{bucket}.s3..localhost.localstack.cloud:4566/{file}}

http://localhost:4566/sample-bucket/sample.json
http://sample-bucket.s3.us-east-1.localhost.localstack.cloud:4566/sample.json
http://sample-bucket.localhost.localstack.cloud:4566/sample.json
```

## 참조

- [Reference, spring-cloud-aws Resource handling](https://docs.awspring.io/spring-cloud-aws/docs/current/reference/html/index.html#resource-handling)
- [Docs, Amazon S3](https://docs.aws.amazon.com/ko_kr/AmazonS3/latest/userguide/Welcome.html)
