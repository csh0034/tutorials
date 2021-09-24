package com.ask.springbatch.entity.batch;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_batch_step_execution_seq")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BatchStepExecutionSeq implements Serializable {

  private static final long serialVersionUID = 7832952846917501510L;

  @Id
  private Long id;

  @Column(name = "unique_Key", unique = true, nullable = false)
  private String uniqueKey;
}
