package com.ask.springbatch.entity.batch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_batch_job_instance",
    uniqueConstraints = @UniqueConstraint(columnNames = { "job_name", "job_name" }))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BatchJobInstance implements Serializable {

  private static final long serialVersionUID = 222163305212949108L;

  @Id
  @Column(name = "job_instance_id")
  private Long id;

  private Long version;

  @Column(name = "job_name", nullable = false, length = 100)
  private String jobName;

  @Column(name = "job_key", nullable = false, length = 32)
  private String jobKey;

  @OneToMany(mappedBy = "batchJobInstance", cascade = CascadeType.ALL)
  private List<BatchJobExecution> batchJobExecutions = new ArrayList<>();
}
