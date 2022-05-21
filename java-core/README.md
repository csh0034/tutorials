# java core

## ForkJoin 프레임워크
- ForkJoin 프레임워크은 자바에서 병렬 처리를 지원하는 프레임워크로 새롭게 소개된 개념이 아니라 Java 7 부터 포함된 개념이다.   
  하나의 작업을 여러개의 작은 Task로 split 한 다음 이 Task를 fork 하여(물리적인 프로세스로 fork 하는 것이 아니라 실제로는 Thread)   
  병렬로 처리하고 각 Task의 수행된 결과를 병합하기 위해 join 단계를 실행하는 프레임워크이다.
- 다른 종류의 ExecutorService와는 다르게 Work-Stealing 매커니즘을 사용
- common pool 이 반납되지 않고 점유중일 경우 더이상 요청은 처리되지 않고 Thread Pool Queue에 쌓이게 되며 일정치 이상이  
  되면 요청이 drop 되는 현상까지 발생

## ExecutorService
- execute()
  - 작업 처리 결과를 받지 못함
  - 작업 처리 중 예외가 발생하면 스레드가 종료되고 해당 스레드는 스레드 풀에서 제거, 스레드 풀은 다른 작업 처리를 위해 새로운 스레드를 생성
- submit()
  - 작업 처리 결과를 Future 타입으로 리턴
  - 작업 처리 중 예외가 발생하면 스레드는 종료되지 않고 다음 작업을 위해 재사용 됩니다.
  
> submit() 사용시에 스레드 삭제/생성에 따른 오버헤드를 줄일 수 있다.

## Java Concurrency – Synchronizer
- CyclicBarrier
  - 해당 장벽에 참여하는 모든 스레드들이 대기상태에 빠진다. 
  - 그리고 조건을 충족할때 모든 스레드들이 대기에서 해제되게된다.
- CountDownLatch
  - Latch 의 사전적 의미는 '걸쇠' 이다. 원하는 지점에서 await() 메서드를 호출해서코드의 진행을 중단시키고
  - 다른 스레드들에서 원하는 횟수만큼 countDown() 메서드를 호출해주면 그때 비로소 코드가 진행되게 된다.
- Semaphore
- Phaser
- Exchanger
- SynchronousQueue

## Maven Test

- Surefire 플러그인은 빌드 수명 주기 의 테스트 단계에서 애플리케이션의 단위 테스트를 실행하는데 사용 된다.
- maven-surefire-plugin 을 추가 하지 않을 경우 Maven `test` LifeCycle 에 Junit 을 실행 안함.

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-surefire-plugin</artifactId>
      <version>2.22.2</version>
    </plugin>
  </plugins>
</build>
```

## Unicode Normalization (유니코드 정규화)

| -   | NFC                | NFD                | NFKC                 | NFKD                 |
|-----|--------------------|--------------------|----------------------|----------------------|
| 유형  | 정규형 C<br>정규형 정준 결합 | 정규형 D<br>정규형 정준 분해 | 정규형 KC<br>정규형 호환성 결합 | 정규형 KD<br>정규형 호환성 분해 |
| 설명  | 정준분해 후, 정준결합       | 	정준분해              | 	호환성 분해 후, 정준결합      | 	호환성 분해              |
| 예시  | 각 -> 각             | 각 -> ㄱㅏㄱ           | 각 -> 각               | 각 -> ㄱㅏㄱ             |

Mac 의 경우 NFD 정규화 방식을 사용함  
Windows 와 Linux 는 NFC 방식을 사용함

> 일반적으로 UTF8 은 NFC 방식을 사용함

Mac 에서 올린 한글 파일명이 윈도우나 리눅스 환경에서는 자, 모음이 분리된 것처럼 보임   
따라서 NFC 아닐 경우 NFC 로 변환 해줘야함

```java
import java.text.Normalizer;

// NFC 방식이 아닐경우 NFC 방식으로 변경
public static String normalizeNfc(String value) {
  if (!Normalizer.isNormalized(value, Normalizer.Form.NFC)) {
    return Normalizer.normalize(value, Normalizer.Form.NFC);
  }
  return value;
}
```

- SublimeText 는 기본적으로 NFC 로 동작 하므로 finder 에서 파일명을 복사해서 붙혀넣기 하면 한글이 분해됨
- Mac 에서 Numbers 로 엑셀을 보면 안깨져있지만 Microsoft Excel 로 보면 깨져있음

## 참고
- [동기화 클래스, 블로그](https://multifrontgarden.tistory.com/266)
