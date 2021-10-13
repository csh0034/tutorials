package com.ask.springjparedisson.entity;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Cacheable;
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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tb_user")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "user_id")
  private String id;

  private String name;

  private String password;

  @JsonIgnore
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
