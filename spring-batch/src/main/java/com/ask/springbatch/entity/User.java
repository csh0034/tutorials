package com.ask.springbatch.entity;

import static lombok.AccessLevel.PROTECTED;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tb_user")
@NoArgsConstructor(access = PROTECTED)
@Setter
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

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private UserExtra userExtra;

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

  public void disable() {
    enabled = false;
  }
}
