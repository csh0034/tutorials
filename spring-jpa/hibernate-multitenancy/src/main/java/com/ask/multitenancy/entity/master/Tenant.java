package com.ask.multitenancy.entity.master;

import static lombok.AccessLevel.PROTECTED;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "tb_tenant")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Tenant extends BaseEntity implements Persistable<String> {

  private static final long serialVersionUID = -1512447590330072206L;

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

  @Builder
  public Tenant(String id, String dbName, String dbAddress, String dbUsername, String dbPassword) {
    this.id = id;
    this.dbName = dbName;
    this.dbAddress = dbAddress;
    this.dbUsername = dbUsername;
    this.dbPassword = dbPassword;
  }

  @Override
  public boolean isNew() {
    return this.createdDt == null;
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
