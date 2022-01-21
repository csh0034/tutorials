# spring - springdoc

## Springdoc 의 Swagger-UI SearchBar url 변경 처리
springdoc 은 webjar 의 swagger-ui 를 사용하는데 Search Bar 의 데이터를 어떻게 바꿀수 있을까?

### ResourceTransformer
리소스 핸들러에 등록된 url 을 처리할때 HttpRequestHandlerAdapter 를 통해서  
ResourceHttpRequestHandler 가 사용된다.

이때 리소스를 가져와서 커스텀 할 수 있는 기능을 제공하는데 이를 처리하는 인터페이스가 ResourceTransformer 이다.

### SwaggerIndexPageTransformer
springdoc 에서는 SwaggerIndexPageTransformer 를 등록하는데  
swagger-ui 의 index 파일 요청일 경우 이를 webjars 에서 찾아온뒤
자바스크립트 호출 부분을 수정한다.

swagger-ui 스크립트 의 SwaggerUIBundle 객체 생성 부분에  
"configUrl" : "호출하려는 url" 부분을 추가하면 이를 읽어 화면을 렌더링한다.

#### before
![01.png](images/01.png)

#### after
![02.png](images/02.png)

#### SwaggerIndexPageTransformer 의 실제 replace 하는 method
![03.png](images/03.png)

## 참조
- [swagger-ui, Configuration](https://swagger.io/docs/open-source-tools/swagger-ui/usage/configuration/)
