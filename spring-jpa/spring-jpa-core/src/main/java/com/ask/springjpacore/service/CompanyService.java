package com.ask.springjpacore.service;

import com.ask.springjpacore.entity.Company;
import com.ask.springjpacore.repository.CompanyRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanyService extends GenericService<Company, String, CompanyRepository> {

}

