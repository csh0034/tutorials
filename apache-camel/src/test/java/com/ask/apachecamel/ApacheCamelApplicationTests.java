package com.ask.apachecamel;

import com.ask.apachecamel.beanio.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.DataFormat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootTest
@Slf4j
class ApacheCamelApplicationTests {

  @Autowired
  private CamelContext camelContext;

  @Test
  void marshal() {
    ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
    producerTemplate.sendBody("direct:marshal", Fixtures.employees());
  }

  @Test
  void unmarshal() {
    ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
    producerTemplate.sendBody("direct:unmarshal", Fixtures.stringEmployees());
  }

  @TestConfiguration
  static class TestConfig {

    @Autowired
    private DataFormat dataFormat;

    @Bean
    public RouteBuilder marshalRoute() {
      return new RouteBuilder() {
        @Override
        public void configure() {
          from("direct:marshal")
//              .bean("sampleBean")
              .to("bean:sampleBean")
//              .marshal(dataFormat)
//              .to("dataformat:beanio:marshal?mapping=mapping.xml&streamName=employeeFile") // 자동설정에 의해 지정 안해도됨.
              .to("dataformat:beanio:marshal")
              .process(exchange -> {
                String body = exchange.getIn().getBody(String.class);
                log.info("body: {}", body);
              });
        }
      };
    }

    @Bean
    public RouteBuilder unmarshalRoute() {
      return new RouteBuilder() {
        @Override
        public void configure() {
          from("direct:unmarshal")
//              .unmarshal(dataFormat)
              .to("dataformat:beanio:unmarshal")
              .split(body())
//              .log("${body}")
              .process(exchange -> {
                Employee employee = exchange.getIn().getBody(Employee.class);
                log.info("employee: {}", employee);
              });
        }
      };
    }

  }

}
