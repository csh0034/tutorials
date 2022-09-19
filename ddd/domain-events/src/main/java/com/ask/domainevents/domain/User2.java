package com.ask.domainevents.domain;

import static lombok.AccessLevel.PROTECTED;

import com.ask.domainevents.config.Events;
import com.ask.domainevents.domain.event.DomainEvent;
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
@Table(name = "mt_user2")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class User2 implements Serializable {

  private static final long serialVersionUID = 1058560104161668822L;

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "user_id")
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  public User2(String name) {
    this.name = name;
  }

  public void operation() {
    // business logic...
    Events.publish(new DomainEvent(id));
  }

}
