package com.ask.springjpaenvers.repository;

import com.ask.springjpaenvers.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
