package com.ask.multitenancy.entity.tenant;

import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Cacheable;
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
@Table(name = "tb_conference")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
@Cacheable
public class Conference {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "conference_id")
  private String id;

  private String name;
}
