docker build -t rabbitmq .
docker run -d --name rabbitmq -p 15672:15672 -p 5672:5672 -p 1883:1883 -p 15675:15675 rabbitmq

check plugin
$ docker exec -it rabbitmq /bin/bash
# rabbitmq-plugins list

필요한 플러그인이 enabled 안되어 있을 경우, 직접 enable 하자
rabbitmq-plugins enable --offline rabbitmq_management
rabbitmq-plugins enable --offline rabbitmq_mqtt
rabbitmq-plugins enable --offline rabbitmq_web_mqtt
rabbitmq-plugins enable --offline rabbitmq_delayed_message_exchange