package com.ask.multitenancy.repository.tenant;

import com.ask.multitenancy.entity.tenant.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, String> {

}
