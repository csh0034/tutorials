# OAuth 2.0

## OAuth 2.0 이란?

- OAuth 2.0(Open Authorization 2.0, OAuth2)은 인증을 위한 개방형 표준 프로토콜이다.  
- 이 프로토콜에서는 Third-Party 프로그램에게 리소스 소유자를 대신하여 리소스 서버에서 제공하는  
  자원에 대한 접근 권한을 위임하는 방식을 제공한다.

> 구글, 페이스북, 카카오, 네이버 등에서 제공하는 간편 로그인 기능도 OAuth2 프로토콜 기반의 사용자 인증 기능을 제공하고 있다.

## OAuth 2.0 주요 용어

| 용어             | 설명                                                                                                  |
|----------------|-----------------------------------------------------------------------------------------------------|
| Authentication | 인증, 접근 자격이 있는지 검증하는 단계이다.                                                                           |
| Authorization  | 인가, 자원에 접근할 권한을 부여하는것이다.<br/> 인가가 완료되면 리소스 접근 권한이 담긴 Access Token 이 클라이언트에게 부여된다.                   |
| Access Token   | 리소스 서버에게서 리소스 소유자의 보호된 자원을 획득할 때 사용되는 만료 기간이 있는 Token 이다.                                           |
| Refresh Token  | Access Token 만료시 이를 갱신하기 위한 용도로 사용하는 Token 이다.<br/>Refresh Token 은 일반적으로 Access Token 보다 만료 기간이 길다. |

## OAuth 2.0의 4가지 역할

| 역할                        | 설명                                                                                                                                                                                                                                          |
|---------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Resource <br/>Owner       | 리소스 소유자 또는 사용자, 보호된 자원에 접근할 수 있는 자격을 부여해 주는 주체. <br/> OAuth2 프로토콜 흐름에서 클라이언트를 인증하는 역할을 수행한다. <br/>인증이 완료되면 권한 획득 자격(Authorization Grant)을 클라이언트에게 부여한다. <br/>개념적으로는 리소스 소유자가 자격을 부여하는 것이지만 일반적으로 권한 서버가 리소스 소유자와 클라이언트 사이에서 중개 역할을 수행하게 된다. |
| Client                    | 보호된 자원을 사용하려고 접근 요청을 하는 애플리케이션                                                                                                                                                                                                              |
| Resource <br/>Server      | 사용자의 보호된 자원을 호스팅하는 서버                                                                                                                                                                                                                       |
| Authorization <br/>Server | 권한 서버, 인증/인가를 수행하는 서버로 클라이언트의 접근 자격을 확인하고<br/> Access Token 을 발급하여 권한을 부여하는 역할을 수행                                                                                                                                                          |


## Obtaining Authorization

OAuth2 프로토콜에서는 다양한 클라이언트 환경에 적합하도록 권한 부여 방식에 따른 프로토콜을 4가지 종류로 구분하여 제공하고 있다.

### 1. Authorization Code Grant │ 권한 부여 승인 코드 방식


권한 부여 승인을 위해 자체 생성한 **Authorization Code** 를 전달하는 방식으로 많이 쓰이고 **기본이 되는 방식**이다.   
간편 로그인 기능에서 사용되는 방식으로 클라이언트가 사용자를 대신하여 특정 자원에 접근을 요청할 때 사용되는 방식이다.  
보통 타사의 클라이언트에게 보호된 자원을 제공하기 위한 인증에 사용됩니다. **Refresh Token 의 사용이 가능한 방식**이다.

![01.png](01.png)

권한 부여 승인 요청 시 response_type 을 **code** 로 지정하여 요청한다.   
이후 클라이언트는 권한 서버에서 제공하는 로그인 페이지로 이동시킨다.  
이 페이지를 통해 사용자가 로그인을 하면 권한 서버는 권한 부여 승인 코드 요청 시 전달받은 redirect_url로 Authorization Code를 전달한다.   
Authorization Code 는 권한 서버에서 제공하는 API 를 통해 Access Token 으로 교환한다.

