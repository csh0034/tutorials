package com.ask.springjpaquerydsl.entity;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tb_child")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class Child {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;

  private long totalCount;

  private long processedCount;

  @Exclude
  @ManyToOne(fetch = LAZY, optional = false)
  private Parent parent;
}
