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
$ sls create -t {원하는 템플릿} -p {원하는 프로젝트 이름}

# sample
$ sls create -t aws-nodejs -p aws-nodejs-sample
```

### Local Invoking function

- 생성한 function 을 로컬에서 테스트 할 수 있도록 지원한다.

```shell
$ sls invoke local -f {함수명}

# sample
$ sls invoke local -f hello
```

## 참조
- [Serverless Framework, Reference](https://www.serverless.com/framework/docs/getting-started)
- [Serverless Examples, GitHub](https://github.com/serverless/examples/)
