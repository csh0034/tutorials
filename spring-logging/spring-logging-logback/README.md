# Spring Logging Logback

## 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true" scanPeriod="5 seconds">
<appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
  <file>./logs/test.log</file>
  <rollingPolicy class="ch.qos.logback.core.rolling.FixedWindowRollingPolicy">
    <fileNamePattern>./logs/test-%i.log.gz</fileNamePattern>
    <minIndex>1</minIndex>
    <maxIndex>3</maxIndex>
  </rollingPolicy>
  <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
    <maxFileSize>50MB</maxFileSize>
  </triggeringPolicy>
  <encoder>
    <charset>UTF-8</charset>
    <pattern>[%d{yyyy-MM-dd HH:mm:ss}]%5level[%t] %logger{0}.%M\(%L\) | %msg%n</pattern>
  </encoder>
</appender>

<logger name="{package}" additivity="false">
  <level value="INFO" />
  <appender-ref ref="FILE" />
</logger>
  
</configuration>
```

## 참조

- [Logback, Docs](https://logback.qos.ch/index.html)
- [Mask Sensitive Data in Logs With Logback, baeldung](https://www.baeldung.com/logback-mask-sensitive-data)
- [improved Java Logging with Mapped Diagnostic Context (MDC)
  , baeldung](https://oddblogger.com/spring-boot-mdc-logging)
- [Spring Boot Applications with MDC for Better Logging](https://oddblogger.com/spring-boot-mdc-logging)
