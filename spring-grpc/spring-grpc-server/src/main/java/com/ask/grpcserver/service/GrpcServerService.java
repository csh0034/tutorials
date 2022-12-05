package com.ask.grpcserver.service;

import com.ask.grpc.HelloReply;
import com.ask.grpc.HelloRequest;
import com.ask.grpc.SimpleGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@Slf4j
public class GrpcServerService extends SimpleGrpc.SimpleImplBase {

  @Override
  public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
    log.info("request, name: {}", request.getName());

    HelloReply reply = HelloReply.newBuilder()
        .setMessage("Hello: " + request.getName())
        .build();
    responseObserver.onNext(reply);
    responseObserver.onCompleted();
  }

}
