package com.ask.multitenancy.repository;

import com.ask.multitenancy.entity.base.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
