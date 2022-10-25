package com.ask.springjpacore.entity;

import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "tb_mission")
@SQLDelete(sql = "update tb_mission set deleted = true where id = ?")
@Where(clause = "deleted = false")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class Mission {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;

  private String name;

  private boolean deleted;

  public static Mission create(String name) {
    Mission mission = new Mission();
    mission.name = name;
    mission.deleted = false;
    return mission;
  }

}
