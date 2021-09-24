package com.ask.springbatch.entity.batch;

import java.io.Serializable;
import java.time.LocalDateTime;
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

@Entity
@Table(name = "tb_batch_step_execution")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BatchStepExecution implements Serializable {

  private static final long serialVersionUID = -4532422167860696208L;

  @Id
  @Column(name = "step_execution_id")
  private Long id;

  @Column(name = "version", nullable = false)
  private Long version;

  @Column(name = "step_name", nullable = false, length = 100)
  private String stepName;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "job_execution_id")
  private BatchJobExecution batchJobExecution;

  @Column(name = "start_time")
  private LocalDateTime startTime;

  @Column(name = "end_time")
  private LocalDateTime endTime;

  @Column(name = "status", length = 10)
  private String status;

  @Column(name = "commit_count")
  private Long commitCount;

  @Column(name = "read_count")
  private Long readCount;

  @Column(name = "filter_count")
  private Long filterCount;

  @Column(name = "write_count")
  private Long writeCount;

  @Column(name = "read_skip_count")
  private Long readSkipCount;

  @Column(name = "write_skip_count")
  private Long writeSkipCount;

  @Column(name = "process_skip_count")
  private Long processSkipCount;

  @Column(name = "rollback_count")
  private Long rollbackCount;

  @Column(name = "exit_code", length = 2500)
  private String exitCode;

  @Column(name = "exit_message", length = 2500)
  private String exitMessage;

  @Column(name = "last_updated")
  private LocalDateTime lastUpdated;
}
