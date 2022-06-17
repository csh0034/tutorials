package com.ask.springjpacore.service;

import com.ask.springjpacore.entity.User;
import com.ask.springjpacore.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends GenericService<User, String, UserRepository> {

}
