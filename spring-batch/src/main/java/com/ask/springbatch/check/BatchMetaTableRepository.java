package com.ask.springbatch.check;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.repository.dao.AbstractJdbcBatchMetadataDao;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@Slf4j
@RequiredArgsConstructor
public class BatchMetaTableRepository {

  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final BatchProperties batchProperties;

  private static final String COUNT_BATCH_SQL = ""
      + "SELECT COUNT(1) AS CNT FROM %PREFIX%JOB_INSTANCE BJI JOIN %PREFIX%JOB_EXECUTION BJE "
      + "ON BJI.JOB_INSTANCE_ID = BJE.JOB_INSTANCE_ID WHERE BJE.STATUS = :status AND BJI.JOB_NAME = :jobName AND BJE.END_TIME > :endTime ";

  private static final String MAX_JOB_EXECUTION_ID_SQL = ""
      + "SELECT MAX(BJE.JOB_EXECUTION_ID) AS JOB_EXECUTION_ID FROM %PREFIX%JOB_INSTANCE BJI JOIN %PREFIX%JOB_EXECUTION BJE "
      + "ON BJI.JOB_INSTANCE_ID = BJE.JOB_INSTANCE_ID WHERE BJI.JOB_NAME = :jobName";

  private static final String SELECT_BATCH_STATUS_SQL = ""
      + "SELECT BJE.STATUS AS STATUS FROM %PREFIX%JOB_INSTANCE BJI JOIN %PREFIX%JOB_EXECUTION BJE "
      + "ON BJI.JOB_INSTANCE_ID = BJE.JOB_INSTANCE_ID WHERE BJE.JOB_EXECUTION_ID = :jobExecutionId";

  public long countBatch(String status, String batchName, Duration duration) {
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue("status", status);
    namedParameters.addValue("jobName", batchName);
    namedParameters.addValue("endTime", Timestamp.valueOf(LocalDateTime.now().minus(duration)));

    Long cnt = jdbcTemplate.queryForObject(getReplacedPrefixQuery(COUNT_BATCH_SQL), namedParameters,
        (rs, i) -> rs.getLong("CNT"));
    return cnt != null ? cnt : 0;
  }

  public long countFailedBatch(String batchName, Duration duration) {
    return countBatch(BatchStatus.FAILED.name(), batchName, duration);
  }

  public long countCompletedBatch(String batchName, Duration duration) {
    return countBatch(BatchStatus.COMPLETED.name(), batchName, duration);
  }

  public Long findMaxJobExecutionIdByJobName(String batchName) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("jobName", batchName);

    try {
      Long jobExecutionId = jdbcTemplate.queryForObject(getReplacedPrefixQuery(MAX_JOB_EXECUTION_ID_SQL), params,
          (rs, i) -> rs.getLong("JOB_EXECUTION_ID"));

      if (jobExecutionId != null && jobExecutionId != 0) {
        return jobExecutionId;
      }
      return null;
    } catch (Exception ex) {
      log.error(String.format("%s: batchName=%s, message=%s", ex.getClass().getSimpleName(), batchName, ex.getMessage()));
      return null;
    }
  }

  public String findStatusByJobExecutionId(Long jobExecutionId) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("jobExecutionId", jobExecutionId);

    try {
      return jdbcTemplate.queryForObject(getReplacedPrefixQuery(SELECT_BATCH_STATUS_SQL), params,
          (rs, i) -> rs.getString("STATUS"));
    } catch (Exception ex) {
      log.error(String.format("%s: executionId=%d, message=%s", ex.getClass().getSimpleName(), jobExecutionId, ex.getMessage()));
      return null;
    }
  }

  private String getReplacedPrefixQuery(String sql) {
    return StringUtils.replace(sql, "%PREFIX%", getTablePrefix());
  }

  private String getTablePrefix() {
    if (StringUtils.hasText(batchProperties.getJdbc().getTablePrefix())) {
      return batchProperties.getJdbc().getTablePrefix();
    }
    return AbstractJdbcBatchMetadataDao.DEFAULT_TABLE_PREFIX;
  }

}
