package com.ask.contractstubrunner.support;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
    "mock-uri=http://localhost:${wiremock.server.port}"
})
@Import(WireMockTestConfig.class)
public @interface WireMockTest {

  String MOCK_URI_PLACEHOLDER = "${mock-uri}";

}
