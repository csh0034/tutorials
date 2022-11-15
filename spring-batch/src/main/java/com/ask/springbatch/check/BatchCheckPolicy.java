package com.ask.springbatch.check;

import java.io.Serializable;
import java.time.Duration;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "zt_batch_check_policy")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@ToString
public class BatchCheckPolicy implements Serializable {

  private static final long serialVersionUID = 7060219363042282690L;

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid2")
  @Column(name = "id", length = 50)
  private String id;

  @Column(name = "batch_name", length = 50, nullable = false)
  private String batchName;

  @Enumerated(EnumType.STRING)
  @Column(name = "policy", length = 50, nullable = false)
  private Policy policy;

  @Column(name = "duration", length = 10)
  private String duration;

  private Integer count;

  private Integer priority;

  public Duration getParsedDuration() {
    return Duration.parse(duration);
  }

  public void validate() {
    if (policy.useDuration) {
      try {
        Duration.parse(duration);
      } catch (Exception e) {
        throw new IllegalArgumentException("invalid duration", e);
      }
    }
    if (policy.useCount && count == null) {
      throw new IllegalArgumentException("invalid count");
    }
  }

  @RequiredArgsConstructor
  @Getter
  public enum Policy {
    NONE,
    NEVER_FAILED(true),
    SUCCESS_COUNT(true, true),
    LAST_SUCCESS;

    private final boolean useDuration;
    private final boolean useCount;

    Policy() {
      this(false, false);
    }

    Policy(boolean useDuration) {
      this(useDuration, false);
    }
  }

}
