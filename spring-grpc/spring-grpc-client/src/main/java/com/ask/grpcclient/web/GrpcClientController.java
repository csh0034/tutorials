package com.ask.grpcclient.web;

import com.ask.grpcclient.service.GrpcClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GrpcClientController {

  private final GrpcClientService grpcClientService;

  @GetMapping("/send")
  public String send(String name) {
    return grpcClientService.sendMessage(name);
  }

}
