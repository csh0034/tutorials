# Spring Websocket

## 특정유저에게 메세지전송

`UserDestinationMessageHandler` 에서 처리

1. /user 로 시작하는 destination 구독
   - ex) /user/queue/message 구독 
2. /queue/message-user{sessionId} 로 변경되어 구독처리됨
3. /user/{username}/queue/message 로 전송하면 2번으로 전달됨

# 참조

- [Spring Docs, Websocket](https://docs.spring.io/spring-framework/docs/5.3.x/reference/html/web.html#websocket-stomp)
- [Spring Security Docs, Websocket](https://docs.spring.io/spring-security/reference/5.7/servlet/integrations/websocket.html)
- [Spring WebSockets: Send Messages to a Specific User](https://www.baeldung.com/spring-websockets-send-message-to-user)
