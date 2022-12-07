# gRPC web

## Generate Protobuf Messages and Client Service Stub

### Precondition
- protoc 설치
- protoc-gen-grpc-web plugin 추가

```shell
$ protoc -I=. helloworld.proto \
  --js_out=import_style=commonjs:. \
  --grpc-web_out=import_style=commonjs,mode=grpcwebtext:.
```

## Compile the Client JavaScript Code

```shell
$ npm install
$ npx webpack client.js
```

## Run the Example!

### 1. Run the Envoy proxy

```shell
$ docker run -d --name envoy -v "$(pwd)"/envoy.yaml:/etc/envoy/envoy.yaml:ro  \
    -p 8080:8080 -p 9901:9901 envoyproxy/envoy:v1.22.0
```

### 2. Run the simple Web Server

```shell
$ python3 -m http.server 8081
```

### 3. spring-grpc-server spring boot run

### 4. 브라우저로 접근후 개발자 도구 콘솔 확인 

http://localhost:8081


## 참조

- [gRPC-Web Hello World Guide](https://github.com/grpc/grpc-web/blob/master/net/grpc/gateway/examples/helloworld/README.md)
