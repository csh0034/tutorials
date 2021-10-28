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


## 참고
- [동기화 클래스, 블로그](https://multifrontgarden.tistory.com/266)