# Apache Camel

## [Apache Camel 이란?](https://camel.apache.org/manual/faq/what-is-camel.html)

- 데이터를 소비하거나 생성하는 다양한 시스템을 쉽고 빠르게 통합할 수 있는 오픈 소스 통합 프레임워크이다.
- 엔터프라이즈 통합 패턴(Enterprise Integration Patterns)을 기반으로 한다.
- Integration 분야에서 전세계적으로 유명한 프로젝트이다.
- [이름이 왜 camel 인지 궁금하면 읽어보자.](https://camel.apache.org/manual/faq/why-the-name-camel.html)

## pom.xml

- [3.14.0 까지가 java8 지원의 마지막 버전이다.](https://camel.apache.org/blog/2021/09/eol-java8/)

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.apache.camel.springboot</groupId>
      <artifactId>camel-spring-boot-bom</artifactId>
      <version>3.14.6</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
<dependencies>
  <dependency>
    <groupId>org.apache.camel.springboot</groupId>
    <artifactId>camel-COMPONENT-starter</artifactId>
  </dependency>
</dependencies>
 ```

### starter groupId 변경

Camel 3.0.0 and newer releases the groupId for Camel Spring Boot support changed   
from `org.apache.camel` to `org.apache.camel.springbooot`

## Data Format

- [Data Format](https://camel.apache.org/components/3.14.x/dataformat-component.html)
- [Data Formats](https://camel.apache.org/components/3.14.x/dataformats/index.html)

## BeanIO

- [Camel BeanIO](https://camel.apache.org/components/3.14.x/dataformats/beanio-dataformat.html#_using_java_dsl)
- [BeanIO V3](https://beanio.github.io/docs/reference-guide/)

## 참조

- [Reference, Apache Camel](https://camel.apache.org/)
- [Baeldung, Introduction To Apache Camel](https://www.baeldung.com/apache-camel-intro)
- [Baeldung, Apache Camel with Spring Boot](https://www.baeldung.com/apache-camel-spring-boot)
