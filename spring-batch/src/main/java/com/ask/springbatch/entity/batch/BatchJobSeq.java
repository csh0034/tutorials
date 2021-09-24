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
@Table(name = "tb_batch_job_seq")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BatchJobSeq implements Serializable {

  private static final long serialVersionUID = 4312497024512010829L;

  @Id
  private Long id;

  @Column(name = "unique_Key", unique = true, nullable = false)
  private String uniqueKey;
}
