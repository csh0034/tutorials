package com.ask.springjpaquerydsl.entity;

import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tb_statistics")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class Statistics {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "statistics_id")
  private String id;

  private String year;

  private String month;

  private String day;

  private String data;

  @Column(nullable = false)
  @ColumnDefault("0")
  private Integer viewCount;
}
