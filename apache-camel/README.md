# Apache Camel

## pom.xml

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.apache.camel.springboot</groupId>
      <artifactId>camel-spring-boot-bom</artifactId>
      <version>3.16.0</version>
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

## 참조

- [Reference, Apache Camel](https://camel.apache.org/)
- [Baeldung, Introduction To Apache Camel](https://www.baeldung.com/apache-camel-intro)
- [Baeldung, Apache Camel with Spring Boot](https://www.baeldung.com/apache-camel-spring-boot)
