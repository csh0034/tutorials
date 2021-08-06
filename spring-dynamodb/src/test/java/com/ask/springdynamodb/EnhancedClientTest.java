package com.ask.springdynamodb;

import static org.assertj.core.api.Assertions.assertThat;

import com.ask.springdynamodb.dynamo.bean.Customer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EnhancedClientTest {

  private static final String TEST_USER_ID = "test_id";
  private static final String TEST_USER_ID_2 = "test_id_2";

  @Autowired
  private DynamoDbEnhancedClient enhancedClient;

  //@Disabled
  @Test
  @Order(1)
  void createTable() {

    // GIVEN
    DynamoDbTable<Customer> customerTable = enhancedClient.table(Customer.TABLE_NAME, TableSchema.fromBean(Customer.class));

    // WHEN
    customerTable.createTable();

    // THEN
    // 존재여부 체크 필요
  }

  @Test
  @Order(2)
  void putItem() {

    // GIVEN
    DynamoDbTable<Customer> customerTable = enhancedClient.table(Customer.TABLE_NAME, TableSchema.fromBean(Customer.class));
    Customer newCustomer1 = Customer.create(TEST_USER_ID, "ask", "test@gmail.com");
    Customer newCustomer2 = Customer.create(TEST_USER_ID_2, "ask2", "test2@gmail.com");

    // WHEN
    customerTable.putItem(newCustomer1);
    customerTable.putItem(newCustomer2);

    // THEN
    Customer customer = customerTable.getItem(Key.builder().partitionValue(TEST_USER_ID).build());

    assertThat(customer).isNotNull();
    log.info("customer : {}", customer);
  }

  @Test
  @Order(3)
  void getItem() {

    // GIVEN
    DynamoDbTable<Customer> customerTable = enhancedClient.table(Customer.TABLE_NAME, TableSchema.fromBean(Customer.class));

    // WHEN
    Customer customer = customerTable.getItem(Key.builder().partitionValue(TEST_USER_ID).build());

    // THEN
    assertThat(customer).isNotNull();
    log.info("customer : {}", customer);
  }

  @Test
  @Order(4)
  void getNotExistsItem() {

    // GIVEN
    DynamoDbTable<Customer> customerTable = enhancedClient.table(Customer.TABLE_NAME, TableSchema.fromBean(Customer.class));
    String notExistsId = "notExistsId";

    // WHEN
    Customer customer = customerTable.getItem(Key.builder().partitionValue(notExistsId).build());

    // THEN
    assertThat(customer).isNull();
  }

  @Test
  @Order(5)
  void scan() {

    // GIVEN
    DynamoDbTable<Customer> customerTable = enhancedClient.table(Customer.TABLE_NAME, TableSchema.fromBean(Customer.class));

    // WHEN
    SdkIterable<Customer> items = customerTable.scan().items();

    // THEN
    items.forEach(customer -> log.info("customer : {}", customer));
  }
}