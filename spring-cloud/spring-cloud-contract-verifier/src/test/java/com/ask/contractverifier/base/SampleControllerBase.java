package com.ask.contractverifier.base;

import static org.mockito.BDDMockito.given;

import com.ask.contractverifier.service.SampleService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public abstract class SampleControllerBase {

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private SampleService sampleService;

  @BeforeEach
  void setup() {
    RestAssuredMockMvc.webAppContextSetup(context);

    given(sampleService.getName()).willReturn("name...");
  }

}
