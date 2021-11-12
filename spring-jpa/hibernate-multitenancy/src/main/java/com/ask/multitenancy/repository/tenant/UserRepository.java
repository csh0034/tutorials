package com.ask.multitenancy.repository.tenant;

import com.ask.multitenancy.entity.tenant.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
