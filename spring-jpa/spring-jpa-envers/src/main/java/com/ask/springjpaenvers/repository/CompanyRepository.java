package com.ask.springjpaenvers.repository;

import com.ask.springjpaenvers.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface CompanyRepository extends JpaRepository<Company, String>, RevisionRepository<Company, String, Long> {

}
