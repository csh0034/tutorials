package com.ask.graphql.food;

import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class Food {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;

  private String name;

  public static Food create(String name) {
    Food food = new Food();
    food.name = name;
    return food;
  }

}
