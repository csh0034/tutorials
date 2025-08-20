package com.ask.springjpaquerydsl.entity;

import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tb_parent")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class Parent {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;

  private long totalCount;

  @Enumerated(EnumType.STRING)
  private Status status;

  public enum Status {
    START, END
  }
}
