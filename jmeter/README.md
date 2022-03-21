# Apache JMeter

## JMeter 란?

- 어플리케이션의 성능 및 부하를 테스트 및 측정하도록 설계된 오픈 소스 Java 응용 프로그램이다.
- 다양한 형태의 어플리케이션 테스트 지원
  - Web (HTTP, HTTPS)
  - SOAP / REST
  - FTP
  - Database
  - Mail
  - ...
- CLI 지원
  - CI 또는 CD 툴과 연동할때 편리
  - UI 를 사용할때보다 메모리 등 시스템 리소스 적게 사용

### 개념

- Thread Group: 한 쓰레드 당 유저 한명
- Sampler: 어떤 유저가 해야 하는 액션
- Listener: 응답을 받았을때 할 일(리포팅, 검증, 그래프 등등)
- Configuration: Sampler 또는 Listener 가 사용할 설정 값(쿠키, JDBC 커넥션 등)
- Assertion: 응답이 성공적인지 확인하는 방법(응답 코드, 본문 내용 등)

### 사용법

#### Thread Group 만들기

- Number of Threads: 쓰레드 개수
- Ramp-up period: 쓰레드 개수를 만드는데 소요할 시간
- Loop Count: infinite 체크 하면 위에서 정한 쓰레드 개수로 계속 요청 보내기.   
   값을 입력하면 해당 쓰레드 개수 X 루프 개수 만큼 요청 보냄.

#### Sampler 만들기

- 여러 종류의 Sampler 가 존재한다.
- HTTP Sampler
  - 요청을 보낼 호스트, 포트, URI, 요청 본문 등을 설정
- 여러 샘플러를 순차적으로 등록하는 것도 가능하다.

#### Listener 만들기

- View Results Tree
- View Results in Table
- Summary Report
- Aggregate Report
- Response Time Graph
- Graph Results
- ...

#### Assertion 만들기

- 응답 코드 확인
- 응답 본문 확인
- ...


### 유사 프로그램

- [Apache Bench](https://httpd.apache.org/docs/2.4/ko/programs/ab.html)
- [Gatling](https://gatling.io/)
- [nGrinder](https://naver.github.io/ngrinder/)

## Settings

### brew 를 통한 설치

```shell
$ brew install jmeter
```

### 압축파일 download

[Download Apache JMeter](https://jmeter.apache.org/download_jmeter.cgi)

### JMeter GUI 실행

```shell
# brew 설치시
$ jmeter

# 압축파일 다운로드시
$ apache-jmeter-{version}/bin/jmeter
```

### JMeter CLI 실행

- gui 로 저장시에 생성된 `.jmx` 파일을 읽어 실행한다.

```shell
$ jmeter -n -t 설정 파일 -l 리포트 파일

# sample 
$ jmeter -n -t Perf-Test.jmx -l report.log
```

## Troubleshooting

### 실행시 jmeter.log 관련 생성 로그 발생할 경우

jmeter 명령어 실행시에 현재 디렉토리에 jmeter.log 파일을 생성한다.  
따라서 명령어를 실행하는 위치에 파일 쓰기 권한이 있어야한다.

### JavaNativeFoundation 관련 로그 발생시

- libjvm.dylib 파일을 못찾아서 생기는 로그
- server directory 안의 libjvm.dylib 에 대해서 심볼릭 링크 생성

```shell
sudo ln -s /usr/local/opt/openjdk/libexec/openjdk.jdk/Contents/Home/lib/server/libjvm.dylib /usr/local/opt/openjdk/libexec/openjdk.jdk/Contents/Home/lib/libjvm.dylib
```

### 저장 안될 경우

Tools -> Look and Feel -> System 으로 변경

## 참조
- [Apache JMeter, Reference](https://jmeter.apache.org/)
- [Apache JMeter, GitHub](https://github.com/apache/jmeter)
- [JavaNativeFoundation error 관련](https://hsik0225.github.io/jmeter/2021/09/16/JMeter-JavaNativeFoundation/)
- [Blog 1](https://effortguy.tistory.com/164)
