package com.ask.springjpacore.entity.auto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tb_auto")
@Getter
@Setter
@ToString
public class Auto {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

}
