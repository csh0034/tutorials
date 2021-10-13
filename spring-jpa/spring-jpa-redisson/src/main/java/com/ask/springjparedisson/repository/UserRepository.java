package com.ask.springjparedisson.repository;

import com.ask.springjparedisson.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
