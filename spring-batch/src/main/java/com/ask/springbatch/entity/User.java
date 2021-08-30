package com.ask.springbatch.entity;

import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tb_user")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class User {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "user_id")
  private String id;

  private String name;

  private String password;

  private boolean enabled;

  public static User create(String name, String password, boolean enabled) {
    User user = new User();
    user.name = name;
    user.password = password;
    user.enabled = enabled;
    return user;
  }

  public static User create(String id, String name) {
    User user = new User();
    user.id = id;
    user.name = name;
    user.password = name;
    user.enabled = true;
    return user;
  }

  public void updateId(String id) {
    this.id = id;
  }

  public void disable() {
    enabled = false;
  }
}
