package com.ask.springjpacache.repository;

import com.ask.springjpacache.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
