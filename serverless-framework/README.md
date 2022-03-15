# Serverless Framework

## Serverless Framework 란?

- AWS, Azure, GCP 등의 Cloud Serverless Service 에 코드를 쉽게 배포해 주는 프레임워크이다.
- Serverless Architecture 를 손쉽게 개발, 테스트, 배포 할 수 있도록 지원한다.

## Settings

### npm 을 통한 설치

```shell
$ npm install -g serverless
```

- serverless 또는 sls 로 사용 가능하다.

### 템플릿 목록 확인

```shell
$ sls create --help
```

### aws-nodejs template 생성

```shell
$ sls create -t {템플릿 명} -p {프로젝트 명}

# sample
$ sls create -t aws-nodejs -p aws-nodejs-sample
```

### GitHub Sample 을 통한 template 생성

- [Serverless Examples, GitHub](https://github.com/serverless/examples)

```shell
$ sls create \
  -u https://github.com/serverless/examples/tree/master/{folder-name} \
  -p {프로젝트 명}

# sample  
$ sls create \
  -u https://github.com/serverless/examples/tree/master/aws-node-simple-http-endpoint \
  -p aws-nodejs-http-sample
```

### Local Invoking function

- 생성한 function 을 로컬에서 테스트 할 수 있도록 지원한다.
- `--path / -p` 옵션뒤에 json 또는 yaml file 설정시 input data 를 설정 가능하다. 

```shell
$ sls invoke local -f {함수명}

# sample 1
$ sls invoke local -f hello

# sample 2, input data setting
$ sls invoke local -f hello -p sample.json
```

## 참조
- [Serverless Framework, Reference](https://www.serverless.com/framework/docs/getting-started)
- [Serverless Examples, GitHub](https://github.com/serverless/examples/)
- [AWS Lambda Node.js, Reference](https://docs.aws.amazon.com/ko_kr/lambda/latest/dg/lambda-nodejs.html)
- [AWS Lambda SES 이메일에 대한 SNS 알림 DynamoDB에 저장, Reference](https://aws.amazon.com/ko/premiumsupport/knowledge-center/lambda-sns-ses-dynamodb/)
- [Blog 1, Monitoring Your Email Bounces and Bounce Rate using Amazon SES, Lambda, SNS, and DynamoDB](https://medium.com/swlh/monitoring-your-email-bounces-and-bounce-rate-using-amazon-ses-lambda-sns-and-dynamodb-ce74859da18f)
- [Blog 2, AWS SES Bounce 처리](https://isntyet.tistory.com/140)
