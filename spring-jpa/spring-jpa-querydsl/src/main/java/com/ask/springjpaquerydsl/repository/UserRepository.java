package com.ask.springjpaquerydsl.repository;

import com.ask.springjpaquerydsl.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
