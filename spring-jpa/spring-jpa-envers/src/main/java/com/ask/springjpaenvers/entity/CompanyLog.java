package com.ask.springjpaenvers.entity;

import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "tb_company_log")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class CompanyLog extends BaseEntity implements Persistable<String> {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "company_log_id")
  private String id;

  private String companyId;

  private String name;

  private long count;

  @Enumerated(EnumType.STRING)
  private LogType logType;

  public static CompanyLog create(String companyId, String name, long count, LogType logType) {
    CompanyLog companyLog = new CompanyLog();
    companyLog.companyId = companyId;
    companyLog.name = name;
    companyLog.count = count;
    companyLog.logType = logType;
    return companyLog;
  }

  @Override
  public boolean isNew() {
    return getCreatedAt() == null;
  }

  public enum LogType {
    CREATE, UPDATE, DELETE
  }
}