> User 가 사용요청을 할 경우 Client 는 현재 페이지를 이동(Redirect)하여 로그인 처리를 한다.

### 2. Implicit Grant │ 암묵적 승인 방식

자격증명을 안전하게 저장하기 힘든 클라이언트(ex: JavaScript 등의 스크립트 언어를 사용한 브라우저)에게 최적화된 방식이다.

암시적 승인 방식에서는 권한 부여 승인 코드 없이 바로 Access Token 이 발급 되며 Token 이 바로 전달되므로   
만료기간을 짧게 설정하여 누출의 위험을 줄일 필요가 있다.

**Refresh Token 사용이 불가능한 방식**이며, 이 방식에서 권한 서버는 client_secret 을 사용해 클라이언트를 인증하지 않는다.   
Access Token 을 획득하기 위한 절차가 간소화되기에 응답성과 효율성은 높아지지만 Access Token 이 URL 로 전달된다는 단점이 있다.

![02.png](02.png)

권한 부여 승인 요청 시 response_type 을 **token** 으로 설정하여 요청한다.  
이후 클라이언트는 권한 서버에서 제공하는 로그인 페이지를 팝업으로 띄워 출력한다.  
로그인이 완료되면 권한 서버는 Authorization Code 가 아닌 Access Token 을 redirect_url 로 바로 전달한다.

> User 가 사용 요청을 할 경우 Client 는 현재 페이지가 아닌 로그인 팝업을 통하여 로그인 처리를 한다.

### 3. Resource Owner Password Credentials Grant │ 자원 소유자 자격증명 승인 방식

간단하게 username, password 로 Access Token 을 받는 방식이다.

클라이언트가 타사의 외부 프로그램일 경우에는 이 방식을 적용하면 안된다.   
자신의 서비스에서 제공하는 어플리케이션일 경우에만 사용되는 인증 방식이다. Refresh Token 의 사용도 가능합니다.

![03.png](03.png)

제공하는 API 를 통해 username, password 를 전달하여 Access Token 을 받는다.  
이 방식은 권한 서버, 리소스 서버, 클라이언트가 모두 같은 시스템에 속해 있을 때 사용되어야 하는 방식이다.

### 4. Client Credentials Grant │ 클라이언트 자격증명 승인 방식

클라이언트의 자격증명만으로 Access Token 을 획득하는 방식이다.

OAuth2의 권한 부여 방식 중 가장 간단한 방식으로 클라이언트 자신이 관리하는 리소스 혹은 권한 서버에 해당 클라이언트를  
위한 제한된 리소스 접근 권한이 설정되어 있는 경우 사용된다.

이 방식은 자격증명을 안전하게 보관할 수 있는 클라이언트에서만 사용되어야 하며, Refresh Token 은 사용할 수 없다.

![04.png](04.png)

## Request and Response Examples

4가지 권한 부여 방식에 대한 Request & Response 예제

| Parameter                | 설명                                                                                                                                                                                                                   |
|--------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| client_id, client_secret | 클라이언트 자격증명. 클라이언트가 권한 서버에 등록하면 발급받을 수 있으며 권한 서버 연동 시 클라이언트의 검증에 사용                                                                                                                                                   |
| redirect_url             | 권한 서버가 요청에 대한 응답을 보낼 url 설정                                                                                                                                                                                          |
| response_type            | 권한 부여 동의 요청 시 포함되는 값으로 권한 부여 방식에 대한 설정 <br/>**code**: Authorization Code Grant  <br/>**token**: Implicit Grant                                                                                                       |
| state                    | CSRF 공격에 대비하기 위해 클라이언트가 권한서버에 요청 시 포함하는 임의의 문자열. <br/>필수 사항은 아니지만 클라이언트가 요청 시 state를 포함 시켰다면 권한 서버는 동일한 값을 클라이언트에게 보내야 한다.                                                                                           |
| grant_type               | Access Token 획득 요청 시 포함되는 값으로 권한 부여 방식에 대한 설정 <br/>**authorization_code**: Authorization Code Grant<br/>**password**: Resource Owner Password Credentials Grant<br/>**client_credentials**: Client Credentials Grant |
| code                     | Authorization Code Grant 방식에서 Access Token 요청 시 사용<br/>권한 서버에서 획득한 Authorization Code 를 입력한다.                                                                                                                        |
| token_type               | 발행된 Token 의 타입. 대표적으로 Bearer, MAC(Message Authentication Code)이 있다.                                                                                                                                                  |
| expires_in               | 토큰 만료 시간(단위: 초)                                                                                                                                                                                                      |
| example_parameter        | Token 타입에 따른 추가 파라미터                                                                                                                                                                                                 |


