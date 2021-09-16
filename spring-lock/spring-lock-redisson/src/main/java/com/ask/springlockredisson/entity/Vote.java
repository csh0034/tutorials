package com.ask.springlockredisson.entity;

import static lombok.AccessLevel.PROTECTED;

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
@Table(name = "tb_vote")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class Vote {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "user_id")
  private String id;

  private String voter;

  private String candidate;

  public static Vote create(String voter, String candidate) {
    Vote vote = new Vote();
    vote.voter = voter;
    vote.candidate = candidate;
    return vote;
  }
}
