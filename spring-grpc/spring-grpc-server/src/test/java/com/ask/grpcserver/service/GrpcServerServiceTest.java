package com.ask.grpcserver.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.fail;

import com.ask.grpc.HelloReply;
import com.ask.grpc.HelloRequest;
import io.grpc.internal.testing.StreamRecorder;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GrpcServerServiceTest {

  private GrpcServerService grpcServerService;

  @BeforeEach
  void setUp() {
    grpcServerService = new GrpcServerService();
  }

  @Test
  void sayHello() throws Exception {
    // given
    HelloRequest request = HelloRequest.newBuilder()
        .setName("ASk")
        .build();
    StreamRecorder<HelloReply> responseObserver = StreamRecorder.create();

    // when
    grpcServerService.sayHello(request, responseObserver);

    // then
    if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
      fail("The call did not terminate in time");
    }

    List<HelloReply> results = responseObserver.getValues();

    assertAll(
        () -> assertThat(responseObserver.getError()).isNull(),
        () -> assertThat(results).hasSize(1),
        () -> assertThat(results).containsExactly(HelloReply.newBuilder().setMessage("Hello: ASk").build())
    );
  }

}
