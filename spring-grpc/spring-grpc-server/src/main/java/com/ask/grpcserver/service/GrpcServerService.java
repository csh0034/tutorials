package com.ask.grpcserver.service;

import com.ask.grpc.GreeterGrpc;
import com.ask.grpc.HelloReply;
import com.ask.grpc.HelloRequest;
import com.ask.grpc.RepeatHelloRequest;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@Slf4j
public class GrpcServerService extends GreeterGrpc.GreeterImplBase {

  @Override
  public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
    log.info("[request] name: {}", request.getName());

    HelloReply reply = HelloReply.newBuilder()
        .setMessage("Hello: " + request.getName())
        .build();
    responseObserver.onNext(reply);
    responseObserver.onCompleted();
  }

  @Override
  public void sayRepeatHello(RepeatHelloRequest request, StreamObserver<HelloReply> responseObserver) {
    log.info("[request] name: {}, count: {}", request.getName(), request.getCount());

    for (int i = 1; i < request.getCount() + 1; i++) {
      HelloReply reply = HelloReply.newBuilder()
          .setMessage(String.format("[%d/%d] Hello: %s", i, request.getCount(), request.getName()))
          .build();

      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      responseObserver.onNext(reply);
    }

    responseObserver.onCompleted();
  }

}
