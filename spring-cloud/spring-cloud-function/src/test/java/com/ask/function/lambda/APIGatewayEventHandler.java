package com.ask.function.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

public class APIGatewayEventHandler implements RequestHandler<APIGatewayProxyRequestEvent, String> {

  @Override
  public String handleRequest(APIGatewayProxyRequestEvent input, Context context) {
    LambdaLogger logger = context.getLogger();
    logger.log("invoke APIGatewayEventHandler::handleRequest");
    return input.getBody();
  }
}
