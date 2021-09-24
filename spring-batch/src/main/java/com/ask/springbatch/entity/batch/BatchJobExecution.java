package com.ask.springbatch.entity.batch;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

@Entity
@Table(name = "tb_batch_job_execution")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BatchJobExecution implements Serializable {

  private static final long serialVersionUID = -7513365146452113682L;

  @Id
  @Column(name = "job_execution_id")
  private Long id;

  private Long version;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "job_instance_id")
  private BatchJobInstance batchJobInstance;

  @Column(name = "create_time", nullable = false)
  private LocalDateTime createTime;

  @Column(name = "start_time")
  private LocalDateTime startTime;

  @Column(name = "end_time")
  private LocalDateTime endTime;

  @Column(name = "status", length = 10)
  private String status;

  @Column(name = "exit_code", length = 2500)
  private String exitCode;

  @Column(name = "exit_message", length = 2500)
  private String exitMessage;

  @Column(name = "last_updated")
  private LocalDateTime lastUpdated;

  @Column(name = "job_configuration_location", length = 2500)
  private String jobConfigurationLocation;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    BatchJobExecution that = (BatchJobExecution) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return 0;
  }
}
