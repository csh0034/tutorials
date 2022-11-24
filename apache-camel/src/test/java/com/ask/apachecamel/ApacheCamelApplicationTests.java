package com.ask.apachecamel;

import com.ask.apachecamel.beanio.Employee;
import com.ask.apachecamel.beanio.FixedLengthDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootTest
@Slf4j
class ApacheCamelApplicationTests {

  @Autowired
  private ProducerTemplate producerTemplate;

  @Autowired
  private ConsumerTemplate consumerTemplate;

  @DisplayName("csv marshal")
  @Test
  void marshal() {
    // Event Message (InOnly)
    producerTemplate.sendBody("direct:marshal", Fixtures.employees());

    // request methods - Request Reply (InOut)
    String result = producerTemplate.requestBody("direct:marshal", Fixtures.employees(), String.class);
    log.info("result: {}", result);
  }

  @DisplayName("fixed length marshal")
  @Test
  void marshal2() {
    producerTemplate.sendBody("direct:marshal2", Fixtures.fixedLengthDto());
  }

  @DisplayName("csv unmarshal")
  @Test
  void unmarshal() {
    producerTemplate.sendBody("direct:unmarshal", Fixtures.stringEmployees());
  }

  @DisplayName("fixed length unmarshal")
  @Test
  void unmarshal2() {
    producerTemplate.sendBody("direct:unmarshal2", Fixtures.stringFixedLengthDto());
  }

  @DisplayName("수동으로 메세지를 가져온다.")
  @Test
  void manualRoute() {
    String endpointUri = "direct:manualRoute?timeout=5000";

    // 동기로 보낼 경우 receive 할때 까지 blocking 상태이므로 비동기 send 처리함
    producerTemplate.asyncSendBody(endpointUri, "sample!!");

//    String result = (String) consumerTemplate.receiveBodyNoWait(endpointUri);
    String result = (String) consumerTemplate.receiveBody(endpointUri, 2000);
    log.info("result: {}", result);
  }

  @TestConfiguration
  static class TestConfig {

    @Bean
    public EndpointRouteBuilder marshalRoute() {
      return new EndpointRouteBuilder() {
        @Override
        public void configure() {
          from("direct:marshal")
//              .bean("sampleBean")
              .to("bean:sampleBean")
//              .marshal(dataFormat)
//              .marshal().beanio("mapping.xml", "employeeFile")
//              .to("dataformat:beanio:marshal?mapping=mapping.xml&streamName=employeeFile") // 자동설정에 의해 지정 안해도됨.
//              .to(dataformat("beanio:marshal"))
              .to("dataformat:beanio:marshal")
              .process(exchange -> {
                String body = exchange.getIn().getBody(String.class);
                log.info("body: {}", body);
              });
        }
      };
    }

    @Bean
    public RouteBuilder fixedLengthMarshalRoute() {
      return new RouteBuilder() {
        @Override
        public void configure() {
          from("direct:marshal2")
              .to("dataformat:beanio:marshal?mapping=mapping.xml&streamName=request")
              .process(exchange -> {
                String body = exchange.getIn().getBody(String.class);
                log.info("body: {}", body);
              });
        }
      };
    }

    @Bean
    public EndpointRouteBuilder unmarshalRoute() {
      return new EndpointRouteBuilder() {
        @Override
        public void configure() {
//          from("direct:unmarshal")
          from(direct("unmarshal"))
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

    @Bean
    public RouteBuilder fixedLengthUnmarshalRoute() {
      return new RouteBuilder() {
        @Override
        public void configure() {
          from("direct:unmarshal2")
              .to("dataformat:beanio:unmarshal?mapping=mapping.xml&streamName=request")
              .process(exchange -> {
                FixedLengthDto dto = exchange.getIn().getBody(FixedLengthDto.class);
                log.info("dto: {}", dto);
              });
        }
      };
    }

//    @Bean
//    public EndpointRouteBuilder manualRoute() {
//      return new EndpointRouteBuilder() {
//        @Override
//        public void configure() {
//          from(direct("manualRoute"))
//              .log("${body}");
//        }
//      };
//    }

  }

}
