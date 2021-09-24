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
@Table(name = "tb_batch_step_execution_context")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BatchStepExecutionContext implements Serializable {

  private static final long serialVersionUID = -6777930716845305788L;

  @Id
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "step_execution_id")
  private BatchStepExecution batchStepExecution;

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
    BatchStepExecutionContext that = (BatchStepExecutionContext) o;
    return Objects.equals(batchStepExecution, that.batchStepExecution);
  }

  @Override
  public int hashCode() {
    return 0;
  }
}
