package com.ask.springjpajcache.repository;

import com.ask.springjpajcache.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
