# Simple HTTP Endpoint Example

## Invoke the function locally

```shell
$ sls invoke local -f currentTime -p sample-data.json
```

## 정리

API Gateway lets you deploy HTTP APIs. It comes in two versions:

- v1, also called REST API
- v2, also called HTTP API, which is faster and cheaper than v1

serverless.yml 에서 events 에 http 일 경우 REST API, httpApi 일 경우 HTTP API 이다.

```yaml
functions:
  currentTime:
    handler: handler.endpoint
    events:
      - http: # REST API           
          path: /time
          method: get
      - httpApi: # HTTP API
          path: /time
          method: get
```

## 참조
- [Serverless, API Gateway HTTP API](https://www.serverless.com/framework/docs/providers/aws/events/http-api)
