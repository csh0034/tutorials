package com.ask.springdynamodb.dynamo.bean;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@Getter
@Setter
@ToString
public class Customer {

  public static String TABLE_NAME = "tb_customer";

  private String id;
  private String name;
  private String email;
  private LocalDateTime createdDt;

  @DynamoDbPartitionKey
  public String getId() {
    return this.id;
  }

  //@DynamoDbSortKey
  public String getName() {
    return this.name;
  }

  public static Customer create(String id, String name, String email) {
    Customer customer = new Customer();
    customer.id = id;
    customer.name = name;
    customer.email = email;
    customer.createdDt = LocalDateTime.now();
    return customer;
  }
}
