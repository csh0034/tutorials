package com.ask.springjpalock.repository;

import com.ask.springjpalock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
