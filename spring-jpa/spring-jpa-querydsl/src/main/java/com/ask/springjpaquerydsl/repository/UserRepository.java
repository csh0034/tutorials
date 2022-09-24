package com.ask.springjpaquerydsl.repository;

import com.ask.springjpaquerydsl.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface UserRepository extends JpaRepository<User, String>, QuerydslPredicateExecutor<User> {

}
