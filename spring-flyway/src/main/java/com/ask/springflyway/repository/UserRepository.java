package com.ask.springflyway.repository;

import com.ask.springflyway.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
