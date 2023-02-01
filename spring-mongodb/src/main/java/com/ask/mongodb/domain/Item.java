package com.ask.mongodb.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("items")
@Getter
@ToString
public class Item {

  @Id
  private String id;

  private String name;
  private int quantity;
  private String category;

  public Item(String name, int quantity, String category) {
    this.name = name;
    this.quantity = quantity;
    this.category = category;
  }

}
