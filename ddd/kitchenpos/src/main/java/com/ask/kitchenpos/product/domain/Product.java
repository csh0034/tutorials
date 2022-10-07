package com.ask.kitchenpos.product.domain;

import static lombok.AccessLevel.PROTECTED;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "product")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class Product implements Serializable {

  private static final long serialVersionUID = -1841395120976677641L;

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "user_id")
  private String id;

  @Embedded
  private DisplayedName displayedName;

  @Column(name = "price", nullable = false)
  private BigDecimal price;

  public Product(DisplayedName displayedName, BigDecimal price) {
    this.displayedName = displayedName;
    this.price = price;
  }

}
