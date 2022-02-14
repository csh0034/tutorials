package com.ask.springr2dbc.model;

import java.util.UUID;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Table("mt_user")
@Data
public class User implements Persistable<String> {

  @Id
  private String id;
  private String name;
  private Integer age;

  public static User create(String name, Integer age) {
    User user = new User();
    user.name = name;
    user.age = age;
    return user;
  }

  public void update(User other) {
    this.name = other.name;
    this.age = other.age;
  }

  @Override
  public boolean isNew() {
    if (id == null) {
      id = UUID.randomUUID().toString();
      return true;
    }
    return false;
  }

}
