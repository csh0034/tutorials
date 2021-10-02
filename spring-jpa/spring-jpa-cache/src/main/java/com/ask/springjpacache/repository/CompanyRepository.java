package com.ask.springjpacache.repository;

import com.ask.springjpacache.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, String> {

}
