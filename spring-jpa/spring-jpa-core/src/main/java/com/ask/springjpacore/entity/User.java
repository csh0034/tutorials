package com.ask.springjpacore.entity;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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

  @NotNull
  private String name;

  private String password;

  private LocalDateTime passwordUpdateDt;

  public static User create(String name, String password) {
    User user = new User();
    user.name = name;
    user.password = password;
    return user;
  }

  public void updatePassword(String password) {
    this.password = password;
    passwordUpdateDt = LocalDateTime.now();
  }

  @PrePersist
  public void prePersist() {
    passwordUpdateDt = LocalDateTime.now();
  }

}
