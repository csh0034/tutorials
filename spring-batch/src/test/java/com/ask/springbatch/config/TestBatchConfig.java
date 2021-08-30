package com.ask.springbatch.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * spring boot 는 EnableAutoConfiguration 이 선언된 패키지 부터 EntityScan, EnableJpaRepositories 을 적용하는데
 * Job 테스트시에 classes 지정하여 SpringBootApplication 안에 있는 EnableAutoConfiguration 이 동작하지 않기때문에 추가로 선언함
 */
@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
@EntityScan(basePackages = "com.ask.springbatch")
@EnableJpaRepositories(basePackages = "com.ask.springbatch")
@ComponentScan(basePackages = {"com.ask.springbatch.config"})
public class TestBatchConfig {

}
