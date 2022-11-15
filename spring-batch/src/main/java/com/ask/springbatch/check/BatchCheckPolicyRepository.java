package com.ask.springbatch.check;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchCheckPolicyRepository extends JpaRepository<BatchCheckPolicy, String> {

  List<BatchCheckPolicy> findAllByOrderByPriority();

}
