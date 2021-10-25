package com.ask.quickperf.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Player {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  private Team team;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "player", optional = false)
  private Coach coach;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "player", optional = false)
  private Locker locker;

  public void setTeam(Team team) {
    this.team = team;
    team.setPlayer(this);
  }
}
