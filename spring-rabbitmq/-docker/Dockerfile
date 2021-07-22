FROM rabbitmq:management
ADD ./plugin/rabbitmq_delayed_message_exchange-3.8.0.ez $RABBITMQ_HOME/plugins/rabbitmq_delayed_message_exchange-3.8.0.ez
RUN rabbitmq-plugins enable --offline rabbitmq_management
RUN rabbitmq-plugins enable --offline rabbitmq_mqtt
RUN rabbitmq-plugins enable --offline rabbitmq_web_mqtt
RUN rabbitmq-plugins enable --offline rabbitmq_delayed_message_exchange