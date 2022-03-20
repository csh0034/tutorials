# Apache JMeter

## JMeter 란?

어플리케이션의 성능 및 부하를 테스트 및 측정하도록 설계된 오픈 소스 Java 응용 프로그램이다.


## Settings

### brew 를 통한 설치

```shell
$ brew install jmeter
```

### jmeter 실행

```shell
$ jmeter
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
