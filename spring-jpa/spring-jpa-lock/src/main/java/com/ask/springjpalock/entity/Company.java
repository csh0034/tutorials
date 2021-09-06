package com.ask.springjpalock.entity;

import static lombok.AccessLevel.PROTECTED;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tb_company")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class Company {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "company_id")
  private String id;

  private String name;

  private long count;

  @Exclude
  @OneToMany(mappedBy = "company")
  private List<User> users = new ArrayList<>();

  @Version
  private long version;

  public static Company create(String name) {
    Company company = new Company();
    company.name = name;
    company.count = 10L;
    return company;
  }

  public void updateName(String name) {
    this.name = name;
  }

  public void decreaseCount() {
    count--;
  }
}