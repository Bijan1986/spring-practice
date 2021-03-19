package com.example.demo.user;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface UserService {

	List<User> getAllUsers();

	User saveuser(User user);

	User findUserById(Integer id);

}
