package com.ask.springdynamodb.dynamo;

import java.net.URI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoConfig {

  @Bean
  public DynamoDbEnhancedClient enhancedClient() {
    DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
        .credentialsProvider(ProfileCredentialsProvider.create())
        .endpointOverride(URI.create("http://localhost:8000"))
        .region(Region.AP_NORTHEAST_2)
        .build();

    return DynamoDbEnhancedClient.builder()
        .dynamoDbClient(dynamoDbClient)
        .build();
  }
}
