package com.ask.springjpaquerydsl.repository;

import com.ask.springjpaquerydsl.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, String> {

}