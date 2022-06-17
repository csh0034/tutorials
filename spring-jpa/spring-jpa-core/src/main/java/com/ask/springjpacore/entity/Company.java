package com.ask.springjpacore.entity;

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

  public static Company create(String name) {
    Company company = new Company();
    company.name = name;
    return company;
  }

}
