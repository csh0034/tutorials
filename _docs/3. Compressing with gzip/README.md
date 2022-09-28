# Compressing with gzip

## SpringBoot

```yaml
server:
  compression:
    enabled: true
    mime-types: text/html,text/plain,text/css,application/javascript,application/json
    min-response-size: 2048 # default
```

- [docs,  Enable HTTP Response Compression](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.webserver.enable-response-compression)

## HAProxy

```text
backend bk_web
 compression algo gzip
 compression type text/html text/plain text/css application/javascript application/json
```

- [docs, Compression](https://www.haproxy.com/documentation/hapee/latest/load-balancing/compression/)
- [blog, haproxy-and-gzip-compression](https://www.haproxy.com/blog/haproxy-and-gzip-compression/)

## 참조

- [Baeldung, Reducing JSON Data Size](https://www.baeldung.com/json-reduce-data-size)
