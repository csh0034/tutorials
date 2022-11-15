package com.ask.springbatch.check;

import com.ask.springbatch.check.BatchCheckPolicy.Policy;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BatchCheckManager {

  private final BatchCheckPolicyRepository batchCheckPolicyRepository;
  private final BatchMetaTableRepository batchMetaTableRepository;

  public void checkFailedBatch() {
    List<BatchCheckPolicy> batchCheckPolicies = batchCheckPolicyRepository.findAllByOrderByPriority();

    batchCheckPolicies.forEach(batchCheckPolicy -> {
      batchCheckPolicy.validate();

      Policy policy = batchCheckPolicy.getPolicy();
      if (policy == Policy.NONE) {
        return;
      }
      if (policy == Policy.NEVER_FAILED) {
        checkNeverFailedPolicy(batchCheckPolicy);
      }
      if (policy == Policy.SUCCESS_COUNT) {
        checkSuccessCountPolicy(batchCheckPolicy);
      }
      if (policy == Policy.LAST_SUCCESS) {
        checkLastSuccessPolicy(batchCheckPolicy);
      }
    });
  }

  private void checkNeverFailedPolicy(BatchCheckPolicy batchCheckPolicy) {
    Duration duration = batchCheckPolicy.getParsedDuration();

    long failedCnt = batchMetaTableRepository.countFailedBatch(batchCheckPolicy.getBatchName(), duration);

    if (failedCnt > 0) {
      String message = String.format("batchName: %s, policy: %s, failedCnt: %d",
          batchCheckPolicy.getBatchName(),
          batchCheckPolicy.getPolicy().name(),
          failedCnt);

      throw new IllegalStateException(message);
    }
  }

  private void checkSuccessCountPolicy(BatchCheckPolicy batchCheckPolicy) {
    Duration duration = batchCheckPolicy.getParsedDuration();
    Integer count = batchCheckPolicy.getCount();

    long completedCnt = batchMetaTableRepository.countCompletedBatch(batchCheckPolicy.getBatchName(), duration);

    if (completedCnt < count) {
      String message = String.format("batchName: %s, policy: %s, completedCnt: %d, count: %d",
          batchCheckPolicy.getBatchName(),
          batchCheckPolicy.getPolicy().name(),
          completedCnt,
          count);

      throw new IllegalStateException(message);
    }
  }

  private void checkLastSuccessPolicy(BatchCheckPolicy batchCheckPolicy) {
    Long jobExecutionId = batchMetaTableRepository.findMaxJobExecutionIdByJobName(batchCheckPolicy.getBatchName());

    if (jobExecutionId == null) {
      return;
    }

    String status = batchMetaTableRepository.findStatusByJobExecutionId(jobExecutionId);

    if (status != null && BatchStatus.match(status).isUnsuccessful()) {
      String message = String.format("batchName: %s, policy: %s, status: %s",
          batchCheckPolicy.getBatchName(),
          batchCheckPolicy.getPolicy().name(),
          status);

      throw new IllegalStateException(message);
    }
  }

}
