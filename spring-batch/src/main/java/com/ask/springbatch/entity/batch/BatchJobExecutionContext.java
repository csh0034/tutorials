package com.ask.springbatch.entity.batch;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

@Entity
@Table(name = "tb_batch_job_execution_context")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BatchJobExecutionContext implements Serializable {

  private static final long serialVersionUID = 8010980110247372527L;

  @Id
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "job_execution_id")
  private BatchJobExecution batchJobExecution;

  @Column(name = "short_context", nullable = false, length = 2500)
  private String shortContext;

  @Lob
  @Column(name = "serialized_context")
  private String serializedContext;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    BatchJobExecutionContext that = (BatchJobExecutionContext) o;
    return Objects.equals(batchJobExecution, that.batchJobExecution);
  }

  @Override
  public int hashCode() {
    return 0;
  }
}
