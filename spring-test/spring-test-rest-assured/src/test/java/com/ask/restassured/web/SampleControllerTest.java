package com.ask.restassured.web;

import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SampleControllerTest {

  @LocalServerPort
  public int port;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
  }

  @Test
  void today() {
    RestAssured.given()
        .accept(ContentType.TEXT)
    .when()
        .get("/sample/today")
    .then()
        .log().all()
        .contentType(ContentType.TEXT)
        .statusCode(HttpStatus.OK.value())
        .assertThat().body(equalTo(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
  }

}
