package com.ask.function.lambda;

import static org.assertj.core.api.Assertions.assertThat;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.tests.annotations.Event;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;

@Slf4j
class LambdaEventTest {

  @DisplayName("APIGatewayProxyRequestEvent 검증")
  @ParameterizedTest
  @Event(value = "apigw_rest_event.json", type = APIGatewayProxyRequestEvent.class)
  void apiGateway(APIGatewayProxyRequestEvent event) {
    // given
    APIGatewayEventHandler handler = new APIGatewayEventHandler();
    TestContext context = new TestContext();

    // when
    String result = handler.handleRequest(event, context);

    // then
    assertThat(result).isEqualTo("Hello from Lambda!");
  }
}
