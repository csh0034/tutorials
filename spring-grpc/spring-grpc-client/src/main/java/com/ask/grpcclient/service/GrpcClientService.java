package com.ask.grpcclient.service;

import com.ask.grpc.HelloReply;
import com.ask.grpc.HelloRequest;
import com.ask.grpc.SimpleGrpc.SimpleBlockingStub;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class GrpcClientService {

  @GrpcClient("test")
  private SimpleBlockingStub simpleStub;

  public String sendMessage(final String name) {
    HelloRequest request = HelloRequest.newBuilder()
        .setName(name)
        .build();

    HelloReply response = simpleStub.sayHello(request);
    return response.getMessage();
  }

}
