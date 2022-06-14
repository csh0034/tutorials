package com.ask.localstack;

import static org.assertj.core.api.Assertions.assertThat;

import cloud.localstack.Constants;
import cloud.localstack.Localstack;
import cloud.localstack.ServiceName;
import cloud.localstack.awssdkv1.TestUtils;
import cloud.localstack.docker.LocalstackDockerExtension;
import cloud.localstack.docker.annotation.LocalstackDockerProperties;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.model.CreateSecretRequest;
import com.amazonaws.services.secretsmanager.model.CreateSecretResult;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersByPathRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersByPathResult;
import com.amazonaws.services.simplesystemsmanagement.model.ParameterType;
import com.amazonaws.services.simplesystemsmanagement.model.PutParameterRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.QueueAttributeName;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LocalstackDockerExtension.class)
@LocalstackDockerProperties(services = {ServiceName.S3, ServiceName.SQS, ServiceName.SSM,
    ServiceName.SECRETSMANAGER}, portEdge = "5001")
@Slf4j
class LocalStackDockerExtensionTest {

  @Test
  void s3() throws IOException {
    AmazonS3 s3 = TestUtils.getClientS3();

    String bucketName = "test-s3";
    s3.createBucket(bucketName);
    log.info("createBucket, bucketName: {}", bucketName);

    String content = "Hello World";
    String key = "s3-key";
    s3.putObject(bucketName, key, content);
    log.info("putObject, bucketName: {}, key: {}, content: {}", bucketName, key, content);

    List<String> results = IOUtils.readLines(s3.getObject(bucketName, key).getObjectContent(), StandardCharsets.UTF_8);
    log.info("getObject, bucketName: {}, key: {}, results: {}", bucketName, key, results);

    assertThat(results).hasSize(1);
    assertThat(results.get(0)).isEqualTo(content);
  }

  @Nested
  class Sqs {

    @Test
    void standard() {
      AmazonSQS sqs = TestUtils.getClientSQS();

      CreateQueueResult queue = sqs.createQueue("standard-queue");
      log.info("queue urls: {}", sqs.listQueues().getQueueUrls());

      SendMessageResult sendMessageResult = sqs.sendMessage(queue.getQueueUrl(), "message...");
      log.info("send message, {}, {}", sendMessageResult.getMessageId(), sendMessageResult.getMD5OfMessageBody());

      ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(queue.getQueueUrl());
      log.info("receive message, {}", receiveMessageResult.getMessages());
    }

    @Test
    void fifo() {
      AmazonSQS sqs = TestUtils.getClientSQS();

      Map<String, String> attributes = new HashMap<>();
      attributes.put(QueueAttributeName.FifoQueue.toString(), Boolean.TRUE.toString());
      attributes.put(QueueAttributeName.ContentBasedDeduplication.toString(), Boolean.TRUE.toString());

      CreateQueueRequest request = new CreateQueueRequest()
          .withQueueName("fifo-queue.fifo")
          .withAttributes(attributes);

      CreateQueueResult queue = sqs.createQueue(request);
      log.info("queue urls: {}", sqs.listQueues().getQueueUrls());

      Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
      messageAttributes.put("AttributeOne", new MessageAttributeValue()
          .withStringValue("This is an attribute")
          .withDataType("String"));

      SendMessageRequest sendMessageRequest = new SendMessageRequest()
          .withQueueUrl(queue.getQueueUrl())
          .withMessageBody("message...")
          .withMessageGroupId("fifo-group-1")
          .withMessageAttributes(messageAttributes);

      SendMessageResult sendMessageResult = sqs.sendMessage(sendMessageRequest);
      log.info("send message, {}, {}", sendMessageResult.getMessageId(), sendMessageResult.getMD5OfMessageBody());

      ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(queue.getQueueUrl());
      log.info("receive message, {}", receiveMessageResult.getMessages());
    }

  }

  @Test
  void ssm() {
    AWSSimpleSystemsManagement ssm = AWSSimpleSystemsManagementClientBuilder.standard()
        .withEndpointConfiguration(new EndpointConfiguration(Localstack.INSTANCE.getEndpointSSM(), Constants.DEFAULT_REGION))
        .withCredentials(TestUtils.getCredentialsProvider())
        .build();

    PutParameterRequest request = new PutParameterRequest()
        .withName("/config/web_local/custom.username")
        .withValue("ASk")
        .withType(ParameterType.String);

    ssm.putParameter(request);

    GetParametersByPathRequest parametersByPathRequest = new GetParametersByPathRequest()
        .withPath("/config/web_local/")
        .withRecursive(true)
        .withWithDecryption(true);

    GetParametersByPathResult parametersByPath = ssm.getParametersByPath(parametersByPathRequest);
    log.info("get parameters, {}", parametersByPath);

    GetParameterRequest getParameterRequest = new GetParameterRequest().withName("/config/web_local/custom.username");
    GetParameterResult parameterResult = ssm.getParameter(getParameterRequest);
    log.info("get parameter, {}", parameterResult);
  }

  @Test
  void secretsManager() {
    AWSSecretsManager secretsManager = TestUtils.getClientSecretsManager();

    CreateSecretRequest createSecretRequest = new CreateSecretRequest()
        .withName("/secret/web_local/custom.username")
        .withSecretString("ASk");
    CreateSecretResult secretResult = secretsManager.createSecret(createSecretRequest);
    log.info("secretResult, {}", secretResult);

    GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
        .withSecretId("/secret/web_local/custom.username");

    GetSecretValueResult secretValue = secretsManager.getSecretValue(getSecretValueRequest);
    log.info("secretValue, {}", secretValue);
    log.info("secretValue.secretString, {}", secretValue.getSecretString());
  }

}
