package com.ask.springjpalock.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tb_room_message")
@NoArgsConstructor
@Getter
@Setter
public class RoomMessage {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;

  private String message;

  @ManyToOne(fetch = FetchType.LAZY)
  private RoomMember sender;

  private LocalDateTime createdAt = LocalDateTime.now();

}
