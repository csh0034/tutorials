package com.ask.springbatch.repository;

import com.ask.springbatch.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
