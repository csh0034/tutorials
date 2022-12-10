# Character Sets and Collations

## Character Set

- 심볼(Symbol)과 인코딩(Encoding)의 집합
- 각 문자가 컴퓨터에 저장될 때 어떠한 '코드'로 저장될지에 대한 규칙의 집합을 의미

### MySQL 지원 Character Set 목록

```sql
show character set; 
```

## Collation

- 특정 Character Set 으로 구성된 문자열을 비교하는 방법
- 정렬등의 작업을 할때 사용하는 규칙의 집합을 의미

### MySQL 지원 Collation 목록

```sql
show collation; 
```

## UTF-8 (Unicode Transformation Format - 8bit)

- 가장 많이 사용되는 가변 길이 유니코드 인코딩
- 최대 표현 가능 길이는 6 byte 이지만 다른 인코딩과 호환을 위해 1 ~ 4 byte 만 사용한다.

## MySQL(MariaDB) UTF-8

- MySQL(MariaDB) 은 UTF-8 을 최초설계시 3 byte 가변 자료형으로 설계하였다.
- 따라서 4byte 의 이모지 등을 저장할때 문제가 발생하여 utf8mb4 라는 4 byte 가변 자료형의 추가되었다.
- utf8mb4 / utf8mb4_general_ci 를 사용하면 된다.
- utf8mb4_general_ci(utf8mb4 의 default) / utf8mb4_unicode_ci 둘다 유사해서 아무거나 써도 된다. 
- [docs, utf8](https://dev.mysql.com/doc/refman/8.0/en/charset-unicode-utf8.html)
- [docs, utf8mb4](https://dev.mysql.com/doc/refman/8.0/en/charset-unicode-utf8mb4.html)

## Character Set and Collation 확인

### default Character Set and Collation 확인

```sql
select @@character_set_database, @@collation_database;
```

### 전체 데이베이스의 Character Set and Collation 목록 

```sql
select *
from information_schema.SCHEMATA;
```

### 현재 데이터베이스의 전체 Table Collation 목록

```sql
show table status;
```

### 선택한 테이블의 Column Collation 목록 

```sql
show full columns from 테이블명;
```

## 참조

- [docs, Character Sets and Collations in General](https://dev.mysql.com/doc/refman/8.0/en/charset-general.html)
