package com.ask.moquette;

import static io.netty.util.CharsetUtil.UTF_8;

import io.moquette.broker.Server;
import io.moquette.broker.config.ClasspathResourceLoader;
import io.moquette.broker.config.IConfig;
import io.moquette.broker.config.IResourceLoader;
import io.moquette.broker.config.ResourceLoaderConfig;
import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.InterceptHandler;
import io.moquette.interception.messages.InterceptPublishMessage;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.MqttMessageBuilders;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public final class EmbeddedLauncher {

  static class PublisherListener extends AbstractInterceptHandler {

    @Override
    public String getID() {
      return "EmbeddedLauncherPublishListener";
    }

    @Override
    public void onPublish(InterceptPublishMessage msg) {
      final String decodedPayload = msg.getPayload().toString(UTF_8);
      System.out.println("Received on topic: " + msg.getTopicName() + " content: " + decodedPayload);
    }

  }

  public static void main(String[] args) throws IOException, InterruptedException {
    IResourceLoader classpathLoader = new ClasspathResourceLoader();
    final IConfig classPathConfig = new ResourceLoaderConfig(classpathLoader);

    final Server mqttBroker = new Server();
    List<? extends InterceptHandler> userHandlers = Collections.singletonList(new PublisherListener());
    mqttBroker.startServer(classPathConfig, userHandlers);

    System.out.println("Broker started press [CTRL+C] to stop");
    //Bind  a shutdown hook
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("Stopping broker");
      mqttBroker.stopServer();
      System.out.println("Broker stopped");
    }));

    Thread.sleep(5000);

    System.out.println("Before self publish");
    MqttPublishMessage message = MqttMessageBuilders.publish()
        .topicName("/exit")
        .retained(true)
        .qos(MqttQoS.EXACTLY_ONCE)
        .payload(Unpooled.copiedBuffer("Hello World!!".getBytes(UTF_8)))
        .build();

    mqttBroker.internalPublish(message, "INTRLPUB");
    System.out.println("After self publish");
  }

  private EmbeddedLauncher() {
  }

}
