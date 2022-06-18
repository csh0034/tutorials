package com.ask.awsmessagingsqs.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.QueueAttributeName;
import io.awspring.cloud.autoconfigure.messaging.SqsProperties;
import io.awspring.cloud.core.region.RegionProvider;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SqsConfig {

  private final AWSCredentialsProvider awsCredentialsProvider;
  private final RegionProvider regionProvider;
  private final SqsProperties sqsProperties;

  /**
   * 레퍼런스에 FIFO queue 사용시 AmazonSQSAsync 사용해야 한다고함.
   * localstack 에선 기존 bean 사용해도 동작함
   */
//  @Bean
//  public AmazonSQSAsync amazonSQS() {
//    return AmazonSQSAsyncClientBuilder.standard()
//        .withCredentials(awsCredentialsProvider)
//        .withEndpointConfiguration(
//            new EndpointConfiguration(sqsProperties.getEndpoint().toString(), regionProvider.getRegion().getName()))
//        .build();
//  }

  @Bean
  public ApplicationRunner applicationRunner(AmazonSQS sqs) {
    return args -> {
      // standard queue 생성
      sqs.createQueue(SqsConstants.STANDARD_QUEUE);

      // fifo queue 생성
      Map<String, String> attributes = new HashMap<>();
      attributes.put(QueueAttributeName.FifoQueue.toString(), Boolean.TRUE.toString());
      attributes.put(QueueAttributeName.ContentBasedDeduplication.toString(), Boolean.TRUE.toString());

      CreateQueueRequest request = new CreateQueueRequest()
          .withQueueName(SqsConstants.FIFO_QUEUE)
          .withAttributes(attributes);

      sqs.createQueue(request);
    };
  }

}
