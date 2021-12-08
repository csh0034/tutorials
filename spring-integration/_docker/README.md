## docker build
docker build -t rabbitmq .

## docker rabbitmq start
docker run -d --name rabbitmq \
-v {호스트 저장 위치}:/var/lib/rabbitmq \
-v {호스트 저장 위치}:/var/log/rabbitmq \
-p 15672:15672 -p 5672:5672 -p 1883:1883 -p 15675:15675 \
--restart=always \
rabbitmq

## check plugin  
$ docker exec -it rabbitmq /bin/bash

## rabbitmq-plugins list
필요한 플러그인이 enabled 안되어 있을 경우, 직접 enable 하자
- rabbitmq-plugins enable --offline rabbitmq_management
- rabbitmq-plugins enable --offline rabbitmq_mqtt
- rabbitmq-plugins enable --offline rabbitmq_web_mqtt
- rabbitmq-plugins enable --offline rabbitmq_delayed_message_exchange

## docker hub
- [docker pull csh0034/rabbitmq](https://hub.docker.com/repository/docker/csh0034/rabbitmq)
- management : [http://localhost:15672](http://localhost:15672)
- default account : guest / guest