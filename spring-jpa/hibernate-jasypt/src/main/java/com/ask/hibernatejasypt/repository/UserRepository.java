package com.ask.hibernatejasypt.repository;

import com.ask.hibernatejasypt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

  User findByData(String data);

}
