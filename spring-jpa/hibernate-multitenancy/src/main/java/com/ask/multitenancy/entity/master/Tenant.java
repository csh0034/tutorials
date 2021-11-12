package com.ask.multitenancy.entity.master;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "tb_tenant")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Tenant implements Persistable<String> {

  @Id
  @Column(length = 50)
  private String id;

  private String dbName;

  private String dbAddress;

  private String dbUsername;

  private String dbPassword;

  public String getJdbcUrl() {
    return String.format("jdbc:mariadb://%s/%s?createDatabaseIfNotExist=true", dbAddress, dbName);
  }

  @CreatedDate
  private LocalDateTime createdDt;

  @Override
  public boolean isNew() {
    return createdDt == null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Tenant tenant = (Tenant) o;
    return id != null && Objects.equals(id, tenant.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
