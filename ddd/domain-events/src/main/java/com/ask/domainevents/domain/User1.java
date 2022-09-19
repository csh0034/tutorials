package com.ask.domainevents.domain;

import static lombok.AccessLevel.PROTECTED;

import java.io.Serializable;
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
@Table(name = "mt_user1")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class User1 implements Serializable {

  private static final long serialVersionUID = 9168334183589288617L;

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "user_id")
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  public User1(String name) {
    this.name = name;
  }

}
