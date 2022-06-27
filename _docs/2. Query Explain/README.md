# Query Explain

## Query Explain 이란?

DB가 데이터를 찾아가는 일련의 과정을 사람이 알아보기 쉽게 DB 결과셋으로 보여주는것이다.  

Explain 을 활용하여 기존의 쿼리를 튜닝할 수 있을 뿐만 아니라 성능 분석, 인덱스 전략 수립 등과  
같이 성능 최적화에 대한 전반적인 업무를 처리할 수 있다.

### 사용 방법

```sql
EXPLAIN [EXTENDED | PARTITIONS | FORMAT=JSON] 
  {SELECT select_options | UPDATE update_options | DELETE delete_options}
```

## Columns in EXPLAIN ... SELECT

| Column        | Description                      |
|---------------|----------------------------------|
| id            | 테이블 JOIN 순서를 나타내는 값              | 
| select_type   | SELECT query 실행 종류               | 
| table         | 해당 단계 접근 테이블명(별칭일 경우 별칭 출력)      |
| type          | 테이블 내 record 조회 접근 방식            |
| possible_keys | record 에 접근하기 위한 key 또는 index 목록 |
| key           | record 에 접근하기 위해 참조한 index       |
| key_len       | index 중 참조한 byte 정보              |
| ref           | index 연산 시 비교/연산 사용 기준값          |
| rows          | record 조회 시 접근해야하는 record 예측     |
| filtered      | 테이블 조건으로 필터링된 행의 백분율             |
| Extra         | 추가 정보                            |


## 참조

- [Docs, MySql Explain](https://dev.mysql.com/doc/refman/8.0/en/explain.html)
- [Docs, Mariadb Explain](https://mariadb.com/kb/en/explain/)
