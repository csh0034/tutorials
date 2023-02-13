package com.ask.quickperf.entity.company;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(exclude = "company")
public class CompanyOption {

  @Id
  private String id;

  private String option1;

  /**
   * 외래키 생성을 위해선 @MapsId + @OneToOne 또는 @OneToOne(optional = false) 를 사용해야 한다.
   */
  @OneToOne(optional = false)
  @JoinColumn(name = "id")
  private Company company;

}
