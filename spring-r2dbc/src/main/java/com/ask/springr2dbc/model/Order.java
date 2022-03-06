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

@Table("mt_order")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order implements Persistable<String> {

  @Id
  @EqualsAndHashCode.Include
  private String id;

  private String name;

  @Column("user_id")
  private String userId;

  @Column("created_dt")
  @CreatedDate
  private LocalDateTime createdDt;

  @Column("created_by")
  @CreatedBy
  private String createdBy;

  public static Order create(String name, String userId) {
    Order order = new Order();
    order.name = name;
    order.userId = userId;
    return order;
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
