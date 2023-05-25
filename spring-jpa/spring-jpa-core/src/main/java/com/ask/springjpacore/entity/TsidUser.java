package com.ask.springjpacore.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tb_tsid_user")
@Getter
@Setter
@ToString
public class TsidUser {

  @Id
  @GeneratedValue(generator = "tsid")
  @GenericGenerator(name = "tsid", strategy = "io.hypersistence.utils.hibernate.id.TsidGenerator")
  private long id;

}
