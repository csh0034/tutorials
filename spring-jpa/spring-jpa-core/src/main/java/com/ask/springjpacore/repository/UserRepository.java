package com.ask.springjpacore.repository;

import com.ask.springjpacore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
