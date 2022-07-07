package com.ask.prometheus.entity;

import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "mt_endpoint")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class Endpoint {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;

  private String name;

  public static Endpoint create(String name) {
    Endpoint endpoint = new Endpoint();
    endpoint.name = name;
    return endpoint;
  }

}
