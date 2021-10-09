package com.ask.springjpajcache.repository;

import com.ask.springjpajcache.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, String> {

}
