package com.ask.springjpaenvers.entity;

import static lombok.AccessLevel.PROTECTED;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Table(name = "tb_company")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
@Audited
public class Company extends BaseEntity {

  private static final long serialVersionUID = 8664127818368505411L;

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "company_id")
  private String id;

  private String name;

  private long count;

  @NotAudited
  @Exclude
  @OneToMany(mappedBy = "company")
  private List<User> users = new ArrayList<>();

  public static Company create(String name) {
    Company company = new Company();
    company.name = name;
    company.count = 10L;
    return company;
  }

  public void updateName(String name) {
    this.name = name;
  }

}