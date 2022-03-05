package com.ask.springjpaenvers.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity
@Getter
@RevisionEntity
@Table(name = "tb_revinfo")
//@Proxy(lazy = false)
public class Revision implements Serializable {

  private static final long serialVersionUID = 7703526253442389993L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @RevisionNumber
  private Long rev;

  @RevisionTimestamp
  private Long timestamp;

  @Transient
  public Date getRevisionDate() {
    return new Date(timestamp);
  }

}
