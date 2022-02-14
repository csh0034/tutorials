package com.ask.springr2dbc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("mt_user")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements Persistable<String> {

  @Id
  @EqualsAndHashCode.Include
  private String id;

  private String name;

  private Integer age;

  @Column("created_dt")
  @CreatedDate
  private LocalDateTime createdDt;

  @Column("created_by")
  @CreatedBy
  private String createdBy;

  public static User create(String name, Integer age) {
    User user = new User();
    user.name = name;
    user.age = age;
    return user;
  }

  public void update(User other) {
    this.name = other.name;
    this.age = other.age;
  }

  @Override
  @JsonIgnore
  public boolean isNew() {
    if (createdDt == null) {
      if (id == null) {
        id = UUID.randomUUID().toString();
      }
      return true;
    }
    return false;
  }

}
