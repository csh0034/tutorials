package com.ask.springjpajcache.entity;

import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "tb_role")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Immutable
public class Role {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "role_id")
  private String id;

  private String name;
}
