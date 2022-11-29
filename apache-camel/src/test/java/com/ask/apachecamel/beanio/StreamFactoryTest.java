package com.ask.apachecamel.beanio;

import com.ask.apachecamel.Fixtures;
import com.ask.apachecamel.beanio.User.Address;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.beanio.BeanReader;
import org.beanio.BeanWriter;
import org.beanio.Marshaller;
import org.beanio.StreamFactory;
import org.beanio.Unmarshaller;
import org.beanio.builder.StreamBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@Slf4j
class StreamFactoryTest {

  private StreamFactory streamFactory;

  @BeforeEach
  void setUp() {
    streamFactory = StreamFactory.newInstance();
    streamFactory.loadResource("mapping.xml");
  }

  @DisplayName("csv marshal")
  @Test
  void marshal() {
    Marshaller marshaller = streamFactory.createMarshaller("employeeFile");
    String result = marshaller.marshal("employee", Fixtures.employee()).toString();
    log.info("result: {}", result);
  }

  @DisplayName("fixed length marshal")
  @Test
  void marshal2() {
    Marshaller marshaller = streamFactory.createMarshaller("request");
    String result = marshaller.marshal("sms", Fixtures.fixedLengthDto()).toString();
    log.info("result: {}", result);
  }

  @DisplayName("csv unmarshal")
  @Test
  void unmarshal() {
    Unmarshaller unmarshaller = streamFactory.createUnmarshaller("employeeFile");

    List<String> stringEmployees = Arrays.asList(Fixtures.stringEmployees().split("\n"));

    stringEmployees.forEach(stringEmployee -> {
      Employee employee = (Employee) unmarshaller.unmarshal(stringEmployee);
      log.info("result: {}", employee);
    });
  }

  @DisplayName("fixed length unmarshal")
  @Test
  void unmarshal2() {
    Unmarshaller unmarshaller = streamFactory.createUnmarshaller("request");

    FixedLengthDto dto = (FixedLengthDto) unmarshaller.unmarshal(Fixtures.stringFixedLengthDto());
    log.info("result: {}", dto);
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

  @Nested
  class AnnotationTest {

    private static final String STREAM_NAME = "user";

    @BeforeEach
    void setUp() {
      StreamBuilder builder = new StreamBuilder(STREAM_NAME)
          .format("fixedlength")
          .strict()
          .addRecord(User.class);

      streamFactory.define(builder);
    }

    @Test
    void marshal() {
      // given
      User user = new User();
      user.setName("ASk");
      user.setAge(99);
      user.setJoinedDt(new Date());

      Address address = new Address();
      address.setStreet("street...");
      address.setZipcode("zipCode...");
      user.setAddress(address);

      // when then
      Marshaller marshaller = streamFactory.createMarshaller(STREAM_NAME);
      String result = marshaller.marshal(user).toString();
      log.info("result: {}", result);
    }

    @Test
    void unmarshal() {
      // given
      String record = "ASk-------99-2022-11-29 16:09:15@@@@@seoulstreet... zipCode...@@@@@@@@@@";

      // when
      Unmarshaller unmarshaller = streamFactory.createUnmarshaller(STREAM_NAME);

      // then
      User user = (User) unmarshaller.unmarshal(record);
      log.info("result: {}", user);
    }

  }

}
