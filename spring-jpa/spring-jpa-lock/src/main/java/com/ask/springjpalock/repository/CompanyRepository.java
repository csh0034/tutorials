package com.ask.springjpalock.repository;

import com.ask.springjpalock.entity.Company;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface CompanyRepository extends JpaRepository<Company, String> {

  @Lock(LockModeType.OPTIMISTIC)
  Company findOptimisticLockCompanyByName(String name);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Company findPessimisticLockCompanyById(String id);
}
