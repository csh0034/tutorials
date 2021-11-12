package com.ask.multitenancy.repository.master;

import com.ask.multitenancy.entity.master.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, String> {

}