추가로 API 요청에 포함되는 Authorization Basic 헤더는 Client 자격증명 관련 데이터로 client_id 와  
client_secret 값을 아래와 같이 Base64 인코딩하여 생성합니다.

> base64(client_id:client_secret)

### 1. Authorization Code Grant │ 권한 부여 승인 코드 방식

#### Step 1: Authorization

```text
# Request
GET /authorize?response_type=code&client_id=s6BhdRkqt3&state=xyz&redirect_uri=https%3A%2F%2Fclient%2Eexample%2Ecom%2Fc

# Response
https://client.example.com/cb?code=SplxlOBeZQQYbYS6WxSbIA&state=xyz
```

#### Step 2: Access Token

```text
# Request
POST /token
Authorization: Basic czZCaGRSa3F0MzpnWDFmQmF0M2JW
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code&code=SplxlOBeZQQYbYS6WxSbIA&redirect_uri=https%3A%2F%2Fclient%2Eexample%2Ecom%2Fcb

# Response
{
  "access_token": "2YotnFZFEjr1zCsicMWpAA",
  "token_type": "example",
  "expires_in": 3600,
  "refresh_token": "tGzv3JOkF0XG5Qx2TlKWIA",
  "example_parameter": "example_value"
}

# 특이사항
Authorization Code 획득 후 해당 Code 로 Access Token 획득
```

### 2. Implicit Grant │ 암묵적 승인 방식

```text
# Request
GET /authorize?response_type=token&client_id=s6BhdRkqt3&state=xyz&redirect_uri=https%3A%2F%2Fclient%2Eexample%2Ecom%2Fcb

# Response
https://example.com/cb#access_token=2YotnFZFEjr1zCsicMWpAA&state=xyz&token_type=example&expires_in=3600

# 특이사항
Authorize 요청 시 url 로 Access Token 이 바로 전달됨
```

### 3. Resource Owner Password Credentials Grant │ 자원 소유자 자격증명 승인 방식

```text
# Request
POST /token
Authorization: Basic czZCaGRSa3F0MzpnWDFmQmF0M2JW
Content-Type: application/x-www-form-urlencoded

grant_type=password&username=johndoe&password=A3ddj3w

# Response
{
  "access_token": "2YotnFZFEjr1zCsicMWpAA",
  "token_type": "example",
  "expires_in": 3600,
  "refresh_token": "tGzv3JOkF0XG5Qx2TlKWIA",
  "example_parameter": "example_value"
}

# 특이사항
Username, Password 로 Access Token 획득
```

### 4. Client Credentials Grant │ 클라이언트  자격증명 승인 방식

```text
# Request
POST /token
Authorization: Basic czZCaGRSa3F0MzpnWDFmQmF0M2JW
Content-Type: application/x-www-form-urlencoded

grant_type=client_credentials

# Response
{
  "access_token": "2YotnFZFEjr1zCsicMWpAA",
  "token_type": "example",
  "expires_in": 3600,
  "example_parameter": "example_value"
}

# 특이사항
클라이언트 자격증명만으로 Access Token 획득
```

## 참조

- [OAuth 2.0 동작 방식의 이해](https://blog.naver.com/mds_datasecurity/222182943542)
