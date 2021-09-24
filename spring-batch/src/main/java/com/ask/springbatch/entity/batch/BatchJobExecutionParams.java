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
@Table(name = "tb_batch_job_execution_params")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BatchJobExecutionParams implements Serializable {

  private static final long serialVersionUID = 7355690287868068653L;

  @Id
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "job_execution_id")
  private BatchJobExecution batchJobExecution;

  @Column(name = "type_cd", nullable = false, length = 6)
  private String typeCd;

  @Column(name = "key_name", nullable = false, length = 100)
  private String keyName;

  @Column(name = "string_val", length = 250)
  private String stringVal;

  @Column(name = "date_val")
  private LocalDateTime dateVal;

  @Column(name = "long_val")
  private Long longVal;

  @Column(name = "double_val")
  private Double doubleVal;

  @Column(name = "identifying", nullable = false, length = 1)
  private String identifying;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    BatchJobExecutionParams that = (BatchJobExecutionParams) o;
    return Objects.equals(batchJobExecution, that.batchJobExecution);
  }

  @Override
  public int hashCode() {
    return 0;
  }
}
