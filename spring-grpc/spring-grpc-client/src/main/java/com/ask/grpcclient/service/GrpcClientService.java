package com.ask.grpcclient.service;

import com.ask.grpc.GreeterGrpc;
import com.ask.grpc.HelloReply;
import com.ask.grpc.HelloRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class GrpcClientService {

  @GrpcClient("test")
  private GreeterGrpc.GreeterBlockingStub greeterStub;

  public String sendMessage(final String name) {
    HelloRequest request = HelloRequest.newBuilder()
        .setName(name)
        .build();

    HelloReply response = greeterStub.sayHello(request);
    return response.getMessage();
  }

}
