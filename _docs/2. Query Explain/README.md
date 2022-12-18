# Query Explain (MariaDB 기준)

## Query Explain 이란?

대부분의 DBMS 의 목적은 많은 데이터를 안전하게 저장 및 관리하고 사용자가 원하는 데이터를  
빠르게 조회할 수 있게 해주는것이 주 목적이다.

이러한 목적을 위해 옵티마이저가 사용자의 쿼리를 최적으로 처리될 수 있게 하는 쿼리의 실행계획을  
수립할 수 있어야 한다.

하지만 옵티마이저가 항상 좋은 실행 계획을 만들어낼 수 있는것은 아니므로 이러한 문제점을 보완  
할 수 있도록 `EXPLAIN` 명령으로 옵티마이저가 수립한 실행 계획을 확인할 수 있게 해준다.

`EXPLAIN` 을 활용하여 기존의 쿼리를 튜닝할 수 있을 뿐만 아니라 성능 분석, 인덱스 전략 수립 등과  
같이 성능 최적화에 대한 전반적인 업무를 처리할 수 있다.

### 사용 방법

```sql
EXPLAIN [EXTENDED | PARTITIONS | FORMAT=JSON] 
  {SELECT select_options | UPDATE update_options | DELETE delete_options}
```

## Columns in EXPLAIN ... SELECT

| Column        | Description                             |
|---------------|-----------------------------------------|
| id            | 테이블 JOIN 순서를 나타내는 값                     | 
| select_type   | SELECT query 실행 종류                      | 
| table         | 해당 단계 접근 테이블명(별칭일 경우 별칭 출력)             |
| type          | 테이블 내 record 조회 접근 방식                   |
| possible_keys | record 에 접근하기 위한 key 또는 index 목록        |
| key           | record 에 접근하기 위해 참조한 index              |
| key_len       | index 중 참조한 byte 정보                     |
| ref           | index 연산 시 비교/연산 사용 기준값                 |
| rows          | record 조회 시 접근해야하는 record 예측            |
| filtered      | 테이블 조건으로 필터링된 행의 백분율, `EXTENDED` 키워드 필요 |
| Extra         | 추가 정보                                   |


### id

단위 SELECT 쿼리별로 부여되는 식별자 값이다.  

테이블을 조인하면 조인되는 테이블 개수만큼 실행계획 레코드가 출력되지만  
같은 id 값이 부여된다.

만약 쿼리안에 서브쿼리 등과 같이 서로 다른 실행 단위로 구성되어 있으면  
각 레코드의 id 는 다르게 부여된다.

여기서 주의할점은 실행 계획의 id 컬럼이 테이블의 접근 순서를  
의미하지는 않는다는것이다.

### select_type

### table

단위 SELECT 쿼리 기준이 아닌 테이블 기준으로 표시된다.  
테이블의 이름에 별칭이 부여된경우에는 별칭이 표시된다.

### type

### possible_keys

옵티마이저가 최적의 실행 계획을 만들기 위해 후보로 선정했던 인덱스 목록  
특별한 경우를 제외하곤 무시해도 된다.

### key

최종 선택된 실행 계획에서 사용하는 인덱스를 의미한다.  
쿼리를 튜닝할 때는 key 컬럼에 의도한 인덱스가 표시되는 확인하는것이 중요하다.

type 컬럼이 index_merge 가 아닌 경우에는 반드시 테이블당 하나의  
인덱스만 이용할 수 있다.

### key_len

인덱스의 각 레코드에서 몇 바이트까지 사용했는지를 의미한다.

많은 사용자가 쉽게 무시하는 정보지만 사실은 매우 중요한 정보이다.

쿼리를 처리하기 위해 다중 칼럼으로 구성된 인덱스에서 몇 개의 컬럼까지  
사용했는지를 알려준다.

### ref

접근 방법(type)이 ref 면 참조 조건(Equal 비교 조건) 으로 어떤 값이  
제공됐는지 보여준다.

상숫값을 지정했다면 ref 컬럼값은 const 로 표시되며 다른 테이블의 컬럼 값이면  
그 테이블명과 컬럼명이 표시된다.

func 로 표시될 경우 Function 의 줄임말로 값을 그대로 사용한것이 아닌  
콜레이션 변환이나 값 자체의 연을 거쳐 참조됐다는 것을 의미한다.

### rows

실행 계획의 효율성 판단을 위해 예측했던 레코드 건수를 보여준다.

이 값은 각 스토리지 엔진별로 가진 통계 정보를 참조해 MySQL 옵티마이저가  
산출해낸 예상값이라서 정확하지는 않다.

반환하는 레코드의 예측치가 아니라 쿼리를 처리하기 위해 얼마나 많은 레코드를 읽고  
체크해야 하는지를 의미한다.

### filtered

`EXPLAIN` 뒤에 `EXTENDED` 키워드가 필요하다.

필터링되어 버려지는 레코드의 비율이 아닌 필터링되고 남은 레코드의  
추정 백분율을 의미한다.

### Extra

## 참조

- [Docs, MariaDB Explain](https://mariadb.com/kb/en/explain/)
- [Docs, MySql Explain](https://dev.mysql.com/doc/refman/8.0/en/explain.html)
- [Blog, Mysql Explain](https://cheese10yun.github.io/mysql-explian/)
