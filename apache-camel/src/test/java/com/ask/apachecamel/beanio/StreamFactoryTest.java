package com.ask.apachecamel.beanio;

import com.ask.apachecamel.Fixtures;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.beanio.BeanReader;
import org.beanio.BeanWriter;
import org.beanio.Marshaller;
import org.beanio.StreamFactory;
import org.beanio.Unmarshaller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class StreamFactoryTest {

  private StreamFactory streamFactory;

  @BeforeEach
  void setUp() {
    streamFactory = StreamFactory.newInstance();
    streamFactory.loadResource("mapping.xml");
  }

  @Test
  void marshal() {
    Marshaller marshaller = streamFactory.createMarshaller("employeeFile");
    String result = marshaller.marshal("employee", Fixtures.employee()).toString();
    log.info("result: {}", result);
  }

  @Test
  void unmarshal() {
    Unmarshaller unmarshaller = streamFactory.createUnmarshaller("employeeFile");

    List<String> stringEmployees = Arrays.asList(Fixtures.stringEmployees().split("\n"));

    stringEmployees.forEach(stringEmployee -> {
      Employee employee = (Employee) unmarshaller.unmarshal(stringEmployee);
      log.info("result: {}", employee);
    });
  }

  @Test
  void write() {
    StringWriter stringWriter = new StringWriter();
    BeanWriter beanWriter = streamFactory.createWriter("employeeFile", stringWriter);

    beanWriter.write("employee", Fixtures.employee());
    String result = stringWriter.toString();
    log.info("result: {}", result);
  }

  @Test
  void read() {
    StringReader stringReader = new StringReader(Fixtures.stringEmployees());
    BeanReader beanReader = streamFactory.createReader("employeeFile", stringReader);

    Employee employee;
    while ((employee = (Employee) beanReader.read()) != null) {
      log.info("result: {}", employee);
    }
  }

}
