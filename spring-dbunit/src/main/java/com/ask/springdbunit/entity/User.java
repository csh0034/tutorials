package com.ask.springdbunit.entity;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;
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

  @Exclude
  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "company_id")
  private Company company;

  public static User create(String name, String password, Company company) {
    User user = new User();
    user.name = name;
    user.password = password;
    user.company = company;
    company.getUsers().add(user);
    return user;
  }
}
