package com.ask.domainevents.domain;

import static lombok.AccessLevel.PROTECTED;

import com.ask.domainevents.domain.event.DomainEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

@Entity
@Table(name = "mt_user3")
@Slf4j
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class User3 implements Serializable {

  private static final long serialVersionUID = 1941788300412621364L;

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "user_id")
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  @Transient
  private final Collection<DomainEvent> domainEvents = new ArrayList<>();

  public User3(String name) {
    this.name = name;
  }

  public void operation() {
    // business logic...
    domainEvents.add(new DomainEvent(id));
  }

  @AfterDomainEventPublication
  public void clearEvents() {
    log.info("clearEvents...");
    domainEvents.clear();
  }

  @DomainEvents
  public Collection<DomainEvent> events() {
    log.info("events...");
    return domainEvents;
  }

}
