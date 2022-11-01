package com.ask.springrest.domain;

import static lombok.AccessLevel.PROTECTED;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "product")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class Product implements Serializable {

  private static final long serialVersionUID = -1841395120976677641L;

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;

  private String name;

  public Product(String name) {
    this.name = name;
  }

}
