package com.ask.restassured.web;

import static org.hamcrest.Matchers.equalTo;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SampleSecondController.class)
class SampleSecondControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mockMvc);
  }

  @Test
  void today() {
    RestAssuredMockMvc.given()
        .accept(ContentType.TEXT)
    .when()
        .get("/sample-second/today")
    .then()
        .log().all()
        .contentType(ContentType.TEXT)
        .statusCode(HttpStatus.OK.value())
        .assertThat().body(equalTo(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
  }

}
