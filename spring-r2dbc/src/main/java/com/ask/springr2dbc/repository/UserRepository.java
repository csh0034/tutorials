package com.ask.springr2dbc.repository;

import com.ask.springr2dbc.model.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface UserRepository extends R2dbcRepository<User, Long> {

}
