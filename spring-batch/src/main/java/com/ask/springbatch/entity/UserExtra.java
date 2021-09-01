package com.ask.springbatch.entity;

import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tb_user_extra")
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
@ToString
public class UserExtra {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "id")
  private String id;

  @Exclude
  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  public static UserExtra create(User user) {
    UserExtra userExtra = new UserExtra();
    userExtra.setUser(user);
    user.setUserExtra(userExtra);
    return userExtra;
  }
}
