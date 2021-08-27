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
@Table(name = "tb_user_not_auto")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class UserNotAuto {

  @Id
  @Column(name = "user_id")
  private String id;

  private String name;

  private String password;

  private boolean enabled;

  public static UserNotAuto create(String id, String name, String password, boolean enabled) {
    UserNotAuto user = new UserNotAuto();
    user.id = id;
    user.name = name;
    user.password = password;
    user.enabled = enabled;
    return user;
  }
}
